-- ============================================================
-- MASON HOTEL
-- 50 EMPLOYEE SEED
-- ============================================================

-- ============================================================
-- MANAGEMENT — EMP001 to EMP004
-- ============================================================

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('1 Oak Avenue', 'Cape Town', 'Western Cape', 'South Africa', '8001')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP001', 'Daniel', 'Molefe', 'daniel.molefe@masonhotel.co.za', '0827001001',
       id, '1980-04-12', 'MALE', 'General Manager', 'Management',
       72000.00, '2018-02-01', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('14 Forest Road', 'Cape Town', 'Western Cape', 'South Africa', '7700')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP002', 'Naledi', 'Mokoena', 'naledi.mokoena@masonhotel.co.za', '0827001002',
       id, '1985-09-24', 'FEMALE', 'Operations Manager', 'Management',
       58000.00, '2019-06-10', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('28 Pine Street', 'Cape Town', 'Western Cape', 'South Africa', '7780')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP003', 'Sipho', 'Dlamini', 'sipho.dlamini@masonhotel.co.za', '0827001003',
       id, '1987-01-15', 'MALE', 'Assistant General Manager', 'Management',
       48000.00, '2021-03-15', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('6 Church Street', 'Cape Town', 'Western Cape', 'South Africa', '8001')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP004', 'Ayesha', 'Naidoo', 'ayesha.naidoo@masonhotel.co.za', '0827001004',
       id, '1990-06-18', 'FEMALE', 'Hotel Manager', 'Management',
       52000.00, '2022-08-01', 'ACTIVE' FROM a;


-- ============================================================
-- FRONT DESK / GUEST SERVICES — EMP100 to EMP107
-- ============================================================

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('22 Long Street', 'Cape Town', 'Western Cape', 'South Africa', '8001')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP100', 'Thandi', 'Nkosi', 'thandi.nkosi@masonhotel.co.za', '0827001100',
       id, '1989-02-10', 'FEMALE', 'Front Desk Manager', 'Front Desk / Guest Services',
       38000.00, '2020-04-06', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('31 Main Road', 'Cape Town', 'Western Cape', 'South Africa', '7700')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP101', 'Lerato', 'Dlamini', 'lerato.dlamini@masonhotel.co.za', '0827001101',
       id, '1995-07-14', 'FEMALE', 'Front Desk Agent', 'Front Desk / Guest Services',
       22000.00, '2023-01-16', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('9 Victoria Road', 'Cape Town', 'Western Cape', 'South Africa', '8005')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP102', 'Kabelo', 'Molefe', 'kabelo.molefe@masonhotel.co.za', '0827001102',
       id, '1997-03-21', 'MALE', 'Front Desk Agent', 'Front Desk / Guest Services',
       22000.00, '2024-02-12', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('17 Kloof Street', 'Cape Town', 'Western Cape', 'South Africa', '8001')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP103', 'Zanele', 'Khumalo', 'zanele.khumalo@masonhotel.co.za', '0827001103',
       id, '1996-11-02', 'FEMALE', 'Front Desk Agent', 'Front Desk / Guest Services',
       22000.00, '2024-05-06', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('43 Church Street', 'Cape Town', 'Western Cape', 'South Africa', '8000')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP104', 'Ryan', 'Peters', 'ryan.peters@masonhotel.co.za', '0827001104',
       id, '1993-08-19', 'MALE', 'Concierge', 'Front Desk / Guest Services',
       24000.00, '2022-11-14', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('8 Adderley Street', 'Cape Town', 'Western Cape', 'South Africa', '8000')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP105', 'Jason', 'Williams', 'jason.williams@masonhotel.co.za', '0827001105',
       id, '1994-05-11', 'MALE', 'Reservations Agent', 'Front Desk / Guest Services',
       23000.00, '2023-04-17', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('12 Bree Street', 'Cape Town', 'Western Cape', 'South Africa', '8001')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP106', 'Nokuthula', 'Zwane', 'nokuthula.zwane@masonhotel.co.za', '0827001106',
       id, '1997-06-30', 'FEMALE', 'Guest Services Agent', 'Front Desk / Guest Services',
       21000.00, '2024-01-15', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('19 Loop Street', 'Cape Town', 'Western Cape', 'South Africa', '8001')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP107', 'Karabo', 'Mokoena', 'karabo.mokoena@masonhotel.co.za', '0827001107',
       id, '1998-10-11', 'FEMALE', 'Night Auditor', 'Front Desk / Guest Services',
       24000.00, '2024-04-08', 'ACTIVE' FROM a;


-- ============================================================
-- HOUSEKEEPING — EMP200 to EMP209
-- ============================================================

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('12 Station Road', 'Cape Town', 'Western Cape', 'South Africa', '7405')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP200', 'Nomsa', 'Mthembu', 'nomsa.mthembu@masonhotel.co.za', '0827001200',
       id, '1986-05-18', 'FEMALE', 'Executive Housekeeper', 'Housekeeping',
       36000.00, '2019-01-07', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('25 Voortrekker Road', 'Cape Town', 'Western Cape', 'South Africa', '7405')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP201', 'Busisiwe', 'Ndlovu', 'busisiwe.ndlovu@masonhotel.co.za', '0827001201',
       id, '1991-09-12', 'FEMALE', 'Housekeeping Supervisor', 'Housekeeping',
       27000.00, '2021-06-14', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('7 Albert Road', 'Cape Town', 'Western Cape', 'South Africa', '7925')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP202', 'Grace', 'Mokoena', 'grace.mokoena@masonhotel.co.za', '0827001202',
       id, '1994-04-25', 'FEMALE', 'Room Attendant', 'Housekeeping',
       19000.00, '2023-02-20', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('19 Imam Haron Road', 'Cape Town', 'Western Cape', 'South Africa', '7780')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP203', 'Patience', 'Sibanda', 'patience.sibanda@masonhotel.co.za', '0827001203',
       id, '1992-12-04', 'FEMALE', 'Room Attendant', 'Housekeeping',
       19000.00, '2023-07-03', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('35 Main Road', 'Cape Town', 'Western Cape', 'South Africa', '7945')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP204', 'Mpho', 'Maseko', 'mpho.maseko@masonhotel.co.za', '0827001204',
       id, '1995-02-16', 'MALE', 'Room Attendant', 'Housekeeping',
       19000.00, '2024-01-08', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('11 Voortrekker Road', 'Cape Town', 'Western Cape', 'South Africa', '7405')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP205', 'Lindiwe', 'Moyo', 'lindiwe.moyo@masonhotel.co.za', '0827001205',
       id, '1993-08-26', 'FEMALE', 'Room Attendant', 'Housekeeping',
       19000.00, '2024-02-12', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('4 Retreat Road', 'Cape Town', 'Western Cape', 'South Africa', '7945')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP206', 'Thando', 'Cele', 'thando.cele@masonhotel.co.za', '0827001206',
       id, '1990-04-10', 'MALE', 'Laundry Supervisor', 'Housekeeping',
       25000.00, '2022-06-06', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('16 Main Road', 'Cape Town', 'Western Cape', 'South Africa', '7700')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP207', 'Precious', 'Mahlangu', 'precious.mahlangu@masonhotel.co.za', '0827001207',
       id, '1995-01-28', 'FEMALE', 'Laundry Attendant', 'Housekeeping',
       18000.00, '2023-09-04', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('22 Belvedere Road', 'Cape Town', 'Western Cape', 'South Africa', '7700')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP208', 'Siyabonga', 'Zulu', 'siyabonga.zulu@masonhotel.co.za', '0827001208',
       id, '1991-11-18', 'MALE', 'Public Area Attendant', 'Housekeeping',
       19000.00, '2023-10-02', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('33 Main Road', 'Cape Town', 'Western Cape', 'South Africa', '7700')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP209', 'Nandi', 'Maseko', 'nandi.maseko@masonhotel.co.za', '0827001209',
       id, '1996-07-09', 'FEMALE', 'Room Attendant', 'Housekeeping',
       19000.00, '2024-03-18', 'ACTIVE' FROM a;


-- ============================================================
-- MAINTENANCE — EMP300 to EMP303
-- ============================================================

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('8 Salt River Road', 'Cape Town', 'Western Cape', 'South Africa', '7925')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP300', 'Sipho', 'Ndlovu', 'sipho.ndlovu@masonhotel.co.za', '0827001300',
       id, '1984-08-22', 'MALE', 'Maintenance Manager', 'Maintenance',
       40000.00, '2018-09-03', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('41 Voortrekker Road', 'Cape Town', 'Western Cape', 'South Africa', '7405')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP301', 'Mandla', 'Zulu', 'mandla.zulu@masonhotel.co.za', '0827001301',
       id, '1988-01-30', 'MALE', 'Electrician', 'Maintenance',
       28000.00, '2021-04-19', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('16 Retreat Road', 'Cape Town', 'Western Cape', 'South Africa', '7945')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP302', 'Thabo', 'Molefe', 'thabo.molefe@masonhotel.co.za', '0827001302',
       id, '1990-10-13', 'MALE', 'Maintenance Technician', 'Maintenance',
       25000.00, '2022-03-07', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('27 Main Road', 'Cape Town', 'Western Cape', 'South Africa', '7700')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP303', 'Sanele', 'Mkhize', 'sanele.mkhize@masonhotel.co.za', '0827001303',
       id, '1992-02-05', 'MALE', 'Plumber', 'Maintenance',
       26000.00, '2023-08-07', 'ACTIVE' FROM a;


-- ============================================================
-- SECURITY — EMP400 to EMP403
-- ============================================================

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('10 Military Road', 'Cape Town', 'Western Cape', 'South Africa', '8001')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP400', 'Mandla', 'Sithole', 'mandla.sithole@masonhotel.co.za', '0827001400',
       id, '1982-06-11', 'MALE', 'Security Manager', 'Security',
       35000.00, '2019-10-01', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('23 Klipfontein Road', 'Cape Town', 'Western Cape', 'South Africa', '7780')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP401', 'Sibusiso', 'Khumalo', 'sibusiso.khumalo@masonhotel.co.za', '0827001401',
       id, '1987-03-08', 'MALE', 'Security Officer', 'Security',
       21000.00, '2022-01-10', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('5 Lansdowne Road', 'Cape Town', 'Western Cape', 'South Africa', '7780')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP402', 'Andile', 'Mthethwa', 'andile.mthethwa@masonhotel.co.za', '0827001402',
       id, '1991-05-29', 'MALE', 'Security Officer', 'Security',
       21000.00, '2023-06-05', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('9 Station Road', 'Cape Town', 'Western Cape', 'South Africa', '7405')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP403', 'Bongani', 'Cele', 'bongani.cele@masonhotel.co.za', '0827001403',
       id, '1994-09-20', 'MALE', 'Security Officer', 'Security',
       21000.00, '2024-01-22', 'ACTIVE' FROM a;


-- ============================================================
-- KITCHEN / RESTAURANT — EMP500 to EMP509
-- ============================================================

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('4 Victoria Road', 'Cape Town', 'Western Cape', 'South Africa', '8005')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP500', 'David', 'Jacobs', 'david.jacobs@masonhotel.co.za', '0827001500',
       id, '1981-06-14', 'MALE', 'Executive Chef', 'Kitchen / Restaurant',
       48000.00, '2017-04-03', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('29 Lower Main Road', 'Cape Town', 'Western Cape', 'South Africa', '7700')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP501', 'Lebo', 'Mokoena', 'lebo.mokoena@masonhotel.co.za', '0827001501',
       id, '1988-11-21', 'FEMALE', 'Sous Chef', 'Kitchen / Restaurant',
       35000.00, '2020-08-17', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('13 Durban Road', 'Cape Town', 'Western Cape', 'South Africa', '7405')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP502', 'Bongani', 'Cele', 'bongani.cele2@masonhotel.co.za', '0827001502',
       id, '1993-04-05', 'MALE', 'Chef', 'Kitchen / Restaurant',
       26000.00, '2022-02-14', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('20 Main Road', 'Cape Town', 'Western Cape', 'South Africa', '7700')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP503', 'Ayanda', 'Dube', 'ayanda.dube@masonhotel.co.za', '0827001503',
       id, '1996-09-17', 'FEMALE', 'Chef', 'Kitchen / Restaurant',
       26000.00, '2023-03-13', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('15 Regent Road', 'Cape Town', 'Western Cape', 'South Africa', '8060')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP504', 'Jason', 'Williams', 'jason.williams@masonhotel.co.za', '0827001504',
       id, '1994-02-27', 'MALE', 'Restaurant Supervisor', 'Kitchen / Restaurant',
       28000.00, '2022-09-12', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('32 Beach Road', 'Cape Town', 'Western Cape', 'South Africa', '8005')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP505', 'Nokuthula', 'Zwane', 'nokuthula.zwane2@masonhotel.co.za', '0827001505',
       id, '1997-06-30', 'FEMALE', 'Waiter', 'Kitchen / Restaurant',
       18000.00, '2024-01-15', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('11 Main Road', 'Cape Town', 'Western Cape', 'South Africa', '8005')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP506', 'Karabo', 'Mokoena', 'karabo.mokoena2@masonhotel.co.za', '0827001506',
       id, '1998-10-11', 'FEMALE', 'Waiter', 'Kitchen / Restaurant',
       18000.00, '2024-04-08', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('18 Sea Point Road', 'Cape Town', 'Western Cape', 'South Africa', '8005')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP507', 'Musa', 'Mthembu', 'musa.mthembu@masonhotel.co.za', '0827001507',
       id, '1995-03-14', 'MALE', 'Bartender', 'Kitchen / Restaurant',
       20000.00, '2023-11-06', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('7 Campground Road', 'Cape Town', 'Western Cape', 'South Africa', '7700')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP508', 'Lerato', 'Mahlangu', 'lerato.mahlangu@masonhotel.co.za', '0827001508',
       id, '1997-12-03', 'FEMALE', 'Waiter', 'Kitchen / Restaurant',
       18000.00, '2024-06-03', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('24 Beach Road', 'Cape Town', 'Western Cape', 'South Africa', '8005')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP509', 'Thomas', 'Meyer', 'thomas.meyer@masonhotel.co.za', '0827001509',
       id, '1992-07-28', 'MALE', 'Restaurant Host', 'Kitchen / Restaurant',
       21000.00, '2023-08-21', 'ACTIVE' FROM a;


-- ============================================================
-- ADMINISTRATIVE — EMP600 to EMP604
-- ============================================================

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('18 Adderley Street', 'Cape Town', 'Western Cape', 'South Africa', '8000')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP600', 'Ayesha', 'Naidoo', 'ayesha.naidoo2@masonhotel.co.za', '0827001600',
       id, '1988-12-08', 'FEMALE', 'HR Manager', 'Administrative',
       45000.00, '2020-01-13', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('24 Bree Street', 'Cape Town', 'Western Cape', 'South Africa', '8001')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP601', 'Michael', 'Pillay', 'michael.pillay@masonhotel.co.za', '0827001601',
       id, '1986-05-22', 'MALE', 'Accountant', 'Administrative',
       34000.00, '2021-07-05', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('30 Strand Street', 'Cape Town', 'Western Cape', 'South Africa', '8001')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP602', 'Precious', 'Mahlangu', 'precious.mahlangu2@masonhotel.co.za', '0827001602',
       id, '1992-03-19', 'FEMALE', 'HR Officer', 'Administrative',
       29000.00, '2023-05-15', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('42 Loop Street', 'Cape Town', 'Western Cape', 'South Africa', '8001')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP603', 'Ethan', 'Meyer', 'ethan.meyer@masonhotel.co.za', '0827001603',
       id, '1995-07-07', 'MALE', 'Accounts Clerk', 'Administrative',
       24000.00, '2024-02-19', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('9 Darling Street', 'Cape Town', 'Western Cape', 'South Africa', '8001')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP604', 'Nandi', 'Maseko', 'nandi.maseko2@masonhotel.co.za', '0827001604',
       id, '1993-10-15', 'FEMALE', 'Procurement Officer', 'Administrative',
       30000.00, '2022-10-10', 'ACTIVE' FROM a;


-- ============================================================
-- TEMPORARY / CONTRACT — EMP700 to EMP701
-- ============================================================

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('19 Voortrekker Road', 'Cape Town', 'Western Cape', 'South Africa', '7405')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP700', 'Brian', 'Peters', 'brian.peters@masonhotel.co.za', '0827001700',
       id, '1996-04-17', 'MALE', 'Contract Receptionist', 'Temporary / Contract Staff',
       20000.00, '2026-01-12', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('26 Main Road', 'Cape Town', 'Western Cape', 'South Africa', '7700')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP701', 'Lindiwe', 'Moyo', 'lindiwe.moyo@masonhotel.co.za', '0827001701',
       id, '1998-08-26', 'FEMALE', 'Contract Housekeeper', 'Temporary / Contract Staff',
       18000.00, '2026-02-02', 'ACTIVE' FROM a;


-- ============================================================
-- MANAGEMENT TRAINEES / INTERNS — EMP800 to EMP801
-- ============================================================

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('8 Campground Road', 'Cape Town', 'Western Cape', 'South Africa', '7700')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP800', 'Zodwa', 'Mkhize', 'zodwa.mkhize@masonhotel.co.za', '0827001800',
       id, '2002-10-22', 'FEMALE', 'Management Trainee', 'Management Trainees / Interns',
       14000.00, '2026-02-02', 'ACTIVE' FROM a;

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('13 Observatory Road', 'Cape Town', 'Western Cape', 'South Africa', '7925')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT 'EMP801', 'Tumelo', 'Molefe', 'tumelo.molefe@masonhotel.co.za', '0827001801',
       id, '2001-05-16', 'MALE', 'Hospitality Intern', 'Management Trainees / Interns',
       10000.00, '2026-01-19', 'ACTIVE' FROM a;


-- ============================================================
-- OWNER — EMP900
-- ============================================================
-- Replace the placeholder contact details with your actual
-- Mason Hotel owner information.

WITH a AS (
    INSERT INTO Address (street, city, province, country, postalCode)
        VALUES ('Mason Hotel Estate', 'Cape Town', 'Western Cape', 'South Africa', '8001')
        RETURNING id
)
INSERT INTO Employees
(employeeID, firstName, lastName, email, phoneNumber, address_id,
 dateOfBirth, gender, jobPosition, department, salary, hireDate, employmentStatus)
SELECT
    'EMP900',
    'Tlou',
    'Chauke',
    'chauketlou8@gmail.com',
    '0790433094',
    id,
    '2007-06-23',
    'MALE',
    'Owner',
    'Special / Founder / Owner',
    0.00,
    '2026-07-31',
    'ACTIVE'
FROM a;