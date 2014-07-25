-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Июл 25 2014 г., 17:23
-- Версия сервера: 5.5.25
-- Версия PHP: 5.2.12

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `theerder`
--

-- --------------------------------------------------------

--
-- Структура таблицы `ai`
--

CREATE TABLE IF NOT EXISTS `ai` (
  `entityId` int(11) NOT NULL,
  `class` text NOT NULL,
  `reaction` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `ai`
--

INSERT INTO `ai` (`entityId`, `class`, `reaction`) VALUES
(7, 'ru.alastar.game.ai.npc.GuardAI', 1),
(8, 'ru.alastar.game.ai.hostile.Orc', 1);

-- --------------------------------------------------------

--
-- Структура таблицы `attributes`
--

CREATE TABLE IF NOT EXISTS `attributes` (
  `name` text NOT NULL,
  `itemId` int(11) NOT NULL,
  `value` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `attributes`
--

INSERT INTO `attributes` (`name`, `itemId`, `value`) VALUES
('Durability', 6, 949),
('Durability', 2, 893),
('Charges', 17, 10);

-- --------------------------------------------------------

--
-- Структура таблицы `entities`
--

CREATE TABLE IF NOT EXISTS `entities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountId` int(11) NOT NULL,
  `caption` text NOT NULL,
  `type` text NOT NULL,
  `worldId` int(11) NOT NULL,
  `x` float NOT NULL,
  `y` float NOT NULL,
  `z` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Дамп данных таблицы `entities`
--

INSERT INTO `entities` (`id`, `accountId`, `caption`, `type`, `worldId`, `x`, `y`, `z`) VALUES
(4, 1, 'Wookie', 'Elf', 1, -5, -7, 2),
(5, 1, 'Zuzya', 'Human', 1, -4.5178, 2.34251, 1),
(6, 3, 'Alastar', 'Human', 1, -7, -3, 2),
(7, -1, 'Ed', 'Human', 1, 1, 1, 2);

-- --------------------------------------------------------

--
-- Структура таблицы `equip`
--

CREATE TABLE IF NOT EXISTS `equip` (
  `entityId` int(11) NOT NULL,
  `slotName` text NOT NULL,
  `itemId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `equip`
--

INSERT INTO `equip` (`entityId`, `slotName`, `itemId`) VALUES
(5, 'Head', -1);

-- --------------------------------------------------------

--
-- Структура таблицы `inventories`
--

CREATE TABLE IF NOT EXISTS `inventories` (
  `entityId` int(11) NOT NULL,
  `max` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `inventories`
--

INSERT INTO `inventories` (`entityId`, `max`) VALUES
(4, 20),
(5, 20),
(6, 20);

-- --------------------------------------------------------

--
-- Структура таблицы `items`
--

CREATE TABLE IF NOT EXISTS `items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `worldId` int(11) NOT NULL,
  `caption` text NOT NULL,
  `amount` int(11) NOT NULL,
  `entityId` int(11) NOT NULL DEFAULT '-1',
  `equipType` text NOT NULL,
  `type` text NOT NULL,
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  `z` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=20 ;

--
-- Дамп данных таблицы `items`
--

INSERT INTO `items` (`id`, `worldId`, `caption`, `amount`, `entityId`, `equipType`, `type`, `x`, `y`, `z`) VALUES
(2, 1, 'Wooden pickaxe', 1, 4, 'None', 'Pickaxe', 0, 0, 0),
(6, 1, 'Wooden axe', 1, 4, 'None', 'Axe', 0, 0, 0),
(7, 1, 'wheat', 3, 4, 'None', 'Plant', 0, 0, 0),
(8, 1, 'emerald', 7, 4, 'None', 'Gem', 0, 0, 0),
(9, 1, 'iron ore', 9, 4, 'None', 'Ore', 0, 0, 0),
(10, 1, 'ginseng', 7, 4, 'None', 'Reagent', 0, 0, 0),
(11, 1, 'swiftstone', 3, 4, 'None', 'Gem', 0, 0, 0),
(12, 1, 'copper ore', 7, 4, 'None', 'Ore', 0, 0, 0),
(14, 1, 'Coin', 1, 4, 'None', 'Gold', 0, 0, 0),
(15, 1, 'Coin', 1, 5, 'None', 'Gold', 22, 40, 0),
(17, 1, 'Wooden Totem', 10, 4, 'None', 'Totem', 0, 0, 0),
(18, 1, 'Coin', 1, 6, 'None', 'Gold', 0, 0, 0),
(19, -1, 'Helm', 1, 4, 'Head', 'Helm', 1, 1, 1);

-- --------------------------------------------------------

--
-- Структура таблицы `locationflags`
--

CREATE TABLE IF NOT EXISTS `locationflags` (
  `locationId` int(11) NOT NULL,
  `flag` text NOT NULL,
  `val` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `locationflags`
--

INSERT INTO `locationflags` (`locationId`, `flag`, `val`) VALUES
(1, 'Start', ''),
(2, 'PVP', ''),
(3, 'Wood', ''),
(4, 'Wood', ''),
(5, 'Mine', ''),
(6, 'Mine', ''),
(3, 'Plough', ''),
(4, 'Plough', '');

-- --------------------------------------------------------

--
-- Структура таблицы `locations`
--

CREATE TABLE IF NOT EXISTS `locations` (
  `id` int(11) NOT NULL,
  `worldId` text NOT NULL,
  `name` text NOT NULL,
  `nearlocationsIDs` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `locations`
--

INSERT INTO `locations` (`id`, `worldId`, `name`, `nearlocationsIDs`) VALUES
(1, 'Primus', 'River', '4;3;5;6;'),
(2, 'Hell', 'Lava Lake', ''),
(3, 'Primus', 'Plains', '1;4;'),
(4, 'Primus', 'Plains', '3;1;'),
(5, 'Primus', 'Old Mines', '1;'),
(6, 'Primus', 'Old Mines', '1;');

-- --------------------------------------------------------

--
-- Структура таблицы `plants`
--

CREATE TABLE IF NOT EXISTS `plants` (
  `name` text NOT NULL,
  `growTime` date NOT NULL,
  `locationId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `plants`
--

INSERT INTO `plants` (`name`, `growTime`, `locationId`) VALUES
('wheat', '2014-06-30', 4);

-- --------------------------------------------------------

--
-- Структура таблицы `skills`
--

CREATE TABLE IF NOT EXISTS `skills` (
  `entityId` int(11) NOT NULL,
  `name` text NOT NULL,
  `sValue` int(11) NOT NULL,
  `mValue` int(11) NOT NULL,
  `hardness` float NOT NULL,
  `primaryStat` text NOT NULL,
  `secondaryStat` text NOT NULL,
  `state` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `skills`
--

INSERT INTO `skills` (`entityId`, `name`, `sValue`, `mValue`, `hardness`, `primaryStat`, `secondaryStat`, `state`) VALUES
(4, 'Carpentry', 50, 50, 5, 'Int', 'Strength', 0),
(4, 'Lumberjacking', 2, 50, 5, 'Strength', 'Dexterity', 0),
(4, 'Herding', 0, 50, 5, 'Int', 'Int', 0),
(4, 'Swords', 32, 50, 5, 'Strength', 'Dexterity', 0),
(4, 'Necromancy', 0, 50, 5, 'Int', 'Int', 0),
(4, 'Magery', 0, 50, 5, 'Int', 'Int', 0),
(4, 'Chivalry', 0, 50, 5, 'Strength', 'Int', 0),
(4, 'Parrying', 3, 50, 5, 'Dexterity', 'Strength', 0),
(4, 'Mining', 0, 50, 5, 'Strength', 'Int', 0),
(5, 'Taming', 0, 50, 5, 'Int', 'Strength', 0),
(5, 'Lumberjacking', 0, 50, 5, 'Strength', 'Dexterity', 0),
(5, 'Herding', 0, 50, 5, 'Int', 'Int', 0),
(5, 'Swords', 33, 50, 5, 'Strength', 'Dexterity', 0),
(5, 'Necromancy', 0, 50, 5, 'Int', 'Int', 0),
(5, 'Magery', 0, 50, 5, 'Int', 'Int', 0),
(5, 'Chivalry', 0, 50, 5, 'Strength', 'Int', 0),
(5, 'Parrying', 10, 50, 5, 'Dexterity', 'Strength', 0),
(5, 'Mining', 0, 50, 5, 'Strength', 'Int', 0),
(6, 'Taming', 0, 50, 5, 'Int', 'Strength', 0),
(6, 'Lumberjacking', 0, 50, 5, 'Strength', 'Dexterity', 0),
(6, 'Herding', 0, 50, 5, 'Int', 'Int', 0),
(6, 'Swords', 10, 50, 5, 'Strength', 'Dexterity', 0),
(6, 'Necromancy', 0, 50, 5, 'Int', 'Int', 0),
(6, 'Magery', 0, 50, 5, 'Int', 'Int', 0),
(6, 'Carpentry', 0, 50, 5, 'Int', 'Strength', 0),
(6, 'Chivalry', 0, 50, 5, 'Strength', 'Int', 0),
(6, 'Parrying', 7, 50, 5, 'Dexterity', 'Strength', 0),
(6, 'Mining', 0, 50, 5, 'Strength', 'Int', 0),
(8, 'Swords', 1, 20, 1, 'Strength', 'Strength', 0),
(8, 'Parrying', 1, 20, 1, 'Strength', 'Strength', 0),
(4, 'Taming', 0, 50, 5, 'Int', 'Strength', 0),
(5, 'Carpentry', 0, 50, 5, 'Int', 'Strength', 0),
(7, 'Taming', 0, 50, 5, 'Int', 'Strength', 0),
(7, 'Lumberjacking', 0, 50, 5, 'Strength', 'Dexterity', 0),
(7, 'Herding', 0, 50, 5, 'Int', 'Int', 0),
(7, 'Swords', 0, 50, 5, 'Strength', 'Dexterity', 0),
(7, 'Necromancy', 0, 50, 5, 'Int', 'Int', 0),
(7, 'Magery', 0, 50, 5, 'Int', 'Int', 0),
(7, 'Carpentry', 0, 50, 5, 'Int', 'Strength', 0),
(7, 'Chivalry', 0, 50, 5, 'Strength', 'Int', 0),
(7, 'Parrying', 0, 50, 5, 'Dexterity', 'Strength', 0),
(7, 'Mining', 0, 50, 5, 'Strength', 'Int', 0),
(8, 'Taming', 0, 50, 5, 'Int', 'Strength', 0),
(8, 'Lumberjacking', 0, 50, 5, 'Strength', 'Dexterity', 0),
(8, 'Chivalry', 0, 50, 5, 'Strength', 'Int', 0),
(8, 'Carpentry', 0, 50, 5, 'Int', 'Strength', 0),
(8, 'Necromancy', 0, 50, 5, 'Int', 'Int', 0),
(8, 'Mining', 0, 50, 5, 'Strength', 'Int', 0),
(8, 'Herding', 0, 50, 5, 'Int', 'Int', 0),
(8, 'Magery', 0, 50, 5, 'Int', 'Int', 0);

-- --------------------------------------------------------

--
-- Структура таблицы `stats`
--

CREATE TABLE IF NOT EXISTS `stats` (
  `entityId` int(11) NOT NULL,
  `sValue` int(11) NOT NULL,
  `mValue` int(11) NOT NULL,
  `name` text NOT NULL,
  `hardness` float NOT NULL,
  `state` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `stats`
--

INSERT INTO `stats` (`entityId`, `sValue`, `mValue`, `name`, `hardness`, `state`) VALUES
(4, 5, 50, 'Strength', 5, 0),
(4, 5, 50, 'Dexterity', 5, 0),
(4, 1, 50, 'Hits', 5, 0),
(4, 0, 20, 'Mana', 5, 0),
(4, 5, 50, 'Int', 5, 0),
(5, 5, 50, 'Strength', 5, 0),
(5, 5, 50, 'Dexterity', 5, 0),
(5, 12, 50, 'Hits', 5, 0),
(5, 20, 20, 'Mana', 5, 0),
(5, 5, 50, 'Int', 5, 0),
(6, 5, 50, 'Strength', 5, 0),
(6, 5, 50, 'Dexterity', 5, 0),
(6, 8, 50, 'Hits', 5, 0),
(6, 20, 20, 'Mana', 5, 0),
(6, 5, 50, 'Int', 5, 0),
(8, 11, 20, 'Hits', 5, 0),
(8, 1, 20, 'Strength', 1, 0),
(8, 1, 20, 'Dexterity', 1, 0),
(4, 5, 50, 'Strength', 5, 0),
(5, 5, 50, 'Strength', 5, 0),
(6, 5, 50, 'Strength', 5, 0),
(7, 5, 50, 'Strength', 5, 0),
(7, 5, 50, 'Dexterity', 5, 0),
(7, 10, 50, 'Hits', 5, 0),
(7, 5, 50, 'Int', 5, 0),
(7, 20, 20, 'Mana', 5, 0),
(8, 5, 50, 'Strength', 5, 0),
(8, 5, 50, 'Int', 5, 0),
(8, 20, 20, 'Mana', 5, 0);

-- --------------------------------------------------------

--
-- Структура таблицы `worlds`
--

CREATE TABLE IF NOT EXISTS `worlds` (
  `name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `worlds`
--

INSERT INTO `worlds` (`name`) VALUES
('Primus'),
('Hell');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
