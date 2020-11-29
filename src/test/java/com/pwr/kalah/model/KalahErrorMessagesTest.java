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

import com.pwr.kalah.exception.KalahGameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link KalahErrorMessages} Integration Tests
 */
@DisplayName("KalahErrorMessages Integration Tests")
class KalahErrorMessagesTest {

    private final KalahBoard board = new KalahGame(1L).getBoard();


    @BeforeEach
    void setUp() {
        board.initGameField();
    }

    @Test
    void testEndGameAndScoring_GAME_OVER() {
        Exception exception;
        // New game, test non-repetitive
        board.fillGameFieldWithSample(new int[]{0, 0, 0, 0, 1, 0, 19, 0, 0, 0, 0, 0, 0, 10});
        exception = assertThrows(
                KalahGameException.class,
                () -> board.makeNextMove(5));
        assertEquals(String.format(KalahErrorMessages.GAME_OVER, 20, 10), exception.getMessage());

        // New game, test repetitive move
        board.setCurrentPlayer(1);
        board.fillGameFieldWithSample(new int[]{0, 0, 0, 0, 0, 1, 19, 0, 0, 0, 0, 0, 0, 10});
        exception = assertThrows(
                KalahGameException.class,
                () -> board.makeNextMove(6));
        assertEquals(String.format(KalahErrorMessages.GAME_OVER, 20, 10), exception.getMessage());

        // New game, make first player's move
        // test second player move
        board.setCurrentPlayer(2);
        board.fillGameFieldWithSample(new int[]{0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 1, 9});
        exception = assertThrows(
                KalahGameException.class,
                () -> board.makeNextMove(13));
        assertEquals(String.format(KalahErrorMessages.GAME_OVER, 20, 10), exception.getMessage());

    }

    @Test
    void testException_YOU_CAN_NOT_PUT_LESS_THAN_0_STONES_IN_A_PIT() {
        Exception exception = assertThrows(
                KalahGameException.class,
                () -> board.setPitStones(1, -1));
        assertEquals(KalahErrorMessages.YOU_CAN_NOT_PUT_LESS_THAN_0_STONES_IN_A_PIT, exception.getMessage());
    }

    @Test
    void testException_INVALID_PIT_NUMBER() {
        Exception exception = assertThrows(
                KalahGameException.class,
                () -> board.setPitStones(0, 1));
        assertEquals(KalahBoardSixStones.INVALID_PIT_NUMBER, exception.getMessage());
    }

    @Test
    void testException_INVALID_MOVE() {
        Exception exception;
        exception = assertThrows(
                KalahGameException.class,
                () -> board.makeNextMove(7));
        assertEquals(KalahErrorMessages.INVALID_MOVE, exception.getMessage());

        exception = assertThrows(
                KalahGameException.class,
                () -> board.makeNextMove(14));
        assertEquals(KalahErrorMessages.INVALID_MOVE, exception.getMessage());

        exception = assertThrows(
                KalahGameException.class,
                () -> board.makeNextMove(8));
        assertEquals(KalahErrorMessages.INVALID_MOVE, exception.getMessage());
    }

    @Test
    void testZeroStonesPlayersPitMove_INVALID_MOVE() {
        // Make first move (with repeat rule applied)
        board.makeNextMove(1);
        // Make move into the same pit
        Exception exception = assertThrows(
                KalahGameException.class,
                () -> board.makeNextMove(1));
        assertEquals(KalahErrorMessages.INVALID_MOVE, exception.getMessage());
    }
}