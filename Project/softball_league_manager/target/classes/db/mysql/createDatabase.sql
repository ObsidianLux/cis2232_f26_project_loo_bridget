DROP DATABASE IF EXISTS cis2232_softball_league;
CREATE DATABASE cis2232_softball_league;
USE cis2232_softball_league;

CREATE TABLE SoftballGame
(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique id for the game',
    homeTeam varchar(20) NOT NULL COMMENT 'The Home team''s name',
    roadTeam varchar(20) NOT NULL COMMENT 'The Road team''s name',
    forfeited boolean NOT NULL DEFAULT FALSE COMMENT 'Whether the game was forfeited by one of the teams',
    teamAtFault varchar(20) COMMENT 'If the game was forfeited, indicates which team was at fault',
    winningTeam varchar(20) NOT NULL COMMENT 'The team that won the game',
    isMercyWin boolean NOT NULL DEFAULT FALSE COMMENT 'Whether the game was won because of the mercy rule'
) COMMENT 'This table stores details about each softball game in the league.';

INSERT INTO SoftballGame (id, homeTeam, roadTeam, forfeited, teamAtFault, winningTeam, isMercyWin)
VALUES (1, 'Apples', 'Oranges', false, null, 'Apples', false),
       (2, 'Apples', 'Bananas', false, null, 'Bananas', false),
       (3, 'Bananas', 'Oranges', false, null, 'Oranges', true),
       (4, 'Grapes', 'Apples', false, null, 'Apples', false),
       (5, 'Oranges', 'Grapes', true, 'Grapes', 'Oranges', false);