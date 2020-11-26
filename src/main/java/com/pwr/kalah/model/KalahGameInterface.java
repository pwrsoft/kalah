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

public interface KalahGameInterface extends KalahErrorMessages {
    int MAX_PITS = 14;  // The sum of of all pits on the game field pits (including each player's pits and the two kalah pits)
    int MAX_STONES = 6; // The maximum stones in a pit number (pre-defined for a game of 6-stone Kalah)

    /**
     * Initialize Kalah game field before the first move
     */
    default void initGameField() {
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
     * Fill playing board with a sample values (used in unit tests only)
     *
     * @param sampleBoard values array
     */
    void fillGameFieldWithSample(int[] sampleBoard);

    /**
     * Get current player
     *
     * @return current player
     */
    int getCurrentPlayer();

    /**
     * Set current player
     *
     * @param currentPlayer player to be set as current
     */
    int setCurrentPlayer(int currentPlayer);

    /** Get game id
     *
     * @return game id
     */
    Long getGameId();

    /**
     * Put a number of stones stones in a pit
     *
     * @param pit    number of pit
     * @param stones number of stones to put
     */
    void setPitStones(int pit, int stones);

    /**
     * Return number of stones in a pit
     *
     * @param pit pit number
     * @return number of stones
     */
    int getPitStones(int pit);

    /**
     * Add number of stones to a pit
     *
     * @param pit    pit number
     * @param stones number of stones to add
     */
    default void addPitStones(int pit, int stones) {
        setPitStones(pit, getPitStones(pit) + stones);
    }

    /**
     * Validate pit number
     *
     * @param pit pit number
     */
    default void validatePitNumber(int pit) {
        if (pit < 1 || pit > MAX_PITS)
            throw new KalahGameException(INVALID_PIT_NUMBER);
    }

    /**
     * Get opposite player
     *
     * @return player number
     */
    default int getOppositePlayer() {
        return getCurrentPlayer() == 1 ? 2 : 1;
    }

    /**
     * Get Kalah pit for the given player
     *
     * @param player player number
     * @return kalah pit number
     */
    default int getPlayersKalahPit(int player) {
        return player == 1 ? MAX_PITS / 2 : MAX_PITS;
    }

    /**
     * Count stones which belongs to given player
     *
     * @param player     player
     * @param countKalah include Kalah home in total count
     * @return stones count
     */
    default int countPlayerStones(int player, boolean countKalah) {
        int stonesCount = 0;
        int firstPit = player == 1 ? 1 : 8;
        for (int i = firstPit; i < firstPit + 6 + (countKalah ? 1 : 0); i++) {
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
    default int getOppositeSidePitNumber(int pit) {
        return MAX_PITS - pit;
    }

    /**
     * Checks is given pit is Kalah and belongs to the current player
     *
     * @param pit pit number
     * @return result
     */
    default boolean isPitMineKalah(int pit) {
        return getCurrentPlayer() == 1 && pit == MAX_PITS / 2 || getCurrentPlayer() == 2 && pit == MAX_PITS;
    }

    /**
     * Checks is given is Kalah pit
     *
     * @param pit pit number
     * @return result
     */
    default boolean isPitKalah(int pit) {
        return pit == MAX_PITS / 2 || pit == MAX_PITS;
    }

    // The next methods are used in tests, thus having a package-wide access

    /**
     * Change the current player to the opposite
     *
     * @return the current player
     */
    default int changePlayer() {
        return setCurrentPlayer(getOppositePlayer());
    }

    /**
     * Checks if the given pit is not Kalah and belongs the current player
     *
     * @param pit pit number
     * @return the result of checking
     */
    default boolean isPitMine(int pit) {
        if (getCurrentPlayer() == 1) return pit >= 1 && pit < MAX_PITS / 2;
        else return pit > MAX_PITS / 2 && pit < MAX_PITS;
    }

    /**
     * Returns the JSON Object of the current game board
     *
     * @return board JSON string representation
     */
    String toString();

    /**
     * Helper method for quoting given values
     *
     * @param value value to quote
     * @return quoted value
     */
    @SuppressWarnings("StringBufferReplaceableByString")
    default String inQuotes(int value) {
        return new StringBuilder().append('"').append(value).append('"').toString();
    }

}
