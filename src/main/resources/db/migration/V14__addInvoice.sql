CREATE TABLE invoice
(
    id        VARCHAR(255) NOT NULL,
    org_id    VARCHAR(20)  NOT NULL,
    ref_no    VARCHAR(255) NULL,
    date      datetime NULL,
    coupon_id VARCHAR(255) NOT NULL,
    CONSTRAINT pk_invoice PRIMARY KEY (id)
);

CREATE INDEX org_index ON invoice (org_id);

ALTER TABLE invoice
    ADD CONSTRAINT FK_INVOICE_ON_COUPON FOREIGN KEY (coupon_id) REFERENCES coupon (id);