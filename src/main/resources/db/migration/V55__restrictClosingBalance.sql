ALTER TABLE monthly_balance
    MODIFY closing_balance DECIMAL (19, 5);

ALTER TABLE monthly_balance
    MODIFY closing_balance DECIMAL (19, 5) NOT NULL;