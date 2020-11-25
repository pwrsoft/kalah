/*
 * Copyright (c) 2020 Volodymyr Protsaylo
 *                            
 *                               Licensed under the Apache License, Version 2.0 (the "License");
 *                               you may not use this file except in compliance with the License.
 *                               You may obtain a copy of the License at
 *                            
 *                                 http://www.apache.org/licenses/LICENSE-2.0
 *                            
 *                               Unless required by applicable law or agreed to in writing, software
 *                               distributed under the License is distributed on an "AS IS" BASIS,
 *                               WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *                               See the License for the specific language governing permissions and
 *                               limitations under the License.
 */

package com.pwr.kalah;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Objects;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Kalah game Integration Tests
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Kalah game Integration Tests")
public class KalahIIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should Return CREATED Response when request sent to /games endpoint")
    public void shouldReturnCreatedOnGamesEndpoints() throws Exception {
        // Create first game and save this game ID
        Long firstGameId = createOneGame();
        // Create second game and verify it's ID
        checkResultActionsNewGame()
                .andExpect(jsonPath("$.id", is(String.valueOf(firstGameId.intValue()+1))));
    }

    @Test
    @DisplayName("Should be able to Make some moves")
    public void shouldBeAbleToMakeMoves() throws Exception {
        // Create first game
        Long firstGameId = createOneGame();
        // Make move in first game
        checkResultActionsMakeMove(firstGameId);
        // Create second game
        Long secondGameId = createOneGame();
        // Make move in second game
        checkResultActionsMakeMove(secondGameId);
    }

    @Test
    @DisplayName("Test non-existing game number")
    public void testNonExistingGameNumber() throws Exception {
        mockMvc.perform(put("/games/99999999/pits/1"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof KalahException))
                .andExpect(result -> assertEquals(ErrorMessages.INVALID_GAME_NUMBER, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("Test invalid game ID and pit number")
    public void testInvalidGameIdAndPitNumber() throws Exception {
        createOneGame();
        mockMvc.perform(put("/games/1/pits/0"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ErrorMessages.NON_NUMERIC_VALUE));
        mockMvc.perform(put("/games/1/pits/a"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ErrorMessages.NON_NUMERIC_VALUE));
        mockMvc.perform(put("/games/a/pits/a"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ErrorMessages.NON_NUMERIC_VALUE));
        mockMvc.perform(put("/games/1/pits/1.1"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ErrorMessages.NON_NUMERIC_VALUE));    }

    /**
     * This helper creates the new game and return it's {@link ResultActions}
     * It verifies the correct response HTTP status (CREATED)
     *
     * @return results of game creation
     * @throws Exception exception
     */
    private ResultActions checkResultActionsNewGame() throws Exception {
        return mockMvc.perform(post("/games"))
                .andExpect(status().isCreated());
    }

    /**
     * This helper make a game move and return it's {@link ResultActions}
     * Also it verifies the response HTTP and game status as well as game ID
     *
     * @throws Exception exception
     */
    private void checkResultActionsMakeMove(Long gameId) throws Exception {
        mockMvc.perform(put("/games/" + gameId + "/pits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(String.valueOf(gameId))))
                .andExpect((jsonPath("$.status",
                        aMapWithSize(14))));
    }

    /**
     * This helper creates one game and returns it's game ID
     *
     * @return game ID
     */
    private Long createOneGame() {
        try {
            MvcResult result = checkResultActionsNewGame()
                    .andReturn();
            KalahGame game = new ObjectMapper()
                    .readValue(result.getResponse().getContentAsString(), KalahGame.class);
            return game.getGameId();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Request to Create Game", e);
        }
    }
}
