PRAGMA foreign_keys = OFF;

CREATE TABLE IF NOT EXISTS games (
    id INTERGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL UNIQUE,
    release_date TEXT,
    genre TEXT,
    image_url TEXT,
    background_image_url TEXT,
    platform TEXT,
    rating REAL DEFAULT 0.0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_at DATETIME DEAFULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_games_title ON games(title);
CREATE INDEX IF NOT EXISTS idx_games_genre ON games(genre);
CREATE INDEX IF NOT EXISTS idx_games_release_date ON games(release_date);

CREATE TRIGGER IF NOT EXISTS update_games_timestamp
AFTER UPADTE ON games
FOR EACH ROW
BEGIN
    UPDATE games SET updated_at = CURRENT_TIMESTAMP WHERE id = NEW.id;
END;

PRAGMA foreign_keys = ON;

SELECT 'Database schema created successfully!' as status;