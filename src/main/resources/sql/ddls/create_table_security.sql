USE wells_fargo;

CREATE TABLE security (
security_id BIGINT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100) NOT NULL,
category VARCHAR(100) NOT NULL,
purchase_price DECIMAL(10, 2) NOT NULL,
purchase_date VARCHAR(100) NOT NULL,
quantity INT NOT NULL,
portfolio_id BIGINT NOT NULL,
CONSTRAINT fk_security_portfolio
	FOREIGN KEY (portfolio_id)
    REFERENCES portfolio(portfolio_id)
);
