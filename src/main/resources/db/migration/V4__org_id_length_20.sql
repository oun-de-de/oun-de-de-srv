ALTER TABLE contact
    MODIFY org_id VARCHAR (20);

ALTER TABLE coupon
    MODIFY org_id VARCHAR (20);

ALTER TABLE customer
    MODIFY org_id VARCHAR (20);

ALTER TABLE product
    MODIFY org_id VARCHAR (20);

ALTER TABLE user
    MODIFY org_id VARCHAR (20);

ALTER TABLE vehicle
    MODIFY org_id VARCHAR (20);

ALTER TABLE weight_record
    MODIFY org_id VARCHAR (20);