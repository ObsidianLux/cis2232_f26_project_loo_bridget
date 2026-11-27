package ca.hccis.softball.graphql;

import ca.hccis.softball.model.dto.GameRequest;
import ca.hccis.softball.service.GameService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller class that makes query methods available to Postman.
 * Directly tied to the GraphQL schema.
 * @QueryMapping is for type Query and @MutationMapping is for type Mutation.
 * @Argument goes on query and mutation parameters.
 */
@Controller
public class GameGraphQLController {
    private final GameService service;

    public GameGraphQLController(GameService service) {
        this.service = service;
    }

    @QueryMapping
    public List<GameRequest> getAllGames() {
        return service.getAllGames();
    }

    @QueryMapping
    public List<GameRequest> getUpcomingGames() {
        return service.getUnplayedGames(LocalDate.now());
    }

    @QueryMapping
    public List<GameRequest> getPlayedGames() {
        return service.getPlayedGames(LocalDate.now());
    }

    @QueryMapping
    public GameRequest getGameById(@Argument Integer id) {
        return service.getGameById(id);
    }

    @MutationMapping
    public GameRequest updateGame(@Argument Integer id, @Argument GameRequest input) {
        return service.updateGame(id, input);
    }

    @MutationMapping
    public Boolean deleteGame(@Argument Integer id) {
        if (service.existsById(id)) {
            service.deleteGame(id);
            return true;
        } else return false;
    }

    @MutationMapping
    public GameRequest createGame(@Argument GameRequest input) {
        return service.insertGame(input.toEntity());
    }
}
