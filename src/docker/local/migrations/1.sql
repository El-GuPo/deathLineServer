CREATE TABLE users (
                       user_id SERIAL PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL
)

CREATE TABLE deadlines (
                           deadline_id SERIAL PRIMARY KEY,
                           user_id INT NOT NULL,
                           deadline_name VARCHAR(200) NOT NULL,
                           deadline_description TEXT,
                           deadline TIMESTAMP NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT fk_deadlines_user
                               FOREIGN KEY (user_id)
                                   REFERENCES users(user_id)
                                   ON DELETE CASCADE
);

CREATE INDEX idx_deadlines_user_id ON deadlines(user_id);