CREATE TABLE IF NOT EXISTS auction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255),
    start_price DOUBLE,
    current_price DOUBLE,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    active BOOLEAN
);
