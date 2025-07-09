INSERT INTO auction (product_name, start_price, current_price, start_time, end_time, active)
VALUES ('PlayStation 6', 500, 500, NOW(), TIMESTAMPADD('MINUTE', 2, NOW()), true);

INSERT INTO auction (product_name, start_price, current_price, start_time, end_time, active)
VALUES ('iPhone 20', 1000, 1000, NOW(), TIMESTAMPADD('MINUTE', 2, NOW()), true);
