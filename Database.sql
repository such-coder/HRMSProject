CREATE DATABASE IF NOT EXISTS hrms_db;
USE hrms_db;

-- ROLE TABLE
CREATE TABLE ROLE_MASTER (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    role_description VARCHAR(200)
);

-- USER TABLE
CREATE TABLE USER_MASTER (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    emp_code VARCHAR(20) NOT NULL UNIQUE,
    emp_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    designation VARCHAR(100),
    department VARCHAR(100),
    role_id INT NOT NULL,
    location VARCHAR(100),
    mobile_no VARCHAR(15),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_on DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (role_id) REFERENCES ROLE_MASTER(role_id)
);

-- HARDWARE TABLE
CREATE TABLE HARDWARE_MASTER (
    hardware_id INT PRIMARY KEY AUTO_INCREMENT,
    hardware_name VARCHAR(100) NOT NULL,
    hardware_category VARCHAR(50),
    specification VARCHAR(200),
    active_flag VARCHAR(1) DEFAULT 'Y'
);

-- REQUEST TABLE
CREATE TABLE REQUEST_MASTER (
    request_id INT PRIMARY KEY AUTO_INCREMENT,
    request_no VARCHAR(20) NOT NULL UNIQUE,
    requested_by INT NOT NULL,
    hardware_id INT NOT NULL,
    quantity INT NOT NULL,
    justification VARCHAR(500) NOT NULL,
    priority VARCHAR(10) DEFAULT 'MEDIUM',
    required_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'DRAFT',
    attachment_path VARCHAR(300),
    created_on DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_on DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    approved_by VARCHAR(100),
    approved_date DATE,
    FOREIGN KEY (requested_by) REFERENCES USER_MASTER(user_id),
    FOREIGN KEY (hardware_id) REFERENCES HARDWARE_MASTER(hardware_id)
);

-- APPROVAL HISTORY
CREATE TABLE APPROVAL_HISTORY (
    history_id INT PRIMARY KEY AUTO_INCREMENT,
    request_id INT NOT NULL,
    action_by INT NOT NULL,
    action_type VARCHAR(20) NOT NULL,
    remarks VARCHAR(500),
    action_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (request_id) REFERENCES REQUEST_MASTER(request_id),
    FOREIGN KEY (action_by) REFERENCES USER_MASTER(user_id)
);

-- NOTIFICATION TABLE
CREATE TABLE NOTIFICATION_MASTER (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    request_id INT NOT NULL,
    notification_message VARCHAR(300) NOT NULL,
    notification_type VARCHAR(50),
    is_read VARCHAR(1) DEFAULT 'N',
    created_on DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES USER_MASTER(user_id),
    FOREIGN KEY (request_id) REFERENCES REQUEST_MASTER(request_id)
);

-- ASSET TABLE
CREATE TABLE ASSET_ALLOCATION (
    allocation_id INT PRIMARY KEY AUTO_INCREMENT,
    request_id INT NOT NULL UNIQUE,
    asset_tag VARCHAR(100) NOT NULL,
    serial_number VARCHAR(100) NOT NULL,
    allocated_by INT NOT NULL,
    allocation_date DATE DEFAULT (CURRENT_DATE),
    remarks VARCHAR(300),
    FOREIGN KEY (request_id) REFERENCES REQUEST_MASTER(request_id),
    FOREIGN KEY (allocated_by) REFERENCES USER_MASTER(user_id)
);

-- INSERT ROLE DATA
INSERT INTO ROLE_MASTER (role_name, role_description) VALUES
('EMPLOYEE', 'Regular employee who raises requests'),
('IS_GM', 'General Manager approval role'),
('PROCESSING_TEAM', 'IT team processing requests'),
('ADMIN', 'System administrator');

-- INSERT HARDWARE DATA
INSERT INTO HARDWARE_MASTER (hardware_name, hardware_category, specification) VALUES
('Laptop', 'Computing', 'Corporate Laptop'),
('Desktop PC', 'Computing', 'Desktop Workstation'),
('iPad/Tablet', 'Mobile', 'Corporate Tablet'),
('Pendrive', 'Storage', 'USB Storage Device'),
('External HDD', 'Storage', 'External Hard Disk'),
('VC Setup', 'Conference', 'Video Conferencing Kit'),
('Accessories', 'Peripherals', 'Keyboard/Mouse etc');

-- INSERT USERS
INSERT INTO USER_MASTER (emp_code, emp_name, email, password, designation, department, role_id, location, mobile_no) VALUES
('EMP001', 'Priya Sharma', 'priya@iocl.in', 'pass123', 'Officer', 'IT', 1, 'Mumbai', '9876543210'),
('GM001', 'Ramesh Verma', 'ramesh@iocl.in', 'pass123', 'GM', 'IT', 2, 'Delhi', '9876500001'),
('TL001', 'Suresh Kumar', 'suresh@iocl.in', 'pass123', 'TL', 'IT Processing', 3, 'Mumbai', '9876500002'),
('ADM001', 'Admin User', 'admin@iocl.in', 'admin123', 'Admin', 'IT', 4, 'HQ', '9876500003');