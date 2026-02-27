-- SQLite Database Schema for TODO Application
-- Run this script to initialize the todo.db database

-- Create tasks table
CREATE TABLE IF NOT EXISTS tasks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    completed BOOLEAN DEFAULT 0
);

-- Create index on id for faster lookups
CREATE INDEX IF NOT EXISTS idx_task_id ON tasks(id);

-- Insert sample tasks (optional - comment out to skip)
INSERT INTO tasks (title, completed) VALUES ('Learn Java REST APIs', 0);
INSERT INTO tasks (title, completed) VALUES ('Build TODO application', 0);
INSERT INTO tasks (title, completed) VALUES ('Test SQLite integration', 0);
INSERT INTO tasks (title, completed) VALUES ('Create frontend UI', 0);

-- Verify table creation
SELECT 'Tasks table created successfully' as status;
SELECT COUNT(*) as total_tasks FROM tasks;
