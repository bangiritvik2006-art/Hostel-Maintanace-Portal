CREATE DATABASE IF NOT EXISTS hostelfix;
USE hostelfix;

CREATE TABLE IF NOT EXISTS staff_members (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  department VARCHAR(80) NOT NULL,
  phone VARCHAR(20),
  active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS maintenance_requests (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  ticket_number VARCHAR(20) NOT NULL UNIQUE,
  reporter_name VARCHAR(100) NOT NULL,
  room_number VARCHAR(30) NOT NULL,
  category VARCHAR(40) NOT NULL,
  priority VARCHAR(20) NOT NULL,
  description TEXT NOT NULL,
  status VARCHAR(25) NOT NULL DEFAULT 'OPEN',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  resolved_at DATETIME NULL,
  staff_id BIGINT NULL,
  CONSTRAINT fk_request_staff FOREIGN KEY (staff_id) REFERENCES staff_members(id)
);

CREATE INDEX idx_request_status ON maintenance_requests(status);
CREATE INDEX idx_request_room ON maintenance_requests(room_number);
