CREATE TABLE warehouse
(
    id     VARCHAR(255) NOT NULL,
    name   VARCHAR(255) NULL,
    descr  VARCHAR(1000) NULL,
    org_id VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_warehouse PRIMARY KEY (id)
);

CREATE INDEX org_index ON warehouse (org_id);