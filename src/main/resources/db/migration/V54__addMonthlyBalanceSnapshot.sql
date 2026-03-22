CREATE TABLE monthly_balance
(
    id              VARCHAR(255) NOT NULL,
    org_id          VARCHAR(20)  NOT NULL,
    `year_month`    VARCHAR(255) NOT NULL,
    closing_balance DECIMAL NULL,
    created_at      datetime NULL,
    CONSTRAINT pk_monthlybalance PRIMARY KEY (id)
);

CREATE INDEX org_index ON monthly_balance (org_id);