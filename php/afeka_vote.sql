--
-- Database: `afeka_vote`
--

-- --------------------------------------------------------

--
-- Table structure for table `apps_names`
--

CREATE TABLE IF NOT EXISTS `apps_names` (
  `year` varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL,
  `semester` varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL,
  `class_name` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `app_name` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `app_votes`
--

CREATE TABLE IF NOT EXISTS `app_votes` (
  `year` varchar(8) COLLATE utf8_unicode_ci NOT NULL,
  `semester` varchar(8) COLLATE utf8_unicode_ci NOT NULL,
  `class_name` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `phone_id` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `app_name` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `category` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `grade` int(11) NOT NULL,
  UNIQUE KEY `unique_index` (`year`,`semester`,`class_name`,`phone_id`,`app_name`,`category`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `catg_names`
--

CREATE TABLE IF NOT EXISTS `catg_names` (
  `year` varchar(8) COLLATE utf8_unicode_ci NOT NULL,
  `semester` varchar(8) COLLATE utf8_unicode_ci NOT NULL,
  `class_name` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `catg_name` varchar(64) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `classes`
--

CREATE TABLE IF NOT EXISTS `classes` (
  `class_id` int(11) NOT NULL AUTO_INCREMENT,
  `year` varchar(8) COLLATE utf8_unicode_ci NOT NULL,
  `semester` varchar(8) COLLATE utf8_unicode_ci NOT NULL,
  `class_name` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `min_votes` int(11) NOT NULL,
  `is_valid` tinyint(4) NOT NULL,
  PRIMARY KEY (`class_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2 ;
