package com.cdtphuhoi.oun_de_de.services.cash_transaction;

import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.cash_transaction.dto.CashTransactionResult;
import com.cdtphuhoi.oun_de_de.services.cash_transaction.dto.CreateCashTransactionData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CashTransactionService implements OrgManagementService {

    public CashTransactionResult create(CreateCashTransactionData createCashTransactionData) {
        return null;
    }
}
