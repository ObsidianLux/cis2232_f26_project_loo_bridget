package ca.hccis.softball.service;

import ca.hccis.softball.model.dto.GameRequest;
import ca.hccis.softball.model.entity.Game;
import ca.hccis.softball.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Holds business logic and calls repository.
 * Methods that modify data are typically marked @Transactional.
 * This keeps business rules out of controllers, makes them testable, and defines transaction boundaries so that a multistep change either fully succeeds or fully rolls back.
 */
@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /* Boilerplate Exceptions */

    private EntityNotFoundException notFound(Integer id) {
        return new EntityNotFoundException("Game not found with id: " + id);
    }

    /* READ */

    public List<GameRequest> getAllGames() { return gameRepository.findAll().stream().map(GameRequest::from).toList(); }

    public List<GameRequest> getPlayedGames(LocalDate date) {
        return gameRepository.findByDateLessThanEqualOrderByDateDesc(date).stream().map(GameRequest::from).toList();
    }

    public List<GameRequest> getUnplayedGames(LocalDate date) {
        return gameRepository.findByDateGreaterThanOrderByDateAsc(date).stream().map(GameRequest::from).toList();
    }

    public GameRequest getGameById(Integer id) {
        // If the id is null or there is no game with the same id, throw an exception that the @RestControllerAdvice class can catch and handle.
        Game game = gameRepository.findById(id).orElseThrow(() -> notFound(id));
        return GameRequest.from(game);
    }

    public boolean existsById(Integer id) {
        return id != null && gameRepository.existsById(id);
    }

    /* UPDATE */

    @Transactional
    public GameRequest updateGame(Integer id, GameRequest dto) {
        // Get the existing database entity.
        Game game = gameRepository.findById(id).orElseThrow(() -> notFound(id));

        // Update the game fields using the DTO.
        Game mappedGame = dto.toEntity();

        game.setHomeTeam(mappedGame.getHomeTeam());
        game.setAwayTeam(mappedGame.getAwayTeam());
        game.setDate(mappedGame.getDate());
        game.setWasForfeited(mappedGame.getWasForfeited());
        game.setTeamAtFault(mappedGame.getTeamAtFault());
        game.setInnings(mappedGame.getInnings());

        // Save the updated game.
        return GameRequest.from(gameRepository.save(game));
    }

    /* INSERT */

    @Transactional
    public GameRequest insertGame(Game game) {
        return GameRequest.from(gameRepository.save(game));
    }

    /* DELETE */

    @Transactional
    public void deleteGame(Integer id) {
        // If the id is null or there is no game with the same id, throw an exception that the @RestControllerAdvice class can catch and handle.
        if (!existsById(id)) throw notFound(id);
        gameRepository.deleteById(id);
    }
}
