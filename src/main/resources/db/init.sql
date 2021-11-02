CREATE TABLE IF NOT EXISTS user (
    `id` INT NOT NULL AUTO_INCREMENT,
    `email` CHAR(100) NOT NULL,
    `nickname` CHAR(100) NOT NULL,
    `name` CHAR(100) NOT NULL,
    `phone_number` CHAR(50) NOT NULL,
    `password` CHAR(100) NOT NULL,
    `token` CHAR(100) NOT NULL,
    PRIMARY KEY(`id`),
    UNIQUE KEY(`email`),
    UNIQUE KEY(`phone_number`),
    UNIQUE KEY(`token`)
);