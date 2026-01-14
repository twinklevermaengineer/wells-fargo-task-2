USE wells_fargo;

CREATE TABLE portfolio (
portfolio_id BIGINT AUTO_INCREMENT PRIMARY KEY,
creation_date VARCHAR(100) NOT NULL,
 client_id BIGINT NOT NULL,
    CONSTRAINT fk_portfolio_client
        FOREIGN KEY (client_id)
        REFERENCES client(client_id)
);
