ALTER TABLE account_type
DROP
COLUMN nature;

ALTER TABLE account_type
    ADD nature VARCHAR(255) NOT NULL;