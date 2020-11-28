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
 * A game of 6-stone Kalah
 */
abstract class Board implements BoardInterface, KalahErrorMessages {

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
        
        // The first player makes the first move.
        setCurrentPlayer(1);
    }

    /**
     * Add number of stones to a pit
     *
     * @param pit    pit number
     * @param stones number of stones to add
     */
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
    public int getOppositePlayer() {
        return getCurrentPlayer() == 1 ? 2 : 1;
    }

    /**
     * Get Kalah pit for the given player
     *
     * @param player player number
     * @return kalah pit number
     */
    public int getPlayersKalahPit(int player) {
        return player == 1 ? MAX_PITS / 2 : MAX_PITS;
    }

    /**
     * Count stones which belongs to given player
     *
     * @param player     player
     * @param countKalah include Kalah home in total count
     * @return stones count
     */
    public int countPlayerStones(int player, boolean countKalah) {
        int stonesCount = 0;
        int firstPit = player == 1 ? 1 : MAX_STONES + 2;
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

    /**
     * Checks is given pit is Kalah and belongs to the current player
     *
     * @param pit pit number
     * @return result
     */
    public boolean isPitMineKalah(int pit) {
        return getCurrentPlayer() == 1 && pit == MAX_PITS / 2 || getCurrentPlayer() == 2 && pit == MAX_PITS;
    }

    /**
     * Checks is given is Kalah pit
     *
     * @param pit pit number
     * @return result
     */
    public boolean isPitKalah(int pit) {
        return pit == MAX_PITS / 2 || pit == MAX_PITS;
    }

    /**
     * Change the current player to the opposite
     *
     * @return the current player
     */
    public int changePlayer() {
        return setCurrentPlayer(getOppositePlayer());
    }

    /**
     * Checks if the given pit is not Kalah and belongs the current player
     *
     * @param pit pit number
     * @return the result of checking
     */
    public boolean isPitMine(int pit) {
        if (getCurrentPlayer() == 1) {
            return pit >= 1 && pit < MAX_PITS / 2;
        }
        else {
            return pit > MAX_PITS / 2 && pit < MAX_PITS;
        }
    }

}
