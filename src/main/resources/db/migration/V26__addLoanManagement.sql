CREATE TABLE loan
(
    id               VARCHAR(255) NOT NULL,
    org_id           VARCHAR(20)  NOT NULL,
    borrower_type    SMALLINT     NOT NULL,
    borrower_id      VARCHAR(36)  NOT NULL,
    principal_amount DECIMAL      NOT NULL,
    term_months      INT          NOT NULL,
    start_date       datetime     NOT NULL,
    create_at        datetime     NOT NULL,
    CONSTRAINT pk_loan PRIMARY KEY (id)
);

CREATE TABLE loan_installment
(
    id          VARCHAR(255) NOT NULL,
    org_id      VARCHAR(20)  NOT NULL,
    loan_id     VARCHAR(255) NOT NULL,
    month_index INT          NOT NULL,
    due_date    datetime     NOT NULL,
    amount      DECIMAL      NOT NULL,
    status      SMALLINT     NOT NULL,
    paid_at     datetime NULL,
    CONSTRAINT pk_loaninstallment PRIMARY KEY (id)
);

CREATE INDEX org_index ON loan_installment (org_id);

CREATE INDEX org_index ON loan (org_id);

ALTER TABLE loan_installment
    ADD CONSTRAINT FK_LOANINSTALLMENT_ON_LOAN FOREIGN KEY (loan_id) REFERENCES loan (id);