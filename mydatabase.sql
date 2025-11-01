CREATE DATABASE IF NOT EXISTS hospital_database;
USE hospital_database;

CREATE TABLE patients (
    patient_id VARCHAR(10) PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    age INT,
    gender ENUM('Male', 'Female', 'Other'),
    city VARCHAR(100)
);

CREATE TABLE visits (
    visit_id VARCHAR(10) PRIMARY KEY,
    patient_id VARCHAR(10),
    visit_date DATE,
    doctor VARCHAR(100),
    reason VARCHAR(200),
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);

CREATE TABLE diagnoses (
    diagnosis_id VARCHAR(10) PRIMARY KEY,
    visit_id VARCHAR(10),
    condition_name VARCHAR(100),
    severity ENUM('Mild', 'Moderate', 'Severe'),
    FOREIGN KEY (visit_id) REFERENCES visits(visit_id)
);

CREATE TABLE treatment (
    treatment_id VARCHAR(10) PRIMARY KEY,
    diagnosis_id VARCHAR(10),
    medication VARCHAR(100),
    dosage VARCHAR(50),
    duration VARCHAR(50),
    FOREIGN KEY (diagnosis_id) REFERENCES diagnoses(diagnosis_id)
);

SHOW TABLES;

USE hospital_database;

ALTER TABLE patients 
MODIFY gender ENUM('M', 'F', 'Other');

SET SQL_SAFE_UPDATES = 0;
DELETE FROM diagnoses;
DELETE FROM visits;
DELETE FROM patients;
SET SQL_SAFE_UPDATES = 1;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE treatment;
TRUNCATE TABLE diagnoses;
TRUNCATE TABLE visits;
TRUNCATE TABLE patients;
SET FOREIGN_KEY_CHECKS = 1;

USE hospital_database;
SELECT COUNT(*) AS total_patients FROM patients;
SELECT COUNT(*) AS total_visits FROM visits;
SELECT COUNT(*) AS total_diagnoses FROM diagnoses;
SELECT COUNT(*) AS total_treatments FROM treatment;

SELECT * FROM patients;
SELECT * FROM visits;
SELECT * FROM diagnoses;
SELECT * FROM treatment;

SELECT p.patient_id, 
       CONCAT(p.first_name, ' ', p.last_name) AS patient_name, 
       COUNT(v.visit_id) AS visit_count
FROM patients p
JOIN visits v ON p.patient_id = v.patient_id
GROUP BY p.patient_id
ORDER BY visit_count DESC;

SELECT 
  CONCAT(FLOOR(age/10)*10, '-', FLOOR(age/10)*10 + 9) AS age_group,
  COUNT(v.visit_id)/COUNT(DISTINCT p.patient_id) AS avg_visits
FROM patients p
JOIN visits v ON p.patient_id = v.patient_id
GROUP BY age_group
ORDER BY age_group;

SELECT condition_name, COUNT(*) AS count
FROM diagnoses
GROUP BY condition_name
ORDER BY count DESC
LIMIT 10;

SELECT severity, COUNT(*) AS count
FROM diagnoses
GROUP BY severity;

SELECT d.condition_name, COUNT(t.treatment_id) AS treatment_count
FROM treatment t
JOIN diagnoses d ON t.diagnosis_id = d.diagnosis_id
GROUP BY d.condition_name
ORDER BY treatment_count DESC
LIMIT 10;

SELECT medication, COUNT(*) AS count
FROM treatment
GROUP BY medication
ORDER BY count DESC
LIMIT 10;












