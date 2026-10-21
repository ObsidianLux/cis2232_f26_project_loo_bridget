DROP DATABASE IF EXISTS cis2232_softball_league;
CREATE DATABASE cis2232_softball_league;
USE cis2232_softball_league;

DROP TABLE IF EXISTS SoftballGame;
CREATE TABLE SoftballGame
(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique id for the game',
    homeTeam varchar(20) NOT NULL COMMENT 'The Home team''s name',
    roadTeam varchar(20) NOT NULL COMMENT 'The Road team''s name',
    forfeited boolean NOT NULL DEFAULT FALSE COMMENT 'Whether the game was forfeited by one of the teams',
    winningTeam varchar(20) NOT NULL COMMENT 'The team that won the game',
    isMercyWin boolean NOT NULL DEFAULT FALSE COMMENT 'Whether the game was won because of the mercy rule',
    scoreDifferential int NOT NULL COMMENT 'The difference between the two scores',
    innings json COMMENT 'A record of all the innings in the game'
) COMMENT 'This table stores details about each softball game in the league.';

INSERT INTO SoftballGame
(id, homeTeam, roadTeam, forfeited, winningTeam,
 isMercyWin, scoreDifferential, innings)
VALUES
    (
        1, 'Apples', 'Oranges', false, 'Apples', false, 2,
        '[
          {"num": 1, "homeRuns": 1, "roadRuns": 0},
          {"num": 2, "homeRuns": 0, "roadRuns": 2},
          {"num": 3, "homeRuns": 2, "roadRuns": 0},
          {"num": 4, "homeRuns": 0, "roadRuns": 1},
          {"num": 5, "homeRuns": 1, "roadRuns": 0},
          {"num": 6, "homeRuns": 0, "roadRuns": 0},
          {"num": 7, "homeRuns": 1, "roadRuns": 0}
        ]'
    ),
    (
        2, 'Apples', 'Bananas', false, 'Bananas', false, 2,
        '[
          {"num": 1, "homeRuns": 0, "roadRuns": 1},
          {"num": 2, "homeRuns": 2, "roadRuns": 0},
          {"num": 3, "homeRuns": 0, "roadRuns": 2},
          {"num": 4, "homeRuns": 1, "roadRuns": 0},
          {"num": 5, "homeRuns": 0, "roadRuns": 1},
          {"num": 6, "homeRuns": 1, "roadRuns": 2},
          {"num": 7, "homeRuns": 0, "roadRuns": 0}
        ]'
    ),
    (
        3, 'Bananas', 'Oranges', false, 'Oranges', true, 9,
        '[
          {"num": 1, "homeRuns": 0, "roadRuns": 2},
          {"num": 2, "homeRuns": 1, "roadRuns": 3},
          {"num": 3, "homeRuns": 0, "roadRuns": 1},
          {"num": 4, "homeRuns": 2, "roadRuns": 2},
          {"num": 5, "homeRuns": 0, "roadRuns": 4}
        ]'
    ),
    (
        4, 'Grapes', 'Apples', false, 'Apples', false, 1,
        '[
          {"num": 1, "homeRuns": 1, "roadRuns": 0},
          {"num": 2, "homeRuns": 0, "roadRuns": 1},
          {"num": 3, "homeRuns": 2, "roadRuns": 0},
          {"num": 4, "homeRuns": 0, "roadRuns": 2},
          {"num": 5, "homeRuns": 1, "roadRuns": 0},
          {"num": 6, "homeRuns": 0, "roadRuns": 1},
          {"num": 7, "homeRuns": 0, "roadRuns": 1}
        ]'
    ),
    (
        5, 'Oranges', 'Grapes', true, 'Oranges', false, 7, NULL
    );