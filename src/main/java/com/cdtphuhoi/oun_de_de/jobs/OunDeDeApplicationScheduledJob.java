package com.cdtphuhoi.oun_de_de.jobs;

import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import com.cdtphuhoi.oun_de_de.common.LoanInstallmentStatus;
import com.cdtphuhoi.oun_de_de.entities.LoanInstallment_;
import com.cdtphuhoi.oun_de_de.repositories.LoanInstallmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OunDeDeApplicationScheduledJob {

    private final LoanInstallmentRepository loanInstallmentRepository;

//    DEMO 10S CRON:
//    @Scheduled(cron = "*/10 * * * * *")

    /*
     * run at: 00:00, 04:00, 08:00, 12:00, 16:00, 20:00
     */
    @Scheduled(
        cron = "0 0 0/4 * * *",
        initialDelay = 60_000
    )
    public void scheduleUpdateLoanInstallmentStatus() {
        long now = System.currentTimeMillis() / 1000;
        log.info("Update installment status - {}", now);
        var overdueInstallments = loanInstallmentRepository.findAll(
            Specification.allOf(
                (root, query, cb) -> cb.lessThanOrEqualTo(root.get(LoanInstallment_.dueDate), cambodiaNow()),
                (root, query, cb) -> cb.equal(root.get(LoanInstallment_.STATUS), LoanInstallmentStatus.UNPAID)
            )
        );
        if (overdueInstallments.isEmpty()) {
            log.info("Empty overdue installments, no need to update");
            return;
        }
        log.info("Overdue installments size = {}", overdueInstallments.size());
        overdueInstallments.forEach(
            installment -> {
                installment.setStatus(LoanInstallmentStatus.OVERDUE);
            }
        );
        var updated = loanInstallmentRepository.saveAll(overdueInstallments);
        log.info("Overdue installments updated, affected row = {}", updated.size());

    }
}
