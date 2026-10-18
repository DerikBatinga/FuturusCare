SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `FuturusCare`
--
CREATE DATABASE IF NOT EXISTS futurus_care DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE futurus_care;

-- --------------------------------------------------------
-- Estrutura das tabelas
-- --------------------------------------------------------
CREATE TABLE tb_user(
                        id_user BIGINT PRIMARY KEY AUTO_INCREMENT,
                        first_name VARCHAR(50) NOT NULL,
                        second_name VARCHAR(50) NOT NULL,
                        cpf VARCHAR(11) NOT NULL UNIQUE,
                        gender CHAR NOT NULL,
                        birthday DATE NOT NULL,
                        email VARCHAR(50) NOT NULL,
                        status ENUM('ATIVO','DESATIVO') NOT NULL DEFAULT 'ATIVO',
                        current_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_pet(
                       id_pet BIGINT PRIMARY KEY AUTO_INCREMENT,
                       user_id BIGINT,
                       first_name VARCHAR(50) NOT NULL,
                       second_name VARCHAR(50),
                       gender ENUM ('MASCULINO','FEMININO','OUTRO') NOT NULL,
                       birthday DATE NOT NULL,
                       current_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       FOREIGN KEY (user_id) REFERENCES tb_user(id_user)
);

CREATE TABLE tb_user_address(
                                user_id BIGINT PRIMARY KEY,
                                street_name VARCHAR(50) NOT NULL,
                                house_number VARCHAR(5) NOT NULL,
                                neighborhood VARCHAR(20) NOT NULL,
                                cep VARCHAR(9) NOT NULL,
                                complement VARCHAR(20),

                                FOREIGN KEY (user_id) REFERENCES tb_user(id_user)
);


CREATE TABLE tb_petsitter (
                              id_petsitter BIGINT PRIMARY KEY AUTO_INCREMENT,
                              user_id BIGINT NOT NULL UNIQUE,
    --Tem que colocar algo a mais, algo que o cara tenha de cadastrar a mais pra poder virar petsitter
                              specialization VARCHAR(30) NOT NULL,

                              FOREIGN KEY (user_id) REFERENCES tb_user(id_user)
);


CREATE TABLE tb_plan_employees (
                                   id_employee BIGINT PRIMARY KEY AUTO_INCREMENT,
                                   user_id BIGINT NOT NULL UNIQUE,
                                   work_area VARCHAR(50) NOT NULL,
                                   salary DECIMAL(10,2) NOT NULL,

                                   FOREIGN KEY (user_id) REFERENCES tb_user(id_user)
);
CREATE TABLE tb_petsitting(
                              id_petsitting BIGINT PRIMARY KEY AUTO_INCREMENT ,
                              user_id BIGINT NOT NULL,
                              pet_id BIGINT NOT NULL,
                              petsitter_id BIGINT NOT NULL,
                              petsitting_date DATETIME NOT NULL,
                              status ENUM('AGENDADA','CANCELADA','CONCLUIDA') NOT NULL DEFAULT 'AGENDADA',

                              FOREIGN KEY (user_id) REFERENCES tb_user(id_user),
                              FOREIGN KEY (pet_id) REFERENCES tb_pet(id_pet)
                              FOREIGN KEY (petsitter_id) REFERENCES tb_petsitter(id_petsitter)
);

CREATE TABLE tb_payments(
                            id_payment BIGINT PRIMARY KEY AUTO_INCREMENT,
                            petsitting_id BIGINT NOT NULL UNIQUE,
                            payment_form ENUM('DEBITO','CREDITO','PIX','BOLETO'),
                            status_payments ENUM('PAGO','PAGAMENTO_PEDENTE','CANCELADO') NOT NULL,
                                            
                            FOREIGN KEY (petsitting_id) REFERENCES tb_petsitting(id_petsitting)
);

CREATE TABLE tb_procedure (
                              id_procedure BIGINT PRIMARY KEY AUTO_INCREMENT,
                              diagnostic VARCHAR(20) NOT NULL,
                              description VARCHAR(50) NOT NULL,
                              procedure_value DECIMAL(10,2) NOT NULL
);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
