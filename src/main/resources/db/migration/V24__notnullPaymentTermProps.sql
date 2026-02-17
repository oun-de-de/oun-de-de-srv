ALTER TABLE payment_term
    MODIFY duration INT NOT NULL;

ALTER TABLE payment_term
    MODIFY start_date datetime NOT NULL;