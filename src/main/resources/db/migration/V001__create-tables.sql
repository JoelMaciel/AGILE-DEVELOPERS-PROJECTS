CREATE TABLE company (
    company_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL UNIQUE,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE developer (
    developer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    age BIGINT,
    position VARCHAR(30) NOT NULL,
    experience_level VARCHAR(20) CHECK (experience_level IN ('INTERN', 'JUNIOR', 'PLENO', 'SENIOR')) NOT NULL,
    years_of_experience INT NOT NULL,
    company_id BIGINT NOT NULL,
    FOREIGN KEY (company_id) REFERENCES company(company_id)
);

CREATE TABLE project (
    project_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    status VARCHAR(20) CHECK (status IN ('IN_PROGRESS', 'COMPLETED', 'CLOSED')) NOT NULL,
    company_id BIGINT NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_date TIMESTAMP,
    FOREIGN KEY (company_id) REFERENCES company(company_id)
);



CREATE TABLE task (
    task_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    status VARCHAR(20) CHECK (status IN ('IN_PROGRESS', 'COMPLETED', 'PENDING')) NOT NULL,
    developer_id BIGINT,
    project_id BIGINT,
    task_difficulty VARCHAR(20) CHECK (task_difficulty IN ('LOW', 'MEDIUM', 'HIGH')) NOT NULL,
    deadline_days INT,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_date TIMESTAMP,
    FOREIGN KEY (developer_id) REFERENCES developer(developer_id),
    FOREIGN KEY (project_id) REFERENCES project(project_id)
);
