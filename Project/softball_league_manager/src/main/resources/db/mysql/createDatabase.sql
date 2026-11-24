DROP DATABASE IF EXISTS cis2232_softball_league;
CREATE DATABASE cis2232_softball_league;
USE cis2232_softball_league;

DROP TABLE IF EXISTS SoftballGame;
CREATE TABLE SoftballGame
(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique id for the game',
    homeTeam varchar(30) NOT NULL COMMENT 'The Home team''s name',
    awayTeam varchar(30) NOT NULL COMMENT 'The Away team''s name',
    date DATE NOT NULL COMMENT 'The date on which the game is scheduled to be played',
    wasForfeited boolean COMMENT 'Whether the game was forfeited by one of the teams',
    teamAtFault varchar(30) COMMENT 'The team that forfeited the game if it applies',
    innings json COMMENT 'A record of all the innings in the game'
) COMMENT 'This table stores details about each softball game in the league.';

INSERT INTO SoftballGame
(id, homeTeam, awayTeam, date, wasForfeited, teamAtFault,
 innings)
VALUES
    (
        1, 'The Umpire Strikes Back', 'Bat Intentions', '2026-10-21',false, null,
        '[
          {"homeRuns": 1, "awayRuns": 0},
          {"homeRuns": 0, "awayRuns": 2},
          {"homeRuns": 2, "awayRuns": 0},
          {"homeRuns": 0, "awayRuns": 1},
          {"homeRuns": 1, "awayRuns": 0},
          {"homeRuns": 0, "awayRuns": 0},
          {"homeRuns": 1, "awayRuns": 0}
        ]'
    ),
    (
        2, 'The Umpire Strikes Back', 'Hit Happens', '2026-10-22', false, null,
        '[
          {"homeRuns": 0, "awayRuns": 1},
          {"homeRuns": 2, "awayRuns": 0},
          {"homeRuns": 0, "awayRuns": 2},
          {"homeRuns": 1, "awayRuns": 0},
          {"homeRuns": 0, "awayRuns": 1},
          {"homeRuns": 1, "awayRuns": 2},
          {"homeRuns": 0, "awayRuns": 0}
        ]'
    ),
    (
        3, 'Hit Happens', 'Bat Intentions', '2026-10-23', false, null,
        '[
          {"homeRuns": 0, "awayRuns": 2},
          {"homeRuns": 1, "awayRuns": 3},
          {"homeRuns": 0, "awayRuns": 1},
          {"homeRuns": 2, "awayRuns": 3},
          {"homeRuns": 0, "awayRuns": 4}
        ]'
    ),
    (
        4, 'Scared Hitless', 'The Umpire Strikes Back', '2026-10-24', false, null,
        '[
          {"homeRuns": 1, "awayRuns": 0},
          {"homeRuns": 0, "awayRuns": 1},
          {"homeRuns": 2, "awayRuns": 0},
          {"homeRuns": 0, "awayRuns": 2},
          {"homeRuns": 1, "awayRuns": 0},
          {"homeRuns": 0, "awayRuns": 1},
          {"homeRuns": 0, "awayRuns": 1}
        ]'
    ),
    (
        5, 'Bat Intentions', 'Scared Hitless', '2026-10-25', true, 'Scared Hitless', null
    );

INSERT INTO SoftballGame (id, homeTeam, awayTeam, date)
VALUES (6, 'Scared Hitless', 'Hit Happens' , '2027-10-26'), (7, 'Base-ically the Best', 'The Umpire Strikes Back', '2027-10-27');