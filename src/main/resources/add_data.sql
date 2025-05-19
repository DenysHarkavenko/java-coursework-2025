TRUNCATE TABLE notification RESTART IDENTITY CASCADE;
TRUNCATE TABLE service_request RESTART IDENTITY CASCADE;
TRUNCATE TABLE appointment_slot RESTART IDENTITY CASCADE;
TRUNCATE TABLE feedback RESTART IDENTITY CASCADE;
TRUNCATE TABLE service_center_service RESTART IDENTITY CASCADE;
TRUNCATE TABLE service_type RESTART IDENTITY CASCADE;
TRUNCATE TABLE service_center RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

INSERT INTO users (user_id, username, password, email, phone, role, created_at)
VALUES
    ('f57da07e-4331-4dd3-9bd9-458382d81070', 'admin_user', '$2a$12$8UHi43ukhf.3jPeM4y5byexS2ybIaLzRKyP4SfiP8Ps5fbCJ/d2e6', 'admin@example.com', '+1234567890', 'ADMIN', NOW()),
    ('f57da07e-4331-4dd3-9bd9-458382d81071', 'regular_user', '$2a$12$8UHi43ukhf.3jPeM4y5byexS2ybIaLzRKyP4SfiP8Ps5fbCJ/d2e6', 'user@example.com', '+0987654321', 'USER', NOW());

INSERT INTO service_center (center_id, name, rating, street, city, region, country, description)
VALUES
    ('f57da07e-4331-4dd3-9bd9-458382d81072', 'AutoService Plus', 4.5, '123 Main St', 'Kyiv', 'Kyiv Oblast', 'Ukraine', 'Premium car service'),
    ('f57da07e-4331-4dd3-9bd9-458382d81073', 'QuickFix Garage', 3.8, '456 Oak Ave', 'Lviv', 'Lviv Oblast', 'Ukraine', 'Fast and reliable repairs'),
    ('f57da07e-4331-4dd3-9bd9-458382d81074', 'MegaService', 4.2, '789 Pine Rd', 'Odesa', 'Odesa Oblast', 'Ukraine', 'Full range of services');

INSERT INTO service_type (service_id, name, description, category)
VALUES
    ('f57da07e-4331-4dd3-9bd9-458382d81075', 'Oil Change', 'Engine oil and filter replacement', 'MAINTENANCE'),
    ('f57da07e-4331-4dd3-9bd9-458382d81076', 'Brake Repair', 'Brake pads and discs replacement', 'REPAIR'),
    ('f57da07e-4331-4dd3-9bd9-458382d81077', 'Diagnostics', 'Full vehicle diagnostics', 'DIAGNOSTICS');

INSERT INTO service_center_service (id, center_id, service_id, price, duration)
VALUES
    ('f57da07e-4331-4dd3-9bd9-458382d81078', 'f57da07e-4331-4dd3-9bd9-458382d81072', 'f57da07e-4331-4dd3-9bd9-458382d81075', 50.00, 30),
    ('f57da07e-4331-4dd3-9bd9-458382d81079', 'f57da07e-4331-4dd3-9bd9-458382d81072', 'f57da07e-4331-4dd3-9bd9-458382d81076', 120.00, 60),
    ('f57da07e-4331-4dd3-9bd9-458382d8107a', 'f57da07e-4331-4dd3-9bd9-458382d81073', 'f57da07e-4331-4dd3-9bd9-458382d81075', 45.00, 30);

INSERT INTO feedback (feedback_id, user_id, center_id, rating, comment, created_at)
VALUES
    ('f57da07e-4331-4dd3-9bd9-458382d8107b', 'f57da07e-4331-4dd3-9bd9-458382d81071', 'f57da07e-4331-4dd3-9bd9-458382d81072', 5, 'Great service!', NOW()),
    ('f57da07e-4331-4dd3-9bd9-458382d8107c', 'f57da07e-4331-4dd3-9bd9-458382d81071', 'f57da07e-4331-4dd3-9bd9-458382d81073', 4, 'Good but a bit slow', NOW());

INSERT INTO appointment_slot (slot_id, center_id, date_time, is_available)
VALUES
    ('f57da07e-4331-4dd3-9bd9-458382d8107d', 'f57da07e-4331-4dd3-9bd9-458382d81072', NOW() + INTERVAL '1 day', true),
    ('f57da07e-4331-4dd3-9bd9-458382d8107e', 'f57da07e-4331-4dd3-9bd9-458382d81072', NOW() + INTERVAL '2 days', true),
    ('f57da07e-4331-4dd3-9bd9-458382d8107f', 'f57da07e-4331-4dd3-9bd9-458382d81073', NOW() + INTERVAL '1 day', false);

INSERT INTO service_request (request_id, user_id, center_id, service_type, request_date, status, slot_id)
VALUES
    ('f57da07e-4331-4dd3-9bd9-458382d81080', 'f57da07e-4331-4dd3-9bd9-458382d81071', 'f57da07e-4331-4dd3-9bd9-458382d81072', 'Oil Change', NOW(), 'CONFIRMED', 'f57da07e-4331-4dd3-9bd9-458382d8107d'),
    ('f57da07e-4331-4dd3-9bd9-458382d81081', 'f57da07e-4331-4dd3-9bd9-458382d81071', 'f57da07e-4331-4dd3-9bd9-458382d81073', 'Brake Repair', NOW(), 'PENDING', 'f57da07e-4331-4dd3-9bd9-458382d8107e');

INSERT INTO notification (notification_id, user_id, message, created_at)
VALUES
    ('f57da07e-4331-4dd3-9bd9-458382d81082', 'f57da07e-4331-4dd3-9bd9-458382d81071', 'Your appointment is confirmed for tomorrow', NOW()),
    ('f57da07e-4331-4dd3-9bd9-458382d81083', 'f57da07e-4331-4dd3-9bd9-458382d81070', 'New service request received', NOW());
