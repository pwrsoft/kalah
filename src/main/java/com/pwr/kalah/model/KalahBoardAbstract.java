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
abstract class KalahBoardAbstract implements KalahBoard, KalahErrorMessages {

    protected int maxStones; // The maximum stones in a pit number (pre-defined for a game of 6-stone Kalah)
    protected int maxPits;   // The sum of of all pits on the game field pits (including each player's pits and the two kalah pits)

    public KalahBoardAbstract(int maxStones) {
        this.maxStones = maxStones;
        this.maxPits = this.maxStones * 2 + 2;
    }

    public void initGameField() {
        // At the start of the game, six stones are put in each pit
        for (int i = 1; i <= maxPits; i++) {
            setPitStones(i, maxStones);
        }

        // Empty two Kalah pits
        setPitStones(maxPits / 2, 0);
        setPitStones(maxPits, 0);

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
        if (pit < 1 || pit > maxPits) {
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
        return player == Player.FIRST ? maxPits / 2 : maxPits;
    }

    public int countPlayerStones(Player player, boolean countKalah) {
        int stonesCount = 0;
        int firstPit = player == Player.FIRST ? 1 : maxStones + 2;
        for (int i = firstPit; i < firstPit + maxStones + (countKalah ? 1 : 0); i++) {
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
        return maxPits - pit;
    }

    public boolean isPitMineKalah(int pit) {
        return getCurrentPlayer() == Player.FIRST && pit == maxPits / 2 || getCurrentPlayer() == Player.SECOND && pit == maxPits;
    }

    public boolean isPitKalah(int pit) {
        return pit == maxPits / 2 || pit == maxPits;
    }

    public Player changePlayer() {
        return setCurrentPlayer(getOppositePlayer());
    }

    public boolean isPitMine(int pit) {
        if (getCurrentPlayer() == Player.FIRST) {
            return pit >= 1 && pit < maxPits / 2;
        }
        else {
            return pit > maxPits / 2 && pit < maxPits;
        }
    }

}
