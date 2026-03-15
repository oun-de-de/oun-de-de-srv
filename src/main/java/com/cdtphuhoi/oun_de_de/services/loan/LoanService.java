package com.cdtphuhoi.oun_de_de.services.loan;

import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import static com.cdtphuhoi.oun_de_de.utils.Utils.startOfDayInCambodia;
import static com.cdtphuhoi.oun_de_de.utils.Utils.toCambodiaLocalDateTime;
import com.cdtphuhoi.oun_de_de.common.BorrowerType;
import com.cdtphuhoi.oun_de_de.common.LoanStatus;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.Customer_;
import com.cdtphuhoi.oun_de_de.entities.Loan;
import com.cdtphuhoi.oun_de_de.entities.LoanPayment;
import com.cdtphuhoi.oun_de_de.entities.LoanPayment_;
import com.cdtphuhoi.oun_de_de.entities.Loan_;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.entities.User_;
import com.cdtphuhoi.oun_de_de.exceptions.BadRequestException;
import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.BaseRepository;
import com.cdtphuhoi.oun_de_de.repositories.CustomerRepository;
import com.cdtphuhoi.oun_de_de.repositories.LoanPaymentRepository;
import com.cdtphuhoi.oun_de_de.repositories.LoanRepository;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.loan.dto.CreateLoanData;
import com.cdtphuhoi.oun_de_de.services.loan.dto.CreateLoanPaymentData;
import com.cdtphuhoi.oun_de_de.services.loan.dto.ExtendLoanData;
import com.cdtphuhoi.oun_de_de.services.loan.dto.LoanPaymentResult;
import com.cdtphuhoi.oun_de_de.services.loan.dto.LoanResult;
import com.cdtphuhoi.oun_de_de.services.loan.dto.UpdateLoanData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoanService implements OrgManagementService {

    public static final int DAY_IN_MONTH = 30;
    public static final int DEFAULT_DUE_WARNING_DAYS = 5;

    private final LoanRepository loanRepository;

    private final LoanPaymentRepository loanPaymentRepository;

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
        var loans = page.getContent();
        var borrowerIdsByType = loans.stream()
            .collect(
                Collectors.groupingBy(
                    Loan::getBorrowerType,
                    Collectors.mapping(
                        Loan::getBorrowerId,
                        Collectors.toSet()
                    )
                )
            );
        var employees = Optional.ofNullable(borrowerIdsByType.get(BorrowerType.EMPLOYEE))
            .map(employeeIds -> userRepository.findAll(
                        Specification.allOf(
                            (root, query, cb) -> root.get(User_.ID).in(employeeIds)
                        )
                    )
                    .stream()
                    .collect(
                        Collectors.toMap(
                            User::getId,
                            u -> String.join(" ", u.getFirstName(), u.getLastName())
                        )
                    )
            )
            .orElseGet(Map::of);

        var customers = Optional.ofNullable(borrowerIdsByType.get(BorrowerType.CUSTOMER))
            .map(
                customerIds -> customerRepository.findAll(
                        Specification.allOf(
                            (root, query, cb) -> root.get(Customer_.ID).in(customerIds)
                        )
                    )
                    .stream()
                    .collect(
                        Collectors.toMap(
                            Customer::getId,
                            Customer::getName
                        )
                    )
            )
            .orElseGet(Map::of);

        var pageResult = page.map(MapperHelpers.getLoanMapper()::toLoanResult);
        pageResult.getContent().forEach(
            loanResult -> {
                var borrowerNamesById = loanResult.getBorrowerType().equals(BorrowerType.EMPLOYEE) ?
                    employees : customers;
                loanResult.setBorrowerName(borrowerNamesById.get(loanResult.getBorrowerId()));
            }
        );
        return pageResult;
    }

    public LoanResult createLoan(CreateLoanData createLoanData) {
        if (createLoanData.getLoanInstallmentAmount()
            .compareTo(createLoanData.getPrincipalAmount()) > 0) {
            throw new BadRequestException("Instalment amount can not greater than principal amount");
        }
        var borrowerRepository = repositoryByBorrowerType.get(createLoanData.getBorrowerType());
        var borrower = borrowerRepository.findOneById(createLoanData.getBorrowerId())
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Borrower [id=%s] not found", createLoanData.getBorrowerId())
                )
            );
        var startDate = toCambodiaLocalDateTime(createLoanData.getStartDate());
        var loan = Loan.builder()
            .orgId(borrower.getOrgId())
            .borrowerType(createLoanData.getBorrowerType())
            .borrowerId(createLoanData.getBorrowerId())
            .principalAmount(createLoanData.getPrincipalAmount())
            .paidAmount(BigDecimal.ZERO)
            .installmentAmount(createLoanData.getLoanInstallmentAmount())
            .dueWarningDays(createLoanData.getDueWarningDays() != null ?
                createLoanData.getDueWarningDays() :
                DEFAULT_DUE_WARNING_DAYS
            )
            .dueDate(startOfDayInCambodia(startDate).plusDays(DAY_IN_MONTH))
            .status(LoanStatus.NORMAL)
            .startDate(startDate)
            .createAt(cambodiaNow())
            .build();
        log.info("Creating loan and installments");
        var loanDb = loanRepository.save(loan);
        log.info("Created loan and installments");
        return MapperHelpers.getLoanMapper().toLoanResult(loanDb);
    }

    public LoanResult findLoanById(String loanId) {
        var loan = loanRepository.findOneById(loanId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Loan [id=%s] not found", loanId)
                )
            );
        var borrowerOpt = repositoryByBorrowerType.get(loan.getBorrowerType())
            .findOneById(loan.getBorrowerId());
        var borrowerName = borrowerOpt
            .filter(User.class::isInstance)
            .map(User.class::cast)
            .map(u -> String.join(" ", u.getFirstName(), u.getLastName()))
            .orElseGet(() -> ((Customer) borrowerOpt.get()).getName());
        var result = MapperHelpers.getLoanMapper().toLoanResult(loan);
        result.setBorrowerName(borrowerName);
        return result;
    }

    public LoanPaymentResult createPayment(String loanId, CreateLoanPaymentData createLoanPaymentData) {
        var loan = loanRepository.findOne(
                Specification.allOf(
                    (root, query, cb) -> cb.notEqual(root.get(Loan_.STATUS), LoanStatus.COMPLETE),
                    (root, query, cb) -> cb.equal(root.get(Loan_.ID), loanId)
                )
            )
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Loan [id=%s] is not found or has already closed", loanId)
                )
            );
        var remainingAmount = loan.getPrincipalAmount().subtract(loan.getPaidAmount());
        if (createLoanPaymentData.getAmount().compareTo(remainingAmount) > 0) {
            throw new BadRequestException(
                String.format("Not allowed to pay more than the remaining amount. " +
                    "Remaining amount: %.5f", remainingAmount)
            );
        }
        loan.setPaidAmount(loan.getPaidAmount().add(createLoanPaymentData.getAmount()));
        updateLoanStatus(createLoanPaymentData, loan);
        var payment = LoanPayment.builder()
            .orgId(loan.getOrgId())
            .loan(loan)
            .paymentDate(createLoanPaymentData.getPaymentDate())
            .amount(createLoanPaymentData.getAmount())
            .build();
        log.info("Creating payment");
        var paymentDb = loanPaymentRepository.save(payment);
        log.info("Created payment, id = {}", paymentDb.getId());
        return MapperHelpers.getLoanMapper().toLoanPaymentResult(paymentDb);
    }

    private static void updateLoanStatus(CreateLoanPaymentData createLoanPaymentData, Loan loan) {
        if (loan.getPaidAmount().equals(loan.getPrincipalAmount())) {
            loan.setStatus(LoanStatus.COMPLETE);
            return;
        }

        if (!createLoanPaymentData.isShouldUpdateDueDate()) {
            return;
        }
        updateLoanDueDateAndStatus(loan);
    }

    private static void updateLoanDueDateAndStatus(Loan loan) {
        var dueDate = calculateLatestLoanDueDate(loan);
        loan.setDueDate(dueDate);
        loan.setStatus(LoanStatus.NORMAL);
    }

    private static LocalDateTime calculateLatestLoanDueDate(Loan loan) {
        var now = cambodiaNow();
        var startDate = loan.getStartDate();
        var daysBetween = ChronoUnit.DAYS.between(startDate, now);
        /*
         * due date should always move to the next cycle from
         * eg: start: 15/02/2026, now: 15/03/2026 => cycle 15/02/2026 ->  17/03/2026
         * but be should move to the next of this cycle: 18/03/2026 -> 16/04/2026
         */
        var cycleIndex = Math.floorDiv(daysBetween, DAY_IN_MONTH) + 1;
        var cycleStart = startDate.plusDays(cycleIndex * DAY_IN_MONTH);
        return cycleStart.plusDays(DAY_IN_MONTH);
    }

    public List<LoanPaymentResult> findLoanPaymentsByLoanId(String loanId) {
        var payments = loanPaymentRepository.findAllByLoanId(
            loanId,
            Sort.by(Sort.Direction.DESC, LoanPayment_.PAYMENT_DATE)
        );
        return MapperHelpers.getLoanMapper().toListLoanPaymentResults(payments);
    }

    public LoanResult postponeLoan(String loanId) {
        var loan = loanRepository.findOneById(loanId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Loan [id=%s] is not found", loanId)
                )
            );
        updateLoanDueDateAndStatus(loan);
        var updated = loanRepository.save(loan);
        return MapperHelpers.getLoanMapper().toLoanResult(updated);
    }

    public LoanResult extendLoan(String loanId, ExtendLoanData extendLoanData) {
        var loan = loanRepository.findOne(
                Specification.allOf(
                    (root, query, cb) -> cb.notEqual(root.get(Loan_.STATUS), LoanStatus.COMPLETE),
                    (root, query, cb) -> cb.equal(root.get(Loan_.ID), loanId)
                )
            )
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Loan [id=%s] is not found or has already closed", loanId)
                )
            );
        loan.setPrincipalAmount(loan.getPrincipalAmount().add(extendLoanData.getAmount()));
        var updated = loanRepository.save(loan);
        return MapperHelpers.getLoanMapper().toLoanResult(updated);
    }

    public LoanResult updateLoan(String loanId, UpdateLoanData updateLoanData) {
        var loan = loanRepository.findOne(
                Specification.allOf(
                    (root, query, cb) -> cb.notEqual(root.get(Loan_.STATUS), LoanStatus.COMPLETE),
                    (root, query, cb) -> cb.equal(root.get(Loan_.ID), loanId)
                )
            )
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Loan [id=%s] is not found or has already closed", loanId)
                )
            );
        MapperHelpers.getLoanMapper().updateLoan(loan, updateLoanData);
        var updated = loanRepository.save(loan);
        return MapperHelpers.getLoanMapper().toLoanResult(updated);
    }
}
