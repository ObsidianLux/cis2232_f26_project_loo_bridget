DROP DATABASE IF EXISTS cis2232_softball_league;
CREATE DATABASE cis2232_softball_league;
USE cis2232_softball_league;

DROP TABLE IF EXISTS SoftballGame;
CREATE TABLE SoftballGame
(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique id for the game',
    homeTeam varchar(20) NOT NULL COMMENT 'The Home team''s name',
    roadTeam varchar(20) NOT NULL COMMENT 'The Road team''s name',
    date DATE NOT NULL COMMENT 'The date on which the game is scheduled to be played',
    wasForfeited boolean COMMENT 'Whether the game was forfeited by one of the teams',
    wasMercyWin boolean COMMENT 'Whether the game was won because of the mercy rule',
    homeScore int COMMENT 'The final score for the home team',
    roadScore int COMMENT 'The final score for the road team',
    innings json COMMENT 'A record of all the innings in the game',
    played boolean DEFAULT FALSE COMMENT 'Whether the game has been played yet or not'
) COMMENT 'This table stores details about each softball game in the league.';

INSERT INTO SoftballGame
(id, homeTeam, roadTeam, date, wasForfeited,
 wasMercyWin, homeScore, roadScore, played, innings)
VALUES
    (
        1, 'Apples', 'Oranges', '2026-10-21',false, false, 5, 3, true,
        '[
          {"inningNumber": 1, "homeRuns": 1, "roadRuns": 0},
          {"inningNumber": 2, "homeRuns": 0, "roadRuns": 2},
          {"inningNumber": 3, "homeRuns": 2, "roadRuns": 0},
          {"inningNumber": 4, "homeRuns": 0, "roadRuns": 1},
          {"inningNumber": 5, "homeRuns": 1, "roadRuns": 0},
          {"inningNumber": 6, "homeRuns": 0, "roadRuns": 0},
          {"inningNumber": 7, "homeRuns": 1, "roadRuns": 0}
        ]'
    ),
    (
        2, 'Apples', 'Bananas', '2026-10-22', false, false, 4, 6, true,
        '[
          {"inningNumber": 1, "homeRuns": 0, "roadRuns": 1},
          {"inningNumber": 2, "homeRuns": 2, "roadRuns": 0},
          {"inningNumber": 3, "homeRuns": 0, "roadRuns": 2},
          {"inningNumber": 4, "homeRuns": 1, "roadRuns": 0},
          {"inningNumber": 5, "homeRuns": 0, "roadRuns": 1},
          {"inningNumber": 6, "homeRuns": 1, "roadRuns": 2},
          {"inningNumber": 7, "homeRuns": 0, "roadRuns": 0}
        ]'
    ),
    (
        3, 'Bananas', 'Oranges', '2026-10-23', false, true, 3, 12, true,
        '[
          {"inningNumber": 1, "homeRuns": 0, "roadRuns": 2},
          {"inningNumber": 2, "homeRuns": 1, "roadRuns": 3},
          {"inningNumber": 3, "homeRuns": 0, "roadRuns": 1},
          {"inningNumber": 4, "homeRuns": 2, "roadRuns": 2},
          {"inningNumber": 5, "homeRuns": 0, "roadRuns": 4}
        ]'
    ),
    (
        4, 'Grapes', 'Apples', '2026-10-24', false, false, 4, 5, true,
        '[
          {"inningNumber": 1, "homeRuns": 1, "roadRuns": 0},
          {"inningNumber": 2, "homeRuns": 0, "roadRuns": 1},
          {"inningNumber": 3, "homeRuns": 2, "roadRuns": 0},
          {"inningNumber": 4, "homeRuns": 0, "roadRuns": 2},
          {"inningNumber": 5, "homeRuns": 1, "roadRuns": 0},
          {"inningNumber": 6, "homeRuns": 0, "roadRuns": 1},
          {"inningNumber": 7, "homeRuns": 0, "roadRuns": 1}
        ]'
    ),
    (
        5, 'Oranges', 'Grapes', '2026-10-25', true, false, 0, 7, true, NULL
    );

INSERT INTO SoftballGame (id, homeTeam, roadTeam, date)
VALUES (6, 'Grapes', 'Bananas' , '2026-10-26'), (7, 'Grapefruit', 'Apples', '2026-10-27');