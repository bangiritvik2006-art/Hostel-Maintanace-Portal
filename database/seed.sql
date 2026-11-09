USE hostelfix;

INSERT INTO staff_members (name, department, phone, active)
SELECT 'Ravi Kumar', 'Electrical', '9000000001', TRUE
WHERE NOT EXISTS (SELECT 1 FROM staff_members WHERE name='Ravi Kumar');

INSERT INTO staff_members (name, department, phone, active)
SELECT 'Meena Devi', 'Plumbing', '9000000002', TRUE
WHERE NOT EXISTS (SELECT 1 FROM staff_members WHERE name='Meena Devi');

INSERT INTO staff_members (name, department, phone, active)
SELECT 'Arun Raj', 'Carpentry', '9000000003', TRUE
WHERE NOT EXISTS (SELECT 1 FROM staff_members WHERE name='Arun Raj');

INSERT INTO maintenance_requests
(ticket_number, reporter_name, room_number, category, priority, description, status, created_at)
SELECT 'MR-1001', 'Karthik', 'A-203', 'Electrical', 'HIGH',
       'Ceiling fan is making unusual noise.', 'ASSIGNED', NOW() - INTERVAL 3 DAY
WHERE NOT EXISTS (SELECT 1 FROM maintenance_requests WHERE ticket_number='MR-1001');

INSERT INTO maintenance_requests
(ticket_number, reporter_name, room_number, category, priority, description, status, created_at)
SELECT 'MR-1002', 'Rahul Verma', 'C-116', 'Plumbing', 'EMERGENCY',
       'Water is leaking continuously from the bathroom tap.', 'IN_PROGRESS', NOW() - INTERVAL 1 DAY
WHERE NOT EXISTS (SELECT 1 FROM maintenance_requests WHERE ticket_number='MR-1002');

INSERT INTO maintenance_requests
(ticket_number, reporter_name, room_number, category, priority, description, status, created_at)
SELECT 'MR-1003', 'Isha Gupta', 'B-307', 'Carpentry', 'LOW',
       'Study table drawer handle is loose.', 'RESOLVED', NOW() - INTERVAL 2 DAY
WHERE NOT EXISTS (SELECT 1 FROM maintenance_requests WHERE ticket_number='MR-1003');

INSERT INTO maintenance_requests
(ticket_number, reporter_name, room_number, category, priority, description, status, created_at)
SELECT 'MR-1004', 'siddu', '610', 'Cleaning', 'HIGH',
       'Some stains on the floor.', 'IN_PROGRESS', NOW()
WHERE NOT EXISTS (SELECT 1 FROM maintenance_requests WHERE ticket_number='MR-1004');
