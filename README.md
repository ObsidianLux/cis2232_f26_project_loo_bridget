# Softball League Manager
An app for team managers to simplify tracking softball games and viewing team standings.

For each game, it will keep track of which teams played, their scores, and whether the winning team won under the Mercy Rule.

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
| teamOneName       | string    | Name of Team 1.                                    |
| teamTwoName       | string    | Name of Team 2.                                    |
| teamOneScore      | int       | Team 1's score.                                    |
| teamTwoScore      | int       | Team 2's score.                                    |
| scoreDifferential | int       | The difference in score between the two teams.     |
| isMercyWin        | boolean   | Whether or not the winning team won by mercy rule. |
## Calculation
**Team Win Percentage**  = Number of Wins / (Number of Wins + Number of Losses)

**Number of Games for a Team** = Number of Wins + Number of Losses

**Game Score Differential** = Team 1 Score - Team 2 Score (and vice versa)

Count the total number of wins, losses, and mercy wins for each team.

*Additional calculations for team standings to be determined.*