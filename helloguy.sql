-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 14, 2023 at 09:18 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.1.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `helloguy`
--

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `description`, `name`) VALUES
(1, 'Chuột máy tính Dell', 'Chuột máy tính');

-- --------------------------------------------------------

--
-- Table structure for table `devices`
--

CREATE TABLE `devices` (
  `device_id` bigint(20) NOT NULL,
  `maintenance_status` int(11) DEFAULT NULL,
  `maintenance_time` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `serial` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `warranty_status` int(11) DEFAULT NULL,
  `warranty_time` bigint(20) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `goods_receipt_note_id` bigint(20) NOT NULL,
  `outgoing_goods_note_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `devices`
--

INSERT INTO `devices` (`device_id`, `maintenance_status`, `maintenance_time`, `name`, `price`, `serial`, `status`, `warranty_status`, `warranty_time`, `category_id`, `goods_receipt_note_id`, `outgoing_goods_note_id`) VALUES
(1, NULL, 24, 'Chuột DELL xịn v1', 120000, 'DEL1', 1, 0, 24, 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `goods_receipt_note`
--

CREATE TABLE `goods_receipt_note` (
  `id` bigint(20) NOT NULL,
  `company_name` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `goods_receipt_note`
--

INSERT INTO `goods_receipt_note` (`id`, `company_name`, `date`, `fullname`, `phone`, `user_id`) VALUES
(1, 'ĐHBKHN', '2023-06-15 02:14:28', 'Vũ Đức Thịnh', '0968968032', 1);

-- --------------------------------------------------------

--
-- Table structure for table `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(2);

-- --------------------------------------------------------

--
-- Table structure for table `outgoing_goods_note`
--

CREATE TABLE `outgoing_goods_note` (
  `id` bigint(20) NOT NULL,
  `export_date` datetime DEFAULT NULL,
  `id_exporter` bigint(20) DEFAULT NULL,
  `id_receiver` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `outgoing_goods_note`
--

INSERT INTO `outgoing_goods_note` (`id`, `export_date`, `id_exporter`, `id_receiver`) VALUES
(1, '2023-06-15 02:14:52', 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `refreshtoken`
--

CREATE TABLE `refreshtoken` (
  `id` bigint(20) NOT NULL,
  `expiry_date` datetime NOT NULL,
  `token` varchar(255) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `name` varchar(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `birth_date` datetime DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `join_date` datetime DEFAULT NULL,
  `password` varchar(120) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `ten_ban` varchar(255) DEFAULT NULL,
  `ten_phong` varchar(255) DEFAULT NULL,
  `ten_vien` varchar(255) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `birth_date`, `email`, `fullname`, `join_date`, `password`, `phone`, `ten_ban`, `ten_phong`, `ten_vien`, `username`) VALUES
(1, '2002-09-15 00:00:00', 'af@gmail.com', 'Vũ Đức Thịnh', '2023-06-15 02:12:33', '$2a$10$wztuTAIDvGExUvhWfG41sO.mIqKjwVyItieyFJS36V.TPhtHK005K', '0968968032', '', '', 'Điện tử viễn thông', 'vuducthinh15019'),
(2, '2002-09-15 00:00:00', 'quang@gmail.com', 'Lại Đăng Quang', '2023-06-15 02:13:01', '$2a$10$6334pw46Disn3yNqiVd3q.V3QOv39hgJnm8Vr6.9kos8vMy36wuMO', '0968968032', '', '', 'Điện tử viễn thông', 'laidangquang');

-- --------------------------------------------------------

--
-- Table structure for table `user_roles`
--

CREATE TABLE `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_roles`
--

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2);

-- --------------------------------------------------------

--
-- Table structure for table `warranty_card`
--

CREATE TABLE `warranty_card` (
  `id` bigint(20) NOT NULL,
  `confirm_status` int(11) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `handover_date` datetime DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `id_confirmer` bigint(20) DEFAULT NULL,
  `device_id` bigint(20) NOT NULL,
  `id_exporter` bigint(20) DEFAULT NULL,
  `id_receiver` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `warranty_card`
--

INSERT INTO `warranty_card` (`id`, `confirm_status`, `date`, `handover_date`, `note`, `price`, `status`, `id_confirmer`, `device_id`, `id_exporter`, `id_receiver`) VALUES
(1, 1, '2023-06-15 02:15:10', NULL, 'this is a note', NULL, 0, 1, 1, NULL, 1),
(2, 2, '2023-06-15 02:15:14', NULL, 'this is a note', NULL, NULL, 1, 1, NULL, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKdpusovwfttipdju5vdp10kcbm` (`description`) USING HASH;

--
-- Indexes for table `devices`
--
ALTER TABLE `devices`
  ADD PRIMARY KEY (`device_id`),
  ADD UNIQUE KEY `UKnn00yd5sid89np7fo8lxgg8lb` (`serial`) USING HASH,
  ADD KEY `FK328396uetnetexyi8dhfcem6w` (`category_id`),
  ADD KEY `FKrnh1jg9u085fn0smer6xa1rac` (`goods_receipt_note_id`),
  ADD KEY `FKc1b6t8xrtvax3mo60mu9yol4q` (`outgoing_goods_note_id`);

--
-- Indexes for table `goods_receipt_note`
--
ALTER TABLE `goods_receipt_note`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK15gescm5mrsrxnj359c9k471e` (`user_id`);

--
-- Indexes for table `outgoing_goods_note`
--
ALTER TABLE `outgoing_goods_note`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK36j36med14sud71f0yont1ncj` (`id_exporter`),
  ADD KEY `FKk1dpy1meswpcrtfkgr6yiq340` (`id_receiver`);

--
-- Indexes for table `refreshtoken`
--
ALTER TABLE `refreshtoken`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_or156wbneyk8noo4jstv55ii3` (`token`) USING HASH,
  ADD KEY `FKa652xrdji49m4isx38pp4p80p` (`user_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  ADD UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`);

--
-- Indexes for table `user_roles`
--
ALTER TABLE `user_roles`
  ADD PRIMARY KEY (`user_id`,`role_id`),
  ADD KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`);

--
-- Indexes for table `warranty_card`
--
ALTER TABLE `warranty_card`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKigk8lh0seep1p3gxafh9cxo51` (`id_confirmer`),
  ADD KEY `FKewtargvyutcjln82u2tmpsveg` (`device_id`),
  ADD KEY `FKqi9lkd1uek1kecj0calg0469v` (`id_exporter`),
  ADD KEY `FKj82iofv9duv44sj7bo9o4quop` (`id_receiver`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `devices`
--
ALTER TABLE `devices`
  MODIFY `device_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `goods_receipt_note`
--
ALTER TABLE `goods_receipt_note`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `outgoing_goods_note`
--
ALTER TABLE `outgoing_goods_note`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `warranty_card`
--
ALTER TABLE `warranty_card`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
