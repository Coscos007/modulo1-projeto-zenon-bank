CREATE TABLE TRANSACTIONS (
    step 			    INT NOT NULL,
    type 			    ENUM('CASH_IN','CASH_OUT','DEBIT','PAYMENT','TRANSFER') NOT NULL,
    amount 			    DECIMAL(13, 2) NOT NULL,
    name_orig 		    VARCHAR(255) NOT NULL,
    old_balance_orig    DECIMAL(13, 2) NOT NULL,
    new_balance_orig 	DECIMAL(13, 2) NOT NULL,
    name_dest 		    VARCHAR(255) NOT NULL,
    old_balance_dest 	DECIMAL(13, 2) NOT NULL,
    new_balance_dest 	DECIMAL(13, 2) NOT NULL,
    is_fraud 		    BOOLEAN NOT NULL,
    is_flagged_fraud 	BOOLEAN NOT NULL
);