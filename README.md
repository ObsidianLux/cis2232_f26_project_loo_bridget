# Softball League Manager
An app for team managers to simplify tracking softball games and viewing team standings.

For each game, it will keep track of which teams played, whether they were forfeited or won under the Mercy Rule, and which team won.

Team managers will be able to see each team's overall standing based on the records of each game provided.
## Development Team
**BA / Business Client:** Richard Baird

**Developer:** Bridget Loo

**Project Manager / QA:** Shabnam Rohani
## Colour
**Primary:** Sky Blue
## Fields
| Name              | Data Type | Description                                        |
| ----------------- | --------- | -------------------------------------------------- |
| gameId            | int       | The unique identifier for a game.                  |
| homeTeam          | string    | Name of the Home team.                             |
| roadTeam          | string    | Name of the Road team.                             |
| forfeited         | boolean   | Whether the game was forfeited or not.             |
| teamAtFault       | string    | The team that forfeited the game.                  |
| winningTeam       | string    | The team that won the game.                        |
| isMercyWin        | boolean   | Whether or not the winning team won by mercy rule. |
## Calculation
**Winner of a Game**  = The greater of (total runs for the home team) vs. (total runs for the road team)

## Report
All tracked games will be analyzed and unique teams will be identified. The number of wins and losses will be tallied for each team and their individual standings will be calculated from those numbers. Each team will then be display to the user in descending order based on their standing.