-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`User` (
  `ID` INT NOT NULL,
  `USERNAME` VARCHAR(50) NOT NULL,
  `EMAIL` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  UNIQUE INDEX `USERNAME_UNIQUE` (`USERNAME` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`EMAIL` ASC) VISIBLE,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`USER_CONNECTIONS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`USER_CONNECTIONS` (
  `USER_ID` INT NULL,
  `CONNECTED_USER_ID` INT NULL,
  INDEX `FK_CONNECTIONS_USER_idx` (`USER_ID` ASC) VISIBLE,
  INDEX `FK_CONNECTIONS_CONNECTED_USER_idx` (`CONNECTED_USER_ID` ASC) VISIBLE,
  CONSTRAINT `FK_CONNECTIONS_USER`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `mydb`.`User` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_CONNECTIONS_CONNECTED_USER`
    FOREIGN KEY (`CONNECTED_USER_ID`)
    REFERENCES `mydb`.`User` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`TRANSACTIONS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`TRANSACTIONS` (
  `ID` INT NOT NULL,
  `SENDER_ID` INT NOT NULL,
  `RECEIVER_ID` INT NOT NULL,
  `DESCRIPTION` VARCHAR(255) NULL,
  `AMOUNT` DECIMAL(12,2) NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `FK_TRANSACTIONS_SENDER_idx` (`SENDER_ID` ASC) VISIBLE,
  INDEX `FK_TRANSACTIONS_RECEIVER_idx` (`RECEIVER_ID` ASC) VISIBLE,
  CONSTRAINT `FK_TRANSACTIONS_SENDER`
    FOREIGN KEY (`SENDER_ID`)
    REFERENCES `mydb`.`User` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_TRANSACTIONS_RECEIVER`
    FOREIGN KEY (`RECEIVER_ID`)
    REFERENCES `mydb`.`User` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
