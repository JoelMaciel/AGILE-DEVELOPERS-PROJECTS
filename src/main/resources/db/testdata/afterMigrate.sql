set foreign_key_checks = 0;

delete from task;
delete from project;
delete from developer;
delete from company;

set foreign_key_checks = 1;


INSERT INTO company (company_id, name, creation_date) VALUES
    (1, 'IBM', CURRENT_TIMESTAMP),
    (2, 'AMAZON', CURRENT_TIMESTAMP),
    (3, 'GOOGLE', CURRENT_TIMESTAMP);


INSERT INTO project (project_id, name, status, company_id, creation_date, update_date) VALUES
    (1, 'Project X', 'IN_PROGRESS', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'Project Y', 'COMPLETED', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 'Project Z', 'CLOSED', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO developer (developer_id, name, phone_number, age, position, experience_level, years_of_experience, company_id) VALUES
    (1, 'John Doe', '123456789', 30, 'Software Engineer', 'JUNIOR', 2, 1),
    (2, 'Jane Smith', '987654321', 35, 'Backend Developer', 'SENIOR', 8, 2),
    (3, 'Bob Johnson', '555555555', 25, 'FullStack Developer', 'JUNIOR', 1, 3);

INSERT INTO task (task_id, description, status, developer_id, project_id, task_difficulty, deadline_days, creation_date, update_date) VALUES
    (1, 'Implement Feature A', 'IN_PROGRESS', 1, 1, 'MEDIUM', 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'Fix Bug in Module B', 'PENDING', 2, 2, 'HIGH', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 'Write Unit Tests for Module C', 'COMPLETED', 3, 3, 'LOW', 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


