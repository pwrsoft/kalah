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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.HashMap;
import java.util.Map;

/**
 * A game of Kalah - Java implementation
 *
 * @author Volodymyr Protsaylo (email: pwr@ukr.net)
 */

public class KalahGame implements ErrorMessages {

    public static final int MAX_PITS = 14;  // The sum of of all pits on the game field pits (including each player's pits and the two kalah pits)
    public static final int MAX_STONES = 6; // The maximum stones in a pit number (pre-defined for a game of 6-stone Kalah)

    @JsonView({KalahView.GameMove.class, KalahView.NewGame.class})
    @JsonProperty("id")
    private final Long gameId; // Each game has it's game ID

    @JsonView({KalahView.GameMove.class, KalahView.NewGame.class})
    @JsonProperty("url")
    private final String gameUrl;

    @JsonView({KalahView.GameMove.class})
    @JsonProperty("status")
    private final Map<Integer, Integer> board = new HashMap<>(); // game field

    @JsonIgnore
    // The number of player who makes next move (1 or 2)
    private int currentPlayer;

    /**
     * Kalah game constructor
     *
     * @param gameId  game ID
     * @param gameUrl game URL
     */
    public KalahGame(Long gameId, String gameUrl) {
        this.gameId = gameId;
        this.gameUrl = gameUrl;
        initGameField();
    }

    /**
     * This constructor is used only in unit tests to simplify test class creation
     *
     * @param gameId game ID
     */
    protected KalahGame(Long gameId) {
        this(gameId, "http://localhost:8080" + "/games/" + gameId);
    }

    /**
     * Empty parameters dummy constructor used in integration tests for JSON deserialization
     */
    protected KalahGame() {
        this(1111L, "http://localhost:8080/games/1111");
    }

    // Getters and Setters
    protected int getCurrentPlayer() {
        return currentPlayer;
    }

    protected void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    protected Long getGameId() {
        return gameId;
    }

    /**
     * Initialize Kalah game field before the first move
     */
    protected final void initGameField() {
        // At the start of the game, six stones are put in each pit
        for (int i = 1; i <= MAX_PITS; i++) {
            setPitStones(i, MAX_STONES);
        }
        // Empty two Kalah pits
        setPitStones(7, 0);
        setPitStones(14, 0);
        // The first player makes the first move.
        // (It can be changed to random player, if needed)
        currentPlayer = 1;
    }

    /**
     * Init playing board with a sample values (used in unit tests only)
     *
     * @param sampleBoard values array
     */
    protected void initGameFieldWithSample(int[] sampleBoard) {
        if (sampleBoard.length < 1 || sampleBoard.length > 14)
            throw new IllegalArgumentException(INPUT_ARRAY_LENGTH_SIZE_IS_INVALID);
        for (int i = 0; i < sampleBoard.length; i++) {
            board.put(i + 1, sampleBoard[i]);
        }
    }

    /**
     * Put a number of stones stones in a pit
     *
     * @param pit    number of pit
     * @param stones number of stones to put
     */
    protected void setPitStones(int pit, int stones) {
        validatePitNumber(pit);
        if (stones < 0)
            throw new BadArgumentsException(YOU_CAN_NOT_PUT_LESS_THAN_0_STONES_IN_A_PIT);
        board.put(pit, stones);
    }

    /**
     * Return number of stones in a pit
     *
     * @param pit pit number
     * @return number of stones
     */
    protected int getPitStones(int pit) {
        validatePitNumber(pit);
        return board.get(pit);
    }

    /**
     * Add number of stones to a pit
     *
     * @param pit    pit number
     * @param stones number of stones to add
     */
    protected void addPitStones(int pit, int stones) {
        setPitStones(pit, getPitStones(pit) + stones);
    }

    /**
     * Validate pit number
     *
     * @param pit pit number
     */
    protected void validatePitNumber(int pit) {
        if (pit < 1 || pit > MAX_PITS)
            throw new BadArgumentsException(INVALID_PIT_NUMBER);
    }

    /**
     * Make the next move
     *
     * @param pit starting move pit number
     */
    public void makeNextMove(int pit) {
        validatePitNumber(pit);
        // The player who begins picks up all the stones in any of their own pits, and sows the stones on
        // to the right, one in each of the following pits, including his own Kalah
        int currentPit = pit;
        int availablePitStones = getPitStones(currentPit);
        if (availablePitStones == 0 || isPitMineKalah(pit) || !doesThePitBelongsTheCurrentPlayer(pit)) {
            throw new BadArgumentsException(INVALID_MOVE);
        }
        setPitStones(currentPit, 0);
        while (availablePitStones > 0) {
            // cycle through the pits
            currentPit++;
            if (currentPit > MAX_PITS)
                currentPit = 1;
            // skip another player's Kalah
            if (currentPlayer == 1 && currentPit == 14 || currentPlayer == 2 && currentPit == 7)
                continue;
            // add the stones one by one
            addPitStones(currentPit, 1);
            availablePitStones--;
        }

        // When the last stone lands in an own empty pit, the player captures this stone and all stones
        // in the opposite pit (the other players' pit) and puts them in his own Kalah
        int oppositePitStones = getPitStones(getOppositePitNumber(currentPit));
        if (doesThePitBelongsTheCurrentPlayer(currentPit) && getPitStones(currentPit) == 1 && oppositePitStones > 0) {
            // acquire stones from the opposite pit plus mine one stone put before
            setPitStones(getOppositePitNumber(currentPit), 0);
            setPitStones(currentPit, 0);
            // put acquired stones in his own Kalah
            int currentPlayersKalahPit = currentPlayer == 1 ? 7 : 14;
            addPitStones(currentPlayersKalahPit, oppositePitStones + (isPitMineKalah(currentPit) ? 0 : 1));
        }

        // After finishing the current player's move
        // if the players last stone lands in his own Kalah, he gets another turn
        if (!isPitMineKalah(currentPit)) {
            // otherwise change the current player
            changePlayer();
        }

        // The game is over as soon as one of the sides run out of stones.
        int currentPlayer = getCurrentPlayer();
        if (countPlayerStones(currentPlayer, false) == 0) {
            // The player who still has stones in his/her pits keeps
            // them and puts them in his/hers Kalah.
            int oppositePlayer = currentPlayer == 1 ? 2 : 1;
            int oppositePlayerStones = countPlayerStones(oppositePlayer, false);
            // NOTE: In this Game End implementation we announce the game results right now

            // moving winner stones to his kalah
            int firstPit = oppositePlayer == 1 ? 1 : 8;
            for (int i = firstPit; i < firstPit + 6; i++) {
                setPitStones(i, 0);
            }
            addPitStones(oppositePlayer == 1 ? 7 : 14, oppositePlayerStones);

            // The winner of the game is the player who has the most stones in his Kalah.
            throw new BadArgumentsException(String.format(GAME_OVER,
                    getPitStones(7), getPitStones(14))
            );
        }

    }

    /**
     * Count stones which belongs to given player
     *
     * @param player     player
     * @param countKalah include Kalah home in total count
     * @return stones count
     */
    protected int countPlayerStones(int player, boolean countKalah) {
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
    private int getOppositePitNumber(int pit) {
        return MAX_PITS - pit;
    }

    /**
     * Checks is given pit belongs to the current player
     *
     * @param pit pit number
     * @return result
     */
    private boolean isPitMineKalah(int pit) {
        return currentPlayer == 1 && pit == 7 || currentPlayer == 2 && pit == 14;
    }

    // The next methods are used in tests
    // thus having a package-wide access

    /**
     * Change the current player to the opposite
     *
     * @return the current player
     */
    protected int changePlayer() {
        currentPlayer = currentPlayer == 1 ? 2 : 1;
        return currentPlayer;
    }

    /**
     * Checks if the given pit belongs the current player
     *
     * @param pit pit number
     * @return the result of checking
     */
    protected boolean doesThePitBelongsTheCurrentPlayer(int pit) {
        validatePitNumber(pit);
        if (currentPlayer == 1) return pit >= 1 && pit <= MAX_PITS / 2;
        else return pit > MAX_PITS / 2 && pit <= MAX_PITS;
    }

    /**
     * Returns the JSON Object of the current game board
     *
     * @return board JSON string representation
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        for (int i = 1; i <= MAX_PITS; i++) {
            sb.append(inQuotes(i)).append(':').append(inQuotes(getPitStones(i))).append(',');
        }
        sb.deleteCharAt(sb.length() - 1).append("}");
        return sb.toString();
    }

    /**
     * Helper method for quoting given values
     *
     * @param value value to quote
     * @return quoted value
     */
    @SuppressWarnings("StringBufferMayBeStringBuilder")
    protected static String inQuotes(int value) {
        return new StringBuffer().append('"').append(value).append('"').toString();
    }
}