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

public interface BoardInterface {

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
}
