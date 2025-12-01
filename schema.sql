-- schema.sql - Estrutura do Banco GameManager
-- Execute: sqlite3 games.db < schema.sql

CREATE TABLE IF NOT EXISTS games (
                                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                                     title TEXT NOT NULL UNIQUE,
                                     release_date TEXT,
                                     genre TEXT,
                                     image_url TEXT,
                                     background_image_url TEXT,
                                     platform TEXT,
                                     rating REAL DEFAULT 0.0
);