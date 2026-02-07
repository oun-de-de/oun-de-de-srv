CREATE TABLE unit
(
    id     VARCHAR(255) NOT NULL,
    name   VARCHAR(255) NULL,
    descr  VARCHAR(1000) NULL,
    org_id VARCHAR(20)  NOT NULL,
    type   SMALLINT NULL,
    CONSTRAINT pk_unit PRIMARY KEY (id)
);

CREATE INDEX org_index ON unit (org_id);