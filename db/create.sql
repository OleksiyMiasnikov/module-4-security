-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema module_4_db
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `module_4_db` ;

-- -----------------------------------------------------
-- Schema module_4_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `module_4_db` DEFAULT CHARACTER SET utf8mb3 ;
USE `module_4_db` ;

-- -----------------------------------------------------
-- Table `module_4_db`.`certificate`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `module_4_db`.`certificate` ;

CREATE TABLE IF NOT EXISTS `module_4_db`.`certificate` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `description` VARCHAR(100) NULL DEFAULT NULL,
  `price` DECIMAL(10,2) UNSIGNED NULL DEFAULT NULL,
  `duration` DECIMAL(10,0) UNSIGNED NULL DEFAULT NULL,
  `create_date` DATETIME NULL DEFAULT NULL,
  `last_update_date` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 30
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `module_4_db`.`tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `module_4_db`.`tag` ;

CREATE TABLE IF NOT EXISTS `module_4_db`.`tag` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 29
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `module_4_db`.`certificate_with_tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `module_4_db`.`certificate_with_tag` ;

CREATE TABLE IF NOT EXISTS `module_4_db`.`certificate_with_tag` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `certificate_id` INT UNSIGNED NOT NULL,
  `tag_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`, `certificate_id`, `tag_id`),
  INDEX `fk_certificate_with_tag_certificate1_idx` (`certificate_id` ASC) VISIBLE,
  INDEX `fk_certificate_with_tag_tag1_idx` (`tag_id` ASC) VISIBLE,
  CONSTRAINT `fk_certificate_with_tag_certificate1`
    FOREIGN KEY (`certificate_id`)
    REFERENCES `module_4_db`.`certificate` (`id`),
  CONSTRAINT `fk_certificate_with_tag_tag1`
    FOREIGN KEY (`tag_id`)
    REFERENCES `module_4_db`.`tag` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `module_4_db`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `module_4_db`.`user` ;

CREATE TABLE IF NOT EXISTS `module_4_db`.`user` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(100) NULL DEFAULT NULL,
  `role` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `module_4_db`.`user_order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `module_4_db`.`user_order` ;

CREATE TABLE IF NOT EXISTS `module_4_db`.`user_order` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `cost` DECIMAL(10,2) UNSIGNED NULL DEFAULT NULL,
  `create_date` DATETIME NULL DEFAULT NULL,
  `user_id` INT UNSIGNED NOT NULL,
  `certificate_with_tag_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`, `user_id`, `certificate_with_tag_id`),
  INDEX `fk_user_order_user1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_user_order_certificate_with_tag1_idx` (`certificate_with_tag_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_order_certificate_with_tag1`
    FOREIGN KEY (`certificate_with_tag_id`)
    REFERENCES `module_4_db`.`certificate_with_tag` (`id`),
  CONSTRAINT `fk_user_order_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `module_4_db`.`user` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
