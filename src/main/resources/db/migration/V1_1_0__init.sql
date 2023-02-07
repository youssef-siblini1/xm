CREATE TABLE IF NOT EXISTS `trade` (
	`id`                UUID PRIMARY KEY,
	`quantity`          INTEGER NOT NULL,
	`symbol`            VARCHAR(50) NOT NULL,
	`price`             DECIMAL NOT NULL,
	`creation_date`     TIMESTAMP NOT NULL,
	`updated_date`      TIMESTAMP NOT NULL,
	`status`            VARCHAR(50) NOT NULL
);