package com.cdtphuhoi.oun_de_de.services.loan;

import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import static com.cdtphuhoi.oun_de_de.utils.Utils.startOfDayInCambodia;
import static com.cdtphuhoi.oun_de_de.utils.Utils.toCambodiaLocalDateTime;
import com.cdtphuhoi.oun_de_de.common.BorrowerType;
import com.cdtphuhoi.oun_de_de.common.LoanInstallmentStatus;
import com.cdtphuhoi.oun_de_de.entities.Loan;
import com.cdtphuhoi.oun_de_de.entities.LoanInstallment;
import com.cdtphuhoi.oun_de_de.entities.LoanInstallment_;
import com.cdtphuhoi.oun_de_de.entities.Loan_;
import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.BaseRepository;
import com.cdtphuhoi.oun_de_de.repositories.CustomerRepository;
import com.cdtphuhoi.oun_de_de.repositories.LoanInstallmentRepository;
import com.cdtphuhoi.oun_de_de.repositories.LoanRepository;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.loan.dto.CreateLoanData;
import com.cdtphuhoi.oun_de_de.services.loan.dto.LoanInstallmentResult;
import com.cdtphuhoi.oun_de_de.services.loan.dto.LoanResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import jakarta.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoanService implements OrgManagementService {

    private static final int DAY_IN_MONTH = 30;

    public static final int MONEY_SCALE = 5;

    private final LoanRepository loanRepository;

    private final LoanInstallmentRepository loanInstallmentRepository;

    private final CustomerRepository customerRepository;

    private final UserRepository userRepository;

    private Map<BorrowerType, BaseRepository<?>> repositoryByBorrowerType;

    @PostConstruct
    public void init() {
        repositoryByBorrowerType = Map.of(
            BorrowerType.CUSTOMER, customerRepository,
            BorrowerType.EMPLOYEE, userRepository
        );
    }

    public Page<LoanResult> findBy(
        BorrowerType borrowerType,
        String borrowerId,
        LocalDateTime from,
        LocalDateTime to,
        Pageable pageable
    ) {
        var page = loanRepository.findAll(
            Specification.allOf(
                LoanSpecifications.hasBorrowerId(borrowerId),
                LoanSpecifications.hasStartDateBetween(from, to),
                LoanSpecifications.hasBorrowerType(borrowerType)
            ),
            pageable
        );
        return page.map(MapperHelpers.getLoanMapper()::toLoanResult);
    }

    public LoanResult createLoan(CreateLoanData createLoanData) {
        var borrowerRepository = repositoryByBorrowerType.get(createLoanData.getBorrowerType());
        var borrower = borrowerRepository.findOneById(createLoanData.getBorrowerId())
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Borrower [id=%s] not found", createLoanData.getBorrowerId())
                )
            );
        var loan = Loan.builder()
            .orgId(borrower.getOrgId())
            .borrowerType(createLoanData.getBorrowerType())
            .borrowerId(createLoanData.getBorrowerId())
            .principalAmount(createLoanData.getPrincipalAmount())
            .termMonths(createLoanData.getTermMonths())
            .startDate(toCambodiaLocalDateTime(createLoanData.getStartDate()))
            .createAt(cambodiaNow())
            .build();
        var installments = createLoanInstallments(loan);
        log.info("Creating loan and installments");
        var installmentsDb = loanInstallmentRepository.saveAll(installments);
        log.info("Created loan and installments");
        return MapperHelpers.getLoanMapper().toLoanResult(
            // assert find first not null
            installmentsDb.stream().findFirst().get().getLoan()
        );
    }

    private List<LoanInstallment> createLoanInstallments(Loan loan) {
        var termMonths = loan.getTermMonths();
        var startDate = startOfDayInCambodia(loan.getStartDate());
        return IntStream.rangeClosed(1, termMonths)
            .mapToObj(idx -> (LoanInstallment)
                LoanInstallment.builder()
                    .orgId(loan.getOrgId())
                    .loan(loan)
                    .monthIndex(idx)
                    .dueDate(startDate.plusDays((long) DAY_IN_MONTH * idx))
                    .amount(
                        loan.getPrincipalAmount().divide(
                            BigDecimal.valueOf(termMonths),
                            MONEY_SCALE,
                            RoundingMode.HALF_EVEN
                        )
                    )
                    .status(LoanInstallmentStatus.UNPAID)
                    .build()
            )
            .toList();
    }

    public LoanResult findLoanById(String loanId) {
        var loan = loanRepository.findOneById(loanId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Loan [id=%s] not found", loanId)
                )
            );
        return MapperHelpers.getLoanMapper().toLoanResult(loan);
    }

    public List<LoanInstallmentResult> findLoanInstallmentsByLoanId(String loanId) {
        var installments = loanInstallmentRepository.findAllByLoanId(
            loanId,
            Sort.by(Sort.Direction.ASC, LoanInstallment_.MONTH_INDEX)
        );
        return MapperHelpers.getLoanMapper().toListLoanInstallmentResult(installments);
    }

    public LoanInstallmentResult payInstallment(String loanId, String installmentId) {
        var installments = loanInstallmentRepository.findAll(
            Specification.allOf(
                (root, query, cb) -> cb.equal(root.get(LoanInstallment_.ID), installmentId),
                (root, query, cb) -> cb.equal(root.get(LoanInstallment_.LOAN).get(Loan_.ID), loanId),
                (root, query, cb) -> root.get(LoanInstallment_.STATUS).in(List.of(LoanInstallmentStatus.UNPAID, LoanInstallmentStatus.OVERDUE))
            ),
            PageRequest.of(
                0,
                1,
                Sort.by(Sort.Direction.ASC, LoanInstallment_.MONTH_INDEX)
            )
        );
        if (installments.isEmpty()) {
            throw new ResourceNotFoundException(
                String.format("Loan Installment [id=%s], Loan [id=%s] not found", installmentId, loanId)
            );
        }
        var installment = installments.stream().findFirst().get();
        installment.setStatus(LoanInstallmentStatus.PAID);
        installment.setPaidAt(cambodiaNow());
        var updated = loanInstallmentRepository.save(installment);
        return MapperHelpers.getLoanMapper().toLoanInstallmentResult(updated);
    }
}
