package ca.hccis.softball.repositories;

import ca.hccis.softball.jpa.entity.Game;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends CrudRepository<Game, Integer> {
    /**
     * Use Spring Data JPA functionality to locate a list that has the string which was passed as a parameter.
     * @param team The name of the team.
     * @return The list of games related to the team.
     * @author BML
     * @since 20261016
     */
    @Query("SELECT g FROM Game g WHERE g.homeTeam LIKE %:team% OR g.roadTeam LIKE %:team%")
    List<Game> findByTeamNameContaining(@Param("team") String team);
}
