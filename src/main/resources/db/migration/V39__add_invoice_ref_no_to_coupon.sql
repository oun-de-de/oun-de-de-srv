ALTER TABLE coupon
    ADD invoice_ref_no VARCHAR(255) NULL;

ALTER TABLE invoice
    MODIFY cycle_id VARCHAR (255) NOT NULL;