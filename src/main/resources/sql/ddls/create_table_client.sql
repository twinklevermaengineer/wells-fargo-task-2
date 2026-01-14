USE wells_fargo;

CREATE TABLE client (
client_id BIGINT AUTO_INCREMENT PRIMARY KEY,
first_name VARCHAR(100) NOT NULL,
last_name VARCHAR(100) NOT NULL,
address VARCHAR(250) NOT NULL,
phone VARCHAR(100) NOT NULL,
email VARCHAR(100) NOT NULL UNIQUE,
advisor_id BIGINT NOT NULL,
    CONSTRAINT fk_client_advisor
        FOREIGN KEY (advisor_id)
        REFERENCES advisor(advisor_id)
);
