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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Kalah game Unit Tests
 */
@DisplayName("Kalah game Unit Tests")
class KalahUnitTests {

    private final KalahGame game = new KalahGame(1L);

    @BeforeEach
    void setUp() {
        game.initGameField();
    }

    @Test
    void testGameFirstPlayer() {
        assertEquals(1, game.getCurrentPlayer(), "The game starts with player number 1");
    }

    @Test
    void testChangingPlayers() {
        assertEquals(2, game.changePlayer(), "When game starts and after the first move the player number should be 2 ");
        assertEquals(1, game.changePlayer(), "After the next move the player number should be 1 again ");
    }

    @Test
    void testInitGameField() {
        assertEquals("{\"1\":\"6\",\"2\":\"6\",\"3\":\"6\",\"4\":\"6\",\"5\":\"6\",\"6\":\"6\",\"7\":\"0\",\"8\":\"6\",\"9\":\"6\",\"10\":\"6\",\"11\":\"6\",\"12\":\"6\",\"13\":\"6\",\"14\":\"0\"}", game.toString(), "Invalid initial game field");
    }

    @Test
    void testSetAndGetCurrentPlayer() {
        game.setCurrentPlayer(1);
        assertEquals(1, game.getCurrentPlayer(), "Current player should be 1");
        game.setCurrentPlayer(2);
        assertEquals(2, game.getCurrentPlayer(), "Current player should be 2");
    }

    @Test
    void testThePitBelongsTheCurrentPlayer() {
        game.setCurrentPlayer(1);
        assertTrue(game.isPitMine(1), "Pit 1 owner is invalid");
        game.setCurrentPlayer(2);
        assertTrue(game.isPitMine(8), "Pit 8 owner is invalid");
    }

    @Test
    void testKalahPits() {
        game.setCurrentPlayer(1);
        assertTrue(game.isPitMineKalah(7), "Pit 7 is Kalah of player 1");
        assertTrue(game.isPitKalah(7), "Pit 7 is Kalah");
        game.setCurrentPlayer(2);
        assertTrue(game.isPitMineKalah(14), "Pit 14 is Kalah of player 2");
        assertTrue(game.isPitKalah(14), "Pit 14 is Kalah");
    }

    @Test
    void testGetSetAddPitStones() {
        assertEquals(6, game.getPitStones(1), "Invalid number of stones in pit 1");
        assertEquals(6, game.getPitStones(13), "Invalid number of stones in pit 13");
        assertEquals(0, game.getPitStones(7), "Invalid number of stones in pit 7");
        assertEquals(0, game.getPitStones(14), "Invalid number of stones in pit 14");
        game.setPitStones(1, 5);
        assertEquals(5, game.getPitStones(1), "Invalid number of stones in pit 1 after setting number of stones to 5");
        game.addPitStones(1, 2);
        assertEquals(7, game.getPitStones(1), "Invalid number of stones in pit 1 after adding 1 stone");
    }

    @Test
    void testSimpleGame() {
        game.makeNextMove(2);
        assertEquals("{\"1\":\"6\",\"2\":\"0\",\"3\":\"7\",\"4\":\"7\",\"5\":\"7\",\"6\":\"7\",\"7\":\"1\",\"8\":\"7\",\"9\":\"6\",\"10\":\"6\",\"11\":\"6\",\"12\":\"6\",\"13\":\"6\",\"14\":\"0\"}", game.toString(), "Invalid game field after step 1");
        game.makeNextMove(10);
        assertEquals("{\"1\":\"7\",\"2\":\"1\",\"3\":\"7\",\"4\":\"7\",\"5\":\"7\",\"6\":\"7\",\"7\":\"1\",\"8\":\"7\",\"9\":\"6\",\"10\":\"0\",\"11\":\"7\",\"12\":\"7\",\"13\":\"7\",\"14\":\"1\"}", game.toString(), "Invalid game field after step 2");
    }

    @Test
    void testCaptureStones() {
        game.fillGameFieldWithSample(new int[]{0, 0, 0, 12, 0, 0, 0, 0, 0, 5, 3, 0, 0, 0});
        game.makeNextMove(4);
        assertEquals("{\"1\":\"1\",\"2\":\"1\",\"3\":\"0\",\"4\":\"0\",\"5\":\"1\",\"6\":\"1\",\"7\":\"6\",\"8\":\"1\",\"9\":\"1\",\"10\":\"6\",\"11\":\"0\",\"12\":\"1\",\"13\":\"1\",\"14\":\"0\"}", game.toString(), "Invalid game field after step 2");

    }

    @Test
    void testPlayersSequentialMoves() {
        game.fillGameFieldWithSample(new int[]{0, 0, 1, 3, 2, 0, 0, 0, 0, 5, 3, 0, 0, 0});
        game.makeNextMove(5);
        assertEquals("{\"1\":\"0\",\"2\":\"0\",\"3\":\"1\",\"4\":\"3\",\"5\":\"0\",\"6\":\"1\",\"7\":\"1\",\"8\":\"0\",\"9\":\"0\",\"10\":\"5\",\"11\":\"3\",\"12\":\"0\",\"13\":\"0\",\"14\":\"0\"}", game.toString(), "Invalid game field after step 2");
        assertEquals(1, game.getCurrentPlayer(), "Player 1 should have another turn");
        game.makeNextMove(4);
        assertEquals("{\"1\":\"0\",\"2\":\"0\",\"3\":\"1\",\"4\":\"0\",\"5\":\"1\",\"6\":\"2\",\"7\":\"2\",\"8\":\"0\",\"9\":\"0\",\"10\":\"5\",\"11\":\"3\",\"12\":\"0\",\"13\":\"0\",\"14\":\"0\"}", game.toString(), "Invalid game field after step 2");
        assertEquals(1, game.getCurrentPlayer(), "Player 1 should have another turn again");
        game.makeNextMove(3);
        assertEquals("{\"1\":\"0\",\"2\":\"0\",\"3\":\"0\",\"4\":\"0\",\"5\":\"1\",\"6\":\"2\",\"7\":\"8\",\"8\":\"0\",\"9\":\"0\",\"10\":\"0\",\"11\":\"3\",\"12\":\"0\",\"13\":\"0\",\"14\":\"0\"}", game.toString(), "Invalid game field after step 2");
        assertEquals(2, game.getCurrentPlayer(), "Now it's turn for Player 2");
    }

    @Test
    void testEndGameAndScoring_GAME_OVER() {
        Exception exception;
        // New game, test non-repetitive
        game.fillGameFieldWithSample(new int[]{0, 0, 0, 0, 1, 0, 19, 0, 0, 0, 0, 0, 0, 10});
        exception = assertThrows(
                KalahException.class,
                () -> game.makeNextMove(5));
        assertEquals(String.format(ErrorMessages.GAME_OVER, 20, 10), exception.getMessage());

        // New game, test repetitive move
        game.setCurrentPlayer(1);
        game.fillGameFieldWithSample(new int[]{0, 0, 0, 0, 0, 1, 19, 0, 0, 0, 0, 0, 0, 10});
        exception = assertThrows(
                KalahException.class,
                () -> game.makeNextMove(6));
        assertEquals(String.format(ErrorMessages.GAME_OVER, 20, 10), exception.getMessage());

        // New game, make first player's move
        // test second player move
        game.setCurrentPlayer(2);
        game.fillGameFieldWithSample(new int[]{0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 1, 9});
        exception = assertThrows(
                KalahException.class,
                () -> game.makeNextMove(13));
        assertEquals(String.format(ErrorMessages.GAME_OVER, 20, 10), exception.getMessage());

    }

    @Test
    void testCountPlayersStones() {
        game.makeNextMove(1);
        assertEquals(35, game.countPlayerStones(1, false), "Counting pit stones of player 1");
        assertEquals(36, game.countPlayerStones(1, true), "Counting all stones of player 1");

    }

    @Test
    void testException_YOU_CAN_NOT_PUT_LESS_THAN_0_STONES_IN_A_PIT() {
        Exception exception = assertThrows(
                KalahException.class,
                () -> game.setPitStones(1, -1));
        assertEquals(ErrorMessages.YOU_CAN_NOT_PUT_LESS_THAN_0_STONES_IN_A_PIT, exception.getMessage());
    }

    @Test
    void testException_INVALID_PIT_NUMBER() {
        Exception exception = assertThrows(
                KalahException.class,
                () -> game.setPitStones(0, 1));
        assertEquals(ErrorMessages.INVALID_PIT_NUMBER, exception.getMessage());
    }

    @Test
    void testException_INVALID_MOVE() {
        Exception exception;
        exception = assertThrows(
                KalahException.class,
                () -> game.makeNextMove(7));
        assertEquals(ErrorMessages.INVALID_MOVE, exception.getMessage());

        exception = assertThrows(
                KalahException.class,
                () -> game.makeNextMove(14));
        assertEquals(ErrorMessages.INVALID_MOVE, exception.getMessage());

        exception = assertThrows(
                KalahException.class,
                () -> game.makeNextMove(8));
        assertEquals(ErrorMessages.INVALID_MOVE, exception.getMessage());
    }

    @Test
    void testZeroStonesPlayersPitMove_INVALID_MOVE() {
        // Make first move (with repeat rule applied)
        game.makeNextMove(1);
        // Make move into the same pit
        Exception exception = assertThrows(
                KalahException.class,
                () -> game.makeNextMove(1));
        assertEquals(ErrorMessages.INVALID_MOVE, exception.getMessage());
    }

}