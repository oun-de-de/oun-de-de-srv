package com.cdtphuhoi.oun_de_de.jobs;

import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import com.cdtphuhoi.oun_de_de.common.LoanStatus;
import com.cdtphuhoi.oun_de_de.common.PaymentTermCycleStatus;
import com.cdtphuhoi.oun_de_de.entities.Loan_;
import com.cdtphuhoi.oun_de_de.entities.PaymentTermCycle_;
import com.cdtphuhoi.oun_de_de.repositories.LoanRepository;
import com.cdtphuhoi.oun_de_de.repositories.PaymentTermCycleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
public class OunDeDeApplicationScheduledJob {

    private static final String CAMBODIA_ZONE_ID = "Asia/Phnom_Penh";

    private final PaymentTermCycleRepository paymentTermCycleRepository;

    private final LoanRepository loanRepository;


//    DEMO 10S CRON:
//    @Scheduled(cron = "*/10 * * * * *")

    /*
     * run at: 00:00, 04:00, 08:00, 12:00, 16:00, 20:00
     */
    @Scheduled(cron = "0 0 0/4 * * *", zone = CAMBODIA_ZONE_ID)
    public void scheduleUpdateLoanInstallmentStatus() {
        long now = System.currentTimeMillis() / 1000;
        log.info("Update loan status - {}", now);
        /*
         * TODO: UPDATE LOAN STATUS TOO
         */
        var loans = loanRepository.findAll(
            Specification.allOf(
                (root, query, cb) -> cb.equal(root.get(Loan_.STATUS), LoanStatus.NORMAL)
            )
        );

        if (loans.isEmpty()) {
            log.info("Empty loans, no need to update");
            return;
        }
        log.info("Due loans size = {}", loans.size());
        var shouldUpdate = new AtomicBoolean(false);
        loans.forEach(loan -> {
            var isDue = loan.getDueDate()
                .minusDays(loan.getDueWarningDays())
                .isBefore(cambodiaNow());
            if (isDue) {
                shouldUpdate.set(true);
                loan.setStatus(LoanStatus.DUE);
            }
        });
        if (shouldUpdate.get()) {
            var updated = loanRepository.saveAll(loans);
            log.info("Due loans updated, affected row = {}", updated.size());
        } else {
            log.info("Empty due loans, no need to update");
        }
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
