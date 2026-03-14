ALTER TABLE loan_installment
DROP
FOREIGN KEY FK_LOANINSTALLMENT_ON_LOAN;

CREATE TABLE loan_payment
(
    id           VARCHAR(255)   NOT NULL,
    org_id       VARCHAR(20)    NOT NULL,
    loan_id      VARCHAR(255)   NOT NULL,
    payment_date datetime       NOT NULL,
    amount       DECIMAL(19, 5) NOT NULL,
    CONSTRAINT pk_loanpayment PRIMARY KEY (id)
);

ALTER TABLE loan
    ADD due_date datetime NULL;

ALTER TABLE loan
    ADD due_warning_days INT NULL;

ALTER TABLE loan
    ADD installment_amount DECIMAL(19, 5) NULL;

ALTER TABLE loan
    ADD paid_amount DECIMAL(19, 5) NULL;

ALTER TABLE loan
    ADD status SMALLINT NULL;

ALTER TABLE loan
    MODIFY due_date datetime NOT NULL;

ALTER TABLE loan
    MODIFY due_warning_days INT NOT NULL;

ALTER TABLE loan
    MODIFY installment_amount DECIMAL (19, 5) NOT NULL;

ALTER TABLE loan
    MODIFY paid_amount DECIMAL (19, 5) NOT NULL;

ALTER TABLE loan
    MODIFY status SMALLINT NOT NULL;

CREATE INDEX org_index ON loan_payment (org_id);

ALTER TABLE loan_payment
    ADD CONSTRAINT FK_LOANPAYMENT_ON_LOAN FOREIGN KEY (loan_id) REFERENCES loan (id);

DROP TABLE loan_installment;

ALTER TABLE loan
DROP
COLUMN term_months;