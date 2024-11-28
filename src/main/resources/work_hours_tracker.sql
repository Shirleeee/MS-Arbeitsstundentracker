SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `work_hours_tracker`
--

-- --------------------------------------------------------

--
-- Table structure for table `user`
--
CREATE TABLE IF NOT EXISTS `user`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT,
    `name`         varchar(256) NOT NULL,
    `mail_address` varchar(256) NOT NULL,
    `created_at`   datetime     DEFAULT NULL,
    `updated_at`   datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `project`
--

CREATE TABLE IF NOT EXISTS `project`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `user_id`     bigint       NOT NULL,
    `name`        varchar(256) NOT NULL,
    `description` varchar(1024) DEFAULT NULL,
    `deadline`    datetime     NOT NULL,
    `created_at`  datetime     DEFAULT NULL,
    `updated_at`  datetime      DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `task`
--

CREATE TABLE IF NOT EXISTS `task`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `project_id`  bigint       NOT NULL,
    `name`        varchar(256) NOT NULL,
    `description` varchar(1024) DEFAULT NULL,
    `deadline`    datetime      DEFAULT NULL,
    `created_at`  datetime     DEFAULT NULL,
    `updated_at`  datetime      DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `time_entry`
--

CREATE TABLE IF NOT EXISTS `time_entry`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT,
    `task_id`     bigint   NOT NULL,
    `start_time`  datetime DEFAULT NULL,
    `end_time`    datetime DEFAULT NULL,
    `time_period` bigint   DEFAULT NULL,
    `created_at`  datetime DEFAULT NULL,
    `updated_at`  datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;
-- --------------------------------------------------------

--
-- Table structure for table `report`
--

CREATE TABLE IF NOT EXISTS `report`
(
    `id`         bigint   NOT NULL AUTO_INCREMENT,
    `user_id`    bigint   NOT NULL,
    `date`       datetime NOT NULL,
    `link`       varchar(256) DEFAULT NULL,
    `created_at` datetime DEFAULT NULL,
    `updated_at` datetime     DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
