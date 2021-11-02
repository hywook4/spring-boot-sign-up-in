CREATE TABLE IF NOT EXISTS user (
    `id` INT NOT NULL AUTO_INCREMENT,
    `email` CHAR(32) NOT NULL,
    `nickname` CHAR(20) NOT NULL,
    `name` CHAR(10) NOT NULL,
    `phone_number` CHAR(15) NOT NULL,
    `password` CHAR(100) NOT NULL,
    `token` CHAR(50) NOT NULL,
    PRIMARY KEY(`id`),
    UNIQUE KEY(`email`),
    UNIQUE KEY(`phone_number`),
    UNIQUE KEY(`token`)
);