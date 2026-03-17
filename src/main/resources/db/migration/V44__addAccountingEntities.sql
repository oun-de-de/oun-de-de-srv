CREATE TABLE account_type
(
    id     VARCHAR(255) NOT NULL,
    name   VARCHAR(255) NULL,
    descr  VARCHAR(1000) NULL,
    org_id VARCHAR(20)  NOT NULL,
    code   VARCHAR(20)  NOT NULL,
    nature SMALLINT     NOT NULL,
    CONSTRAINT pk_accounttype PRIMARY KEY (id)
);

CREATE TABLE chart_of_account
(
    id              VARCHAR(255) NOT NULL,
    name            VARCHAR(255) NULL,
    descr           VARCHAR(1000) NULL,
    org_id          VARCHAR(20)  NOT NULL,
    code            VARCHAR(20)  NOT NULL,
    account_type_id VARCHAR(255) NOT NULL,
    CONSTRAINT pk_chartofaccount PRIMARY KEY (id)
);

CREATE TABLE journal_class
(
    id     VARCHAR(255) NOT NULL,
    name   VARCHAR(255) NULL,
    descr  VARCHAR(1000) NULL,
    org_id VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_journalclass PRIMARY KEY (id)
);

CREATE TABLE journal_type
(
    id     VARCHAR(255) NOT NULL,
    name   VARCHAR(255) NULL,
    descr  VARCHAR(1000) NULL,
    org_id VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_journaltype PRIMARY KEY (id)
);

CREATE INDEX org_index ON account_type (org_id);

CREATE INDEX org_index ON chart_of_account (org_id);

CREATE INDEX org_index ON journal_class (org_id);

CREATE INDEX org_index ON journal_type (org_id);

ALTER TABLE chart_of_account
    ADD CONSTRAINT FK_CHARTOFACCOUNT_ON_ACCOUNTTYPE FOREIGN KEY (account_type_id) REFERENCES account_type (id);