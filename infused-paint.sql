-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 08, 2020 at 12:48 PM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.4.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `infused-paint`
--

-- --------------------------------------------------------

--
-- Table structure for table `image_results`
--

CREATE TABLE `image_results` (
  `photo_id` int(11) NOT NULL,
  `user_id` varchar(100) NOT NULL,
  `style_id` int(11) NOT NULL,
  `content_image` varchar(255) NOT NULL,
  `file_result` varchar(255) NOT NULL,
  `share_ig` tinyint(1) DEFAULT NULL,
  `share_twitter` tinyint(1) DEFAULT NULL,
  `share_fb` tinyint(1) DEFAULT NULL,
  `created_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `image_results`
--

INSERT INTO `image_results` (`photo_id`, `user_id`, `style_id`, `content_image`, `file_result`, `share_ig`, `share_twitter`, `share_fb`, `created_at`) VALUES
(1, 'septilsnna', 6, 'ahddkasjd', 'dndskjds', NULL, NULL, NULL, '2020-09-30 23:59:53'),
(2, 'septilsnna', 5, 'ahddkasjd', 'dndskjds', NULL, NULL, NULL, '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `image_styles`
--

CREATE TABLE `image_styles` (
  `style_id` int(11) NOT NULL,
  `title` varchar(50) NOT NULL,
  `creator` varchar(50) NOT NULL,
  `image_story` text NOT NULL,
  `image_file` varchar(255) NOT NULL,
  `used_freq` int(11) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `image_styles`
--

INSERT INTO `image_styles` (`style_id`, `title`, `creator`, `image_story`, `image_file`, `used_freq`, `created_at`) VALUES
(2, 'Starry Night', 'Vincent Van Gogh', 'adkaldksddmdao', 'asdfghjkl', NULL, '2020-09-30 01:50:57');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `username` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `edit_freq` int(11) NOT NULL,
  `share_freq` int(11) NOT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`username`, `password`, `name`, `email`, `edit_freq`, `share_freq`, `created_at`) VALUES
('akangironman', 'akangironman', 'Iron Man', 'akangironman@gmail.com', 0, 0, '2020-09-29 23:11:56'),
('atylestari', 'atylestari', 'Aty Lestari', 'atylestari@gmail.com', 0, 0, '2020-09-29 22:33:19'),
('salsarahmadati', 'salsarahmadati', 'Salsa Rahmadati', 'salsarahmadati@gmail.com', 0, 0, '2020-09-29 22:32:43'),
('septilsnna', 'septilusianna', 'Septi Lusianna', 'septilusianna19@gmail.com', 0, 0, '2020-09-29 22:32:43'),
('skawngur', 'skawnguri', 'Nam Joo Hyuk', 'namjoohyuk@gmail.com', 0, 0, '2020-09-30 01:11:18');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `image_results`
--
ALTER TABLE `image_results`
  ADD PRIMARY KEY (`photo_id`);

--
-- Indexes for table `image_styles`
--
ALTER TABLE `image_styles`
  ADD PRIMARY KEY (`style_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `image_results`
--
ALTER TABLE `image_results`
  MODIFY `photo_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `image_styles`
--
ALTER TABLE `image_styles`
  MODIFY `style_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
