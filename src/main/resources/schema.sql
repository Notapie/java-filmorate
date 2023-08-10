CREATE TABLE IF NOT EXISTS "user" (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(64),
    login VARCHAR(64),
    name VARCHAR(64),
    birthday TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS "user_email_unique_idx"
    ON "user" (email);

CREATE UNIQUE INDEX IF NOT EXISTS "user_email_unique_idx"
    ON "user" (login);

CREATE TABLE IF NOT EXISTS "user_friend" (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id INTEGER REFERENCES "user" (id) ON DELETE CASCADE,
    friend_id INTEGER REFERENCES "user" (id) ON DELETE CASCADE,
    is_accepted BOOLEAN,
    is_viewed BOOLEAN
);

CREATE UNIQUE INDEX IF NOT EXISTS "user_friend_unique_idx"
    ON "user_friend" (user_id, friend_id);

CREATE TABLE IF NOT EXISTS "genre" (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS "rating" (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(16)
);

CREATE TABLE IF NOT EXISTS "film" (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title VARCHAR(64),
    rating_id INTEGER REFERENCES "rating" (id) ON DELETE SET NULL,
    description VARCHAR(1024),
    release_date DATE,
    duration INTEGER,
    likes_count INTEGER DEFAULT 0
);

CREATE INDEX IF NOT EXISTS "film_likes_idx"
    ON "film" (likes_count);

CREATE TABLE IF NOT EXISTS "film_genre" (
    film_id INTEGER REFERENCES "film" (id) ON DELETE CASCADE,
    genre_id INTEGER REFERENCES "genre" (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS "film_genre_unique_idx"
    ON "film_genre" (film_id, genre_id);

CREATE TABLE IF NOT EXISTS "user_favorite" (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id INTEGER REFERENCES "user" (id) ON DELETE CASCADE,
    film_id INTEGER REFERENCES "film" (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS "user_favorite_unique_idx"
    ON "user_favorite" (user_id, film_id);
