# Softball League Manager
An app for team managers to simplify tracking softball games and viewing team standings.

For future games, it will keep track of which teams will play and on which date.

For past games, it will keep track of which teams played, the date they played, who won and whether it was by forfeit or mercy rule, total scores, and innings with each team's number of runs scored.

Team managers will be able to see each team's overall standing based on the records of each game provided. They will also be able to see which games are upcoming, which ones have been played, and which ones should have been played but were not updated in the app yet.
## Development Team
**BA / Business Client:** Richard Baird

**Developer:** Bridget Loo

**Project Manager / QA:** Shabnam Rohani
## Colours
**Cloud Blue**: #EDF4FA

**Powder Sky**: #CFE3F1

**Calm Ocean**: #8FB6D8

**Dusty Denim**: #5F86A6

**Midnight Blue**: #243A5E
## Fields
| Name              | Data Type | Description                                        |
| ----------------- | --------- | -------------------------------------------------- |
| id            | int       | Unique id for the game.                  |
| homeTeam          | string    | Name of the Home team.                             |
| roadTeam          | string    | Name of the Road team.                             |
| date          | string    | The date on which the game is scheduled to be played.                             |
| wasForfeited         | boolean   | Whether the game was forfeited or not.             |
| wasMercyWin        | boolean   | Whether or not the winning team won by mercy rule. |
| homeScore          | string    | The final score for the home team.                             |
| roadScore         | string    | The final score for the road team.                             |
| innings            | json       | A record of all the innings in the game.                  |
| played            | int       | Whether the game has been played yet or not.                  |
## Calculation
**Winner of a Game**  = The greater of (total runs for the home team) vs. (total runs for the road team)

## Report
**Standings**

On the home page, teams are displayed in order of their standings within the league (sorted in descending order). Displays the number of games played, current standing, number of wins and losses, games behind rating, records for wins and losses at home and on the road, and the record of wins and losses for the last 5 games played.

Upcoming and past games are also displayed with basic information about them. All games provide the option to update or delete them.

**Team History Report**

Select a team and get a list of all the games that that team played in (excluding scheduled games that have not happened yet).

A chart displays the team's score compared to their opponent's score for each game.

Each game is displayed in a card with the outcome, the opponent, the number of innings, the plus/minus value, and the final scores.