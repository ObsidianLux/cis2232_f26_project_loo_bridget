package ca.hccis.softball.controller;

import ca.hccis.softball.model.dto.GameRequest;
import ca.hccis.softball.model.entity.Game;
import ca.hccis.softball.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

/**
 * TODO SPRINT 4 - REST API
 */
@Tag(name = "Games", description = "Operations for managing softball games.")
@RestController
@RequestMapping("/apisecure/game")
public class GameRestController {
    private final GameService service;

    public GameRestController(GameService service) {
        this.service = service;
    }

    /**
     * TODO SPRINT 4 - Service to get one.
     * @param id The id of the game to retrieve
     * @return Game DTO
     */
    @Operation(summary = "Get a game", description = "Retrieve a game with the given id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game with given id retrieved",  content = @Content(schema = @Schema(implementation = GameRequest.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Game not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Database error", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<GameRequest> getGameById(@PathVariable @Parameter(description = "Game id", example = "5") Integer id) {
        // Throws if there is no game with the provided id.
        return ResponseEntity.ok(service.getGameById(id));
    }

    /**
     * TODO SPRINT 4 - Service to get all
     * @return List of Game DTO
     */
    @Operation(summary = "Get all games", description = "Retrieve all games in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of games",  content = @Content(array = @ArraySchema(schema = @Schema(implementation = GameRequest.class)))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content),
            @ApiResponse(responseCode = "500", description = "Database error", content = @Content)
    })
    @GetMapping("/get")
    public ResponseEntity<List<GameRequest>> getAllGames() {
        // Get a list of all games using the service and convert to DTOs.
        return ResponseEntity.ok(service.getAllGames());
    }

    /**
     * TODO SPRINT 4 - Service to delete
     * @param id The id of the game to delete
     */
    @Operation(summary = "Delete a game", description = "Permanently deletes the game with the given id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Game deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Game not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Database error", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable @Parameter(description = "Game id", example = "5") Integer id) {
        // Throws if the game is missing.
        service.deleteGame(id);
        // Returns 204 response.
        return ResponseEntity.noContent().build();
    }

    /**
     * TODO SPRINT 4 - Service to add
     * @param dto DTO with new game data
     * @return New Game DTO
     */
    @Operation(summary = "Add a game", description = "Creates a new game and returns it.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Game created", content = @Content(schema = @Schema(implementation = GameRequest.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content),
            @ApiResponse(responseCode = "500", description = "Database error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<GameRequest> addGame(@Valid @RequestBody GameRequest dto) {
        GameRequest game = service.insertGame(dto.toEntity());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(game.id()).toUri()).body(game);
    }

    /**
     * TODO SPRINT 4 - Service to update
     * @param dto DTO with updated game data
     * @return Updated Game DTO
     */
    @Operation(summary = "Update a game", description = "Update a game with a given id and new data.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game updated", content = @Content(schema = @Schema(implementation = GameRequest.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Game not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Database error", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<GameRequest> updateGame(@PathVariable @Parameter(description = "Game id", example = "5") Integer id, @Valid @RequestBody GameRequest dto) {
        return ResponseEntity.ok(service.updateGame(id, dto));
    }
}