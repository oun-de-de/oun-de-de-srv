package com.cdtphuhoi.oun_de_de.jobs;

import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import com.cdtphuhoi.oun_de_de.common.LoanInstallmentStatus;
import com.cdtphuhoi.oun_de_de.common.PaymentTermCycleStatus;
import com.cdtphuhoi.oun_de_de.entities.LoanInstallment_;
import com.cdtphuhoi.oun_de_de.entities.PaymentTermCycle_;
import com.cdtphuhoi.oun_de_de.repositories.LoanInstallmentRepository;
import com.cdtphuhoi.oun_de_de.repositories.PaymentTermCycleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OunDeDeApplicationScheduledJob {

    private static final String CAMBODIA_ZONE_ID = "Asia/Phnom_Penh";

    private final LoanInstallmentRepository loanInstallmentRepository;

    private final PaymentTermCycleRepository paymentTermCycleRepository;


//    DEMO 10S CRON:
//    @Scheduled(cron = "*/10 * * * * *")

    /*
     * run at: 00:00, 04:00, 08:00, 12:00, 16:00, 20:00
     */
    @Scheduled(cron = "0 0 0/4 * * *", zone = CAMBODIA_ZONE_ID)
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

    /*
     * run at 03:30, 07:30, 11:30, 15:30, 19:30, 23:30 (gap 4h)
     */
    @Scheduled(cron = "0 30 3/4 * * *", zone = CAMBODIA_ZONE_ID)
    public void scheduleUpdatePaymentTermCycleStatus() {
        long now = System.currentTimeMillis() / 1000;
        log.info("Update payment term cycle status - {}", now);
        var overdueCycles = paymentTermCycleRepository.findAll(
          Specification.allOf(
              (root, query, cb) -> cb.lessThanOrEqualTo(root.get(PaymentTermCycle_.endDate), cambodiaNow()),
              (root, query, cb) -> cb.equal(root.get(PaymentTermCycle_.STATUS), PaymentTermCycleStatus.OPEN)
          )
        );
        if (overdueCycles.isEmpty()) {
            log.info("Skip update for empty overdue cycles");
            return;
        }
        log.info("Overdue cycles size = {}", overdueCycles.size());
        overdueCycles.forEach(
            cycle -> {
                if (cycle.getTotalPaidAmount().equals(cycle.getTotalAmount())) {
                    cycle.setStatus(PaymentTermCycleStatus.CLOSED);
                } else {
                    cycle.setStatus(PaymentTermCycleStatus.OVERDUE);
                }
            }
        );
        var updated = paymentTermCycleRepository.saveAll(overdueCycles);
        log.info("Overdue cycles updated, affected row = {}", updated.size());
    }
}
