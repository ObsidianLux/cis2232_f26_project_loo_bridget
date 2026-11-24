package ca.hccis.softball.model.dto;

import ca.hccis.softball.model.domain.Inning;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO used to send and receive data from the client.
 * @param homeRuns The number of runs scored by the home team
 * @param awayRuns The number of runs scored by the away team
 */
public record InningRequest(
        @PositiveOrZero(message = "The number of runs for the home team cannot be less than 0.")
        int homeRuns,

        @PositiveOrZero(message = "The number of runs for the away team cannot be less than 0.")
        int awayRuns
) {
        /**
         * Convert an Inning to an InningRequest.
         * @param inning The Inning to convert
         * @return InningRequest
         */
        public static InningRequest from(Inning inning) {
                return new InningRequest(
                        inning.getHomeRuns(),
                        inning.getAwayRuns()
                );
        }

        /**
         * Convert an InningRequest to an Inning.
         * @return Inning
         */
        public Inning toEntity() {
                return new Inning(
                        homeRuns,
                        awayRuns
                );
        }
}
