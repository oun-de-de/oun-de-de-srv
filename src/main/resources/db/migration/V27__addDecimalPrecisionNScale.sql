ALTER TABLE inventory_item
    MODIFY alert_threshold DECIMAL (19, 5);

ALTER TABLE loan_installment
    MODIFY amount DECIMAL (19, 5);

ALTER TABLE product
    MODIFY cost DECIMAL (19, 5);

ALTER TABLE default_product_setting
    MODIFY price DECIMAL (19, 5);

ALTER TABLE product
    MODIFY price DECIMAL (19, 5);

ALTER TABLE product_setting
    MODIFY price DECIMAL (19, 5);

ALTER TABLE weight_record
    MODIFY price_per_product DECIMAL (19, 5);

ALTER TABLE loan
    MODIFY principal_amount DECIMAL (19, 5);

ALTER TABLE default_product_setting
    MODIFY quantity DECIMAL (19, 5);

ALTER TABLE equipment_borrow
    MODIFY quantity DECIMAL (19, 5);

ALTER TABLE product
    MODIFY quantity DECIMAL (19, 5);

ALTER TABLE product_setting
    MODIFY quantity DECIMAL (19, 5);

ALTER TABLE stock_transaction
    MODIFY quantity DECIMAL (19, 5);

ALTER TABLE weight_record
    MODIFY quantity DECIMAL (19, 5);

ALTER TABLE inventory_item
    MODIFY quantity_on_hand DECIMAL (19, 5);

ALTER TABLE weight_record
    MODIFY quantity_per_product DECIMAL (19, 5);

ALTER TABLE weight_record
    MODIFY weight DECIMAL (19, 5);