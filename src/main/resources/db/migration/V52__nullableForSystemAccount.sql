ALTER TABLE cash_transaction
    ADD reason SMALLINT NULL;

ALTER TABLE cash_transaction_detail
    MODIFY chart_of_account_id VARCHAR (255) NULL;

ALTER TABLE cash_transaction
    MODIFY employee_id VARCHAR (255) NULL;