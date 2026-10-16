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
## Reports
### Team Standings Report
View a list of all teams in the order of their standing within the league. 
Analyzes unique teams to tally the number of wins and losses, and calculates their standing from those values. 
### Record of Games Report
View all games that have been tracked, including which teams played and how they scored.
### Games by Team Report
Select a team to view a list of all the games they played and what the outcomes of those games were.
Uses the final scores for each game to calculate the plus/minus for the team the resulted from each game.