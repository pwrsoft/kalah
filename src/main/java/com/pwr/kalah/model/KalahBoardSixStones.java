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

/**
 * A game of 6-stone Kalah abstract class
 */
abstract class KalahBoardSixStones implements KalahBoard, KalahErrorMessages {

    static final int MAX_STONES = 6;                 // The maximum stones in a pit number (pre-defined for a game of 6-stone Kalah)
    static final int MAX_PITS = MAX_STONES * 2 + 2;  // The sum of of all pits on the game field pits (including each player's pits and the two kalah pits)
    static final String INVALID_PIT_NUMBER = "Invalid pit number. Valid numbers are from 1 to " + MAX_PITS;

    /**
     * Initialize Kalah game field before the first move
     */
    public void initGameField() {
        // At the start of the game, six stones are put in each pit
        for (int i = 1; i <= MAX_PITS; i++) {
            setPitStones(i, MAX_STONES);
        }

        // Empty two Kalah pits
        setPitStones(MAX_PITS / 2, 0);
        setPitStones(MAX_PITS, 0);

        // The first player makes the first move
        setCurrentPlayer(Player.FIRST);
    }

    public void addPitStones(int pit, int stones) {
        setPitStones(pit, getPitStones(pit) + stones);
    }

    /**
     * Validate pit number
     *
     * @param pit pit number
     */
    public void validatePitNumber(int pit) {
        if (pit < 1 || pit > MAX_PITS) {
            throw new KalahGameException(INVALID_PIT_NUMBER);
        }
    }

    /**
     * Get opposite player
     *
     * @return player number
     */
    public Player getOppositePlayer() {
        return getCurrentPlayer() == Player.FIRST ? Player.SECOND : Player.FIRST;
    }

    /**
     * Get Kalah pit for the given player
     *
     * @param player player number
     * @return kalah pit number
     */
    public int getPlayersKalahPit(Player player) {
        return player == Player.FIRST ? MAX_PITS / 2 : MAX_PITS;
    }

    public int countPlayerStones(Player player, boolean countKalah) {
        int stonesCount = 0;
        int firstPit = player == Player.FIRST ? 1 : MAX_STONES + 2;
        for (int i = firstPit; i < firstPit + MAX_STONES + (countKalah ? 1 : 0); i++) {
            stonesCount += getPitStones(i);
        }
        return stonesCount;
    }

    /**
     * Get the number of the opposite pit
     *
     * @param pit pit number
     * @return opposite pit number
     */
    public int getOppositeSidePitNumber(int pit) {
        return MAX_PITS - pit;
    }

    public boolean isPitMineKalah(int pit) {
        return getCurrentPlayer() == Player.FIRST && pit == MAX_PITS / 2 || getCurrentPlayer() == Player.SECOND && pit == MAX_PITS;
    }

    public boolean isPitKalah(int pit) {
        return pit == MAX_PITS / 2 || pit == MAX_PITS;
    }

    public Player changePlayer() {
        return setCurrentPlayer(getOppositePlayer());
    }

    public boolean isPitMine(int pit) {
        if (getCurrentPlayer() == Player.FIRST) {
            return pit >= 1 && pit < MAX_PITS / 2;
        }
        else {
            return pit > MAX_PITS / 2 && pit < MAX_PITS;
        }
    }

}
