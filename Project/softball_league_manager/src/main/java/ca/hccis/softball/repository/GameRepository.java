package ca.hccis.softball.repository;

import ca.hccis.softball.model.entity.Game;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

// Repository interface.
public interface GameRepository extends CrudRepository<Game, Integer> {
    /**
     * Use Spring Data JPA functionality to get a list of played games.
     * @return The list of games that have been played sorted latest to earliest date.
     * @author BML
     * @since 20261028
     * @modified BML 20261111 Replaced explicit query with native Spring Data JPA functionality.
     */
    List<Game> findByPlayedTrueOrderByDateDesc();

    /**
     * Use Spring Data JPA functionality to get a list of unplayed games.
     * @return The list of games that have not been played sorted earliest to latest date.
     * @author BML
     * @since 20261028
     * @modified BML 20261111 Replaced explicit query with native Spring Data JPA functionality.
     */
    List<Game> findByPlayedFalseOrderByDateAsc();
}
