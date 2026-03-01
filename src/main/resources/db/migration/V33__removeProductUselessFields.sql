ALTER TABLE product
DROP
COLUMN cost;

ALTER TABLE product
DROP
COLUMN price;

ALTER TABLE product
DROP
COLUMN quantity;

ALTER TABLE product
    MODIFY date datetime NOT NULL;

ALTER TABLE product
    MODIFY ref_no VARCHAR (255) NOT NULL;