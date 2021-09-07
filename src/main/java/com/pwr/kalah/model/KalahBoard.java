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

import java.util.HashMap;

public interface KalahBoard {

    /**
     * Fill playing board with a sample values (used in unit tests only)
     *
     * @param sampleBoard values array
     */
    void fillGameFieldWithSample(int... sampleBoard);

    /**
     * Get current player
     *
     * @return current player
     */
    Player getCurrentPlayer();

    /**
     * Set current player
     *
     * @param currentPlayer player to be set as current
     */
    Player setCurrentPlayer(Player currentPlayer);

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
     * Returns the JSON Object of the current game board
     *
     * @return board JSON string representation
     */
    String toString();

    /**
     * Initializes a board before the first move according to the rules of the game
     */
    void initGameField();

    /**
     * Make next move starting from the given pit number
     *
     * @param pit pit number
     */
    void makeNextMove(int pit);

    /**
     * Returns HashMap of the board
     * @return game board hashmap
     */
    HashMap<Integer, Integer> getStatus();

    /**
     * Change the current player to the opposite
     *
     * @return the current player
     */
    Player changePlayer();

    /**
     * Checks if the given pit is not Kalah and belongs the current player
     *
     * @param pit pit number
     * @return the result of checking
     */
    boolean isPitMine(int pit);

    /**
     * Checks is given pit is Kalah and belongs to the current player
     *
     * @param pit pit number
     * @return result
     */
    boolean isPitMineKalah(int pit);

    /**
     * Checks is given is Kalah pit
     *
     * @param pit pit number
     * @return result
     */
    boolean isPitKalah(int pit);

    /**
     * Add number of stones to a pit
     *
     * @param pit    pit number
     * @param stones number of stones to add
     */
    void addPitStones(int pit, int stones);

    /**
     * Count stones which belongs to given player
     *
     * @param player     player
     * @param countKalah include Kalah home in total count
     * @return stones count
     */
    int countPlayerStones(Player player, boolean countKalah);
}
