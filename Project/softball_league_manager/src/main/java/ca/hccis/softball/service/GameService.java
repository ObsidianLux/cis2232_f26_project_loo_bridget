package ca.hccis.softball.service;

import ca.hccis.softball.model.dto.GameDTO;
import ca.hccis.softball.model.entity.Game;
import ca.hccis.softball.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /* SELECT */

    public List<Game> getPlayedGames() {
        return gameRepository.findByPlayedTrueOrderByDateDesc();
    }

    public List<Game> getTop5PlayedGames() {
        return gameRepository.findTop5ByPlayedTrueOrderByDateDesc();
    }

    public List<Game> getUnplayedGames() {
        return gameRepository.findByPlayedFalseOrderByDateAsc();
    }

    public List<Game> getTop5UnplayedGames() {
        return gameRepository.findTop5ByPlayedFalseOrderByDateAsc();
    }

    public Game getGameById(int id) {
        return gameRepository.findById(id).orElse(null);
    }

    /* UPDATE */

    @Transactional
    public Game updateGame(Integer id, GameDTO gameDTO) {
        // Get the existing database entity.
        Game game = gameRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + id));

        // Update the game fields using the DTO.
        Game mappedGame = gameDTO.getMappedGame();

        game.setHomeTeam(mappedGame.getHomeTeam());
        game.setRoadTeam(mappedGame.getRoadTeam());
        game.setDate(mappedGame.getDate());
        game.setPlayed(mappedGame.getPlayed());
        game.setWasMercyWin(mappedGame.getWasMercyWin());
        game.setWasForfeited(mappedGame.getWasForfeited());
        game.setInnings(mappedGame.getInnings());
        game.setHomeScore(mappedGame.getHomeScore());
        game.setRoadScore(mappedGame.getRoadScore());

        // Save the updated game.
        gameRepository.save(game);

        // Return the updated game.
        return game;
    }

    /* INSERT */

    @Transactional
    public void insertGame(Game game) {
        gameRepository.save(game);
    }

    /* DELETE */

    @Transactional
    public void deleteGame(Integer id) {
        gameRepository.deleteById(id);
    }
}
