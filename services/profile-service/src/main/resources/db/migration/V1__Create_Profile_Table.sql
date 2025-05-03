CREATE TABLE profile (
    id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    bio TEXT,
    phone_number VARCHAR(20),
    profile_picture_url VARCHAR(255),
    cover_picture_url VARCHAR(255),
    skill_level VARCHAR(20),
    preferred_play_style VARCHAR(20),
    years_of_experience INTEGER,
    preferred_location VARCHAR(100),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
