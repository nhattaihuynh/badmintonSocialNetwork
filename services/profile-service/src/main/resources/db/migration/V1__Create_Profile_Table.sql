CREATE TABLE IF NOT EXISTS profile (
    id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    bio TEXT,
    phone_number VARCHAR(20),
    profile_picture_url VARCHAR(255),
    cover_picture_url VARCHAR(255),
    location VARCHAR(100),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
