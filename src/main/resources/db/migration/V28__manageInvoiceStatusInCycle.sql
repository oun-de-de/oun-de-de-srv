ALTER TABLE payment_term_cycle
    ADD status SMALLINT NULL;

ALTER TABLE payment_term_cycle
    MODIFY status SMALLINT NOT NULL;

ALTER TABLE invoice
DROP
COLUMN status;

ALTER TABLE loan_installment
    MODIFY amount DECIMAL (19, 5) NOT NULL;

ALTER TABLE loan
    MODIFY principal_amount DECIMAL (19, 5) NOT NULL;

ALTER TABLE equipment_borrow
    MODIFY quantity DECIMAL (19, 5) NOT NULL;

ALTER TABLE stock_transaction
    MODIFY quantity DECIMAL (19, 5) NOT NULL;

ALTER TABLE inventory_item
    MODIFY quantity_on_hand DECIMAL (19, 5) NOT NULL;