package com.cdtphuhoi.oun_de_de.jobs;

import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import static com.cdtphuhoi.oun_de_de.utils.Utils.endOfDayInCambodia;
import static com.cdtphuhoi.oun_de_de.utils.Utils.startOfDayInCambodia;
import com.cdtphuhoi.oun_de_de.common.CashTransactionType;
import com.cdtphuhoi.oun_de_de.common.LoanStatus;
import com.cdtphuhoi.oun_de_de.common.PaymentTermCycleStatus;
import com.cdtphuhoi.oun_de_de.entities.CashTransactionDetail;
import com.cdtphuhoi.oun_de_de.entities.CashTransactionDetail_;
import com.cdtphuhoi.oun_de_de.entities.CashTransaction_;
import com.cdtphuhoi.oun_de_de.entities.Loan_;
import com.cdtphuhoi.oun_de_de.entities.MonthlyBalance;
import com.cdtphuhoi.oun_de_de.entities.PaymentTermCycle_;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.entities.User_;
import com.cdtphuhoi.oun_de_de.jobs.dto.MonthlyCashTransactionForBalance;
import com.cdtphuhoi.oun_de_de.repositories.LoanRepository;
import com.cdtphuhoi.oun_de_de.repositories.MonthlyBalanceRepository;
import com.cdtphuhoi.oun_de_de.repositories.PaymentTermCycleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.JoinType;

@Slf4j
@Component
@RequiredArgsConstructor
public class OunDeDeApplicationScheduledJob {

    private static final String CAMBODIA_ZONE_ID = "Asia/Phnom_Penh";

    private final PaymentTermCycleRepository paymentTermCycleRepository;

    private final LoanRepository loanRepository;

    private final EntityManager entityManager;

    private final MonthlyBalanceRepository monthlyBalanceRepository;

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

    /*
     * run at 02:00 06:00 10:00 14:00 18:00 22:00 (gap 4h)
     */
    @Scheduled(cron = "0 0 2/4 1 * *", zone = CAMBODIA_ZONE_ID)
    public void calculateMonthlyClosingBalance() {
        long now = System.currentTimeMillis() / 1000;
        log.info("Calculate monthly report - {}", now);

        var orgIds = getOrgIds();
        log.info("OrgIds size = {}", orgIds.size());

        var monthlyBalances = new ArrayList<MonthlyBalance>();

        orgIds.forEach(orgId -> {
            var today = cambodiaNow();
            var previousMonth = YearMonth.from(today).minusMonths(1);
            var previousMonthBalanceOpt = monthlyBalanceRepository.findByPeriodAndOrgId(previousMonth.toString(), orgId);
            if (previousMonthBalanceOpt.isPresent()) {
                log.info("Monthly balance for orgId {} and month {} already exists, skip calculation", orgId, previousMonth);
                return;
            }
            var previous2Month = previousMonth.minusMonths(1);
            var previous2MonthClosingBalance = monthlyBalanceRepository.findByPeriodAndOrgId(previous2Month.toString(), orgId)
                .map(MonthlyBalance::getClosingBalance)
                .orElse(BigDecimal.ZERO);
            var previousClosingBalance = calculateClosingBalance(orgId, previousMonth);
            var previousMonthBalance = MonthlyBalance.builder()
                .orgId(orgId)
                .period(previousMonth.toString())
                .closingBalance(previous2MonthClosingBalance.add(previousClosingBalance))
                .createdAt(today)
                .build();
            log.info("Save monthly balance for orgId {} and month {}, previous month balance = {}",
                orgId, previousMonth, previous2MonthClosingBalance);
            monthlyBalances.add(previousMonthBalance);
        });

        if (!monthlyBalances.isEmpty()) {
            log.info("Saving monthly balance for {} orgs, month = {}",
                monthlyBalances.size(), monthlyBalances.get(0).getPeriod());
            monthlyBalanceRepository.saveAll(monthlyBalances);
        }
    }

    private BigDecimal calculateClosingBalance(String orgId, YearMonth previousMonth) {
        var start = startOfDayInCambodia(previousMonth.atDay(1).atStartOfDay());
        var end = endOfDayInCambodia(previousMonth.atEndOfMonth().atTime(LocalTime.MAX));
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(MonthlyCashTransactionForBalance.class);
        var root = query.from(CashTransactionDetail.class);
        var cashTxJoin = root.join(CashTransactionDetail_.cashTransaction, JoinType.LEFT);
        query
            .select(
                cb.construct(
                    MonthlyCashTransactionForBalance.class,
                    cashTxJoin.get(CashTransaction_.TYPE),
                    root.get(CashTransactionDetail_.AMOUNT)
                )
            )
            .where(
                cb.between(
                    cashTxJoin.get(CashTransaction_.date),
                    start,
                    end
                ),
                cb.equal(
                    root.get(CashTransactionDetail_.ORG_ID),
                    orgId
                )
            );
        var monthlyCashTransactionForBalances = entityManager.createQuery(query).getResultList();
        return monthlyCashTransactionForBalances.stream()
            .reduce(
                BigDecimal.ZERO,
                (balance, tx) -> CashTransactionType.DEBIT.equals(tx.type())
                    ? balance.add(tx.amount())
                    : balance.subtract(tx.amount()),
                BigDecimal::add
            );
    }

    private List<String> getOrgIds() {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(String.class);
        var root = query.from(User.class);

        query
            .select(root.get(User_.ORG_ID))
            .distinct(true);

        return entityManager.createQuery(query).getResultList();
    }
}
