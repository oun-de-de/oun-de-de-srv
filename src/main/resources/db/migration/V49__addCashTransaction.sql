CREATE TABLE cash_transaction
(
    id          VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NULL,
    descr       VARCHAR(1000) NULL,
    org_id      VARCHAR(20)  NOT NULL,
    ref_no      VARCHAR(255) NOT NULL,
    type        SMALLINT     NOT NULL,
    date        datetime NULL,
    currency_id VARCHAR(255) NULL,
    employee_id VARCHAR(255) NOT NULL,
    memo        VARCHAR(255) NULL,
    CONSTRAINT pk_cashtransaction PRIMARY KEY (id)
);

CREATE TABLE cash_transaction_detail
(
    id                  VARCHAR(255)   NOT NULL,
    name                VARCHAR(255) NULL,
    descr               VARCHAR(1000) NULL,
    org_id              VARCHAR(20)    NOT NULL,
    cash_transaction_id VARCHAR(255)   NOT NULL,
    chart_of_account_id VARCHAR(255) NULL,
    account_type_id     VARCHAR(255) NULL,
    memo                VARCHAR(255) NULL,
    amount              DECIMAL(19, 5) NOT NULL,
    customer_id         VARCHAR(255)   NOT NULL,
    journal_class_id    VARCHAR(255) NULL,
    CONSTRAINT pk_cashtransactiondetail PRIMARY KEY (id)
);

CREATE INDEX org_index ON cash_transaction (org_id);

CREATE INDEX org_index ON cash_transaction_detail (org_id);

ALTER TABLE cash_transaction_detail
    ADD CONSTRAINT FK_CASHTRANSACTIONDETAIL_ON_ACCOUNTTYPE FOREIGN KEY (account_type_id) REFERENCES account_type (id);

ALTER TABLE cash_transaction_detail
    ADD CONSTRAINT FK_CASHTRANSACTIONDETAIL_ON_CASHTRANSACTION FOREIGN KEY (cash_transaction_id) REFERENCES cash_transaction (id);

ALTER TABLE cash_transaction_detail
    ADD CONSTRAINT FK_CASHTRANSACTIONDETAIL_ON_CHARTOFACCOUNT FOREIGN KEY (chart_of_account_id) REFERENCES chart_of_account (id);

ALTER TABLE cash_transaction_detail
    ADD CONSTRAINT FK_CASHTRANSACTIONDETAIL_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE cash_transaction_detail
    ADD CONSTRAINT FK_CASHTRANSACTIONDETAIL_ON_JOURNALCLASS FOREIGN KEY (journal_class_id) REFERENCES journal_class (id);

ALTER TABLE cash_transaction
    ADD CONSTRAINT FK_CASHTRANSACTION_ON_CURRENCY FOREIGN KEY (currency_id) REFERENCES currency (id);

ALTER TABLE cash_transaction
    ADD CONSTRAINT FK_CASHTRANSACTION_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES user (id);