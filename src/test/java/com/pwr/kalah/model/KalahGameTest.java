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

package com.pwr.kalah.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link KalahGame} Unit Tests
 */
@DisplayName("KalahGame Unit Tests")
class KalahGameTest {

    private final KalahBoard board = new KalahGame(1L).getBoard();

    @BeforeEach
    void setUp() {
        board.initGameField();
    }

    @Test
    void testGameFirstPlayer() {
        assertEquals(Player.FIRST, board.getCurrentPlayer(), "The game starts with player number 1");
    }

    @Test
    void testChangingPlayers() {
        assertEquals(Player.SECOND, board.changePlayer(), "When game starts and after the first move the player number should be 2 ");
        assertEquals(Player.FIRST, board.changePlayer(), "After the next move the player number should be 1 again ");
    }

    @Test
    void testInitGameField() {
        assertEquals("{1=6, 2=6, 3=6, 4=6, 5=6, 6=6, 7=0, 8=6, 9=6, 10=6, 11=6, 12=6, 13=6, 14=0}", board.toString(), "Invalid initial game field");
    }

    @Test
    void testSetAndGetCurrentPlayer() {
        board.setCurrentPlayer(Player.FIRST);
        assertEquals(Player.FIRST, board.getCurrentPlayer(), "Current player should be 1");
        board.setCurrentPlayer(Player.SECOND);
        assertEquals(Player.SECOND, board.getCurrentPlayer(), "Current player should be 2");
    }

    @Test
    void testThePitBelongsTheCurrentPlayer() {
        board.setCurrentPlayer(Player.FIRST);
        assertTrue(board.isPitMine(1), "Pit 1 owner is invalid");
        board.setCurrentPlayer(Player.SECOND);
        assertTrue(board.isPitMine(8), "Pit 8 owner is invalid");
    }

    @Test
    void testKalahPits() {
        board.setCurrentPlayer(Player.FIRST);
        assertTrue(board.isPitMineKalah(7), "Pit 7 is Kalah of player 1");
        assertTrue(board.isPitKalah(7), "Pit 7 is Kalah");
        board.setCurrentPlayer(Player.SECOND);
        assertTrue(board.isPitMineKalah(14), "Pit 14 is Kalah of player 2");
        assertTrue(board.isPitKalah(14), "Pit 14 is Kalah");
    }

    @Test
    void testGetSetAddPitStones() {
        assertEquals(6, board.getPitStones(1), "Invalid number of stones in pit 1");
        assertEquals(6, board.getPitStones(13), "Invalid number of stones in pit 13");
        assertEquals(0, board.getPitStones(7), "Invalid number of stones in pit 7");
        assertEquals(0, board.getPitStones(14), "Invalid number of stones in pit 14");
        board.setPitStones(1, 5);
        assertEquals(5, board.getPitStones(1), "Invalid number of stones in pit 1 after setting number of stones to 5");
        board.addPitStones(1, 2);
        assertEquals(7, board.getPitStones(1), "Invalid number of stones in pit 1 after adding 1 stone");
    }

    @Test
    void testSimpleGame() {
        board.makeNextMove(2);
        assertEquals("{1=6, 2=0, 3=7, 4=7, 5=7, 6=7, 7=1, 8=7, 9=6, 10=6, 11=6, 12=6, 13=6, 14=0}", board.toString(), "Invalid game field after step 1");
        board.makeNextMove(10);
        assertEquals("{1=7, 2=1, 3=7, 4=7, 5=7, 6=7, 7=1, 8=7, 9=6, 10=0, 11=7, 12=7, 13=7, 14=1}", board.toString(), "Invalid game field after step 2");
    }

    @Test
    void testSecondPlayerMakesFirstMoveWithRepeat() {
        board.makeNextMove(8);
        assertEquals(Player.SECOND, board.getCurrentPlayer(), "The game starts with player number 2, no repeat rule");
        board.makeNextMove(9);
        assertEquals(Player.FIRST, board.getCurrentPlayer(), "The game starts with player number 2, no repeat rule");
    }

    @Test
    void testCaptureStones() {
        board.fillGameFieldWithSample(new int[]{0, 0, 0, 12, 0, 0, 0, 0, 0, 5, 3, 0, 0, 0});
        board.makeNextMove(4);
        assertEquals("{1=1, 2=1, 3=0, 4=0, 5=1, 6=1, 7=6, 8=1, 9=1, 10=6, 11=0, 12=1, 13=1, 14=0}", board.toString(), "Invalid game field after step 2");

    }

    @Test
    void testPlayersSequentialMoves() {
        board.fillGameFieldWithSample(new int[]{0, 0, 1, 3, 2, 0, 0, 0, 0, 5, 3, 0, 0, 0});
        board.makeNextMove(5);
        assertEquals("{1=0, 2=0, 3=1, 4=3, 5=0, 6=1, 7=1, 8=0, 9=0, 10=5, 11=3, 12=0, 13=0, 14=0}", board.toString(), "Invalid game field after step 2");
        assertEquals(Player.FIRST, board.getCurrentPlayer(), "Player 1 should have another turn");
        board.makeNextMove(4);
        assertEquals("{1=0, 2=0, 3=1, 4=0, 5=1, 6=2, 7=2, 8=0, 9=0, 10=5, 11=3, 12=0, 13=0, 14=0}", board.toString(), "Invalid game field after step 2");
        assertEquals(Player.FIRST, board.getCurrentPlayer(), "Player 1 should have another turn again");
        board.makeNextMove(3);
        assertEquals("{1=0, 2=0, 3=0, 4=0, 5=1, 6=2, 7=8, 8=0, 9=0, 10=0, 11=3, 12=0, 13=0, 14=0}", board.toString(), "Invalid game field after step 2");
        assertEquals(Player.SECOND, board.getCurrentPlayer(), "Now it's turn for Player 2");
    }

    @Test
    void testCountPlayersStones() {
        board.makeNextMove(1);
        assertEquals(35, board.countPlayerStones(Player.FIRST, false), "Counting pit stones of player 1");
        assertEquals(36, board.countPlayerStones(Player.FIRST, true), "Counting all stones of player 1");
        assertEquals(36, board.countPlayerStones(Player.SECOND, true), "Counting all stones of player 2");

    }
}
