package ca.hccis.softball.repository;

import ca.hccis.softball.model.entity.Game;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.ListCrudRepository;

import java.time.LocalDate;
import java.util.List;

// Repository interface.
public interface GameRepository extends ListCrudRepository<Game, Integer> {
    /**
     * Use Spring Data JPA functionality to get a list of played games.
     * @return The list of games that have been played sorted latest to earliest date.
     * @author BML
     * @since 20261028
     * @modified BML 20261111 Replaced explicit query with native Spring Data JPA functionality.
     */
    List<Game> findByDateLessThanEqualOrderByDateDesc(@NotNull(message = "The scheduled game date is required.") LocalDate date);

    /**
     * Use Spring Data JPA functionality to get a list of unplayed games.
     * @return The list of games that have not been played sorted earliest to latest date.
     * @author BML
     * @since 20261028
     * @modified BML 20261111 Replaced explicit query with native Spring Data JPA functionality.
     */
    List<Game> findByDateGreaterThanOrderByDateAsc(@NotNull(message = "The scheduled game date is required.") LocalDate date);
}
