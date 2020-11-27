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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.pwr.kalah.exception.KalahGameException;
import com.pwr.kalah.view.KalahView;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A game of 6-stone Kalah implementation
 */
public class KalahGame implements KalahGameInterface {
    @JsonView({KalahView.GameMove.class, KalahView.NewGame.class})
    @JsonProperty("id")
    private final Long gameId;

    @JsonView({KalahView.GameMove.class, KalahView.NewGame.class})
    @JsonProperty("url")
    private final String gameUrl;

    @JsonView({KalahView.GameMove.class})
    @JsonProperty("status")
    // TODO replace by KalahBoard class
    //private KalahBoard board;
    Map<Integer, Integer> board = new ConcurrentHashMap<>();


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
        // TODO replace by KalahBoard class
        //board = new KalahBoard();
        initGameField();
    }

    /**
     * This constructor is used only in unit tests to simplify test class creation
     *
     * @param gameId game ID
     */
    public KalahGame(Long gameId) {
        this(gameId, "http://localhost:8080" + "/games/" + gameId);
    }

    /**
     * Empty parameters dummy constructor used in integration tests for JSON deserialization
     */
    public KalahGame() {
        this(1111L, "http://localhost:8080/games/1111");
    }

    public void makeNextMove(int pit) {
        validatePitNumber(pit);

        // The player who begins picks up all the stones in any of their own pits, and sows the stones on
        // to the right, one in each of the following pits, including his own Kalah
        int currentPit = pit;
        int availablePitStones = getPitStones(currentPit);
        if (availablePitStones == 0 || isPitKalah(pit) || !isPitMine(pit)) {
            throw new KalahGameException(INVALID_MOVE);
        }
        setPitStones(currentPit, 0);
        while (availablePitStones > 0) {

            // cycle through the pits
            currentPit++;
            if (currentPit > MAX_PITS) {
                currentPit = 1;
            }

            // skip another player's Kalah
            if (currentPit == getPlayersKalahPit(getOppositePlayer())) {
                continue;
            }

            // add the stones one by one
            addPitStones(currentPit, 1);
            availablePitStones--;
        }

        // When the last stone lands in an own empty pit, the player captures this stone and all stones
        // in the opposite pit (the other players' pit) and puts them in his own Kalah
        int oppositePitStones = 0;
        if (!isPitKalah(currentPit)) {
            oppositePitStones = getPitStones(getOppositeSidePitNumber(currentPit));
        }
        if (isPitMine(currentPit) && !isPitMineKalah(currentPit) && getPitStones(currentPit) == 1 && oppositePitStones > 0) {

            // acquire stones from the opposite pit plus mine one stone put before
            setPitStones(getOppositeSidePitNumber(currentPit), 0);
            setPitStones(currentPit, 0);

            // put acquired stones in his own Kalah
            addPitStones(getPlayersKalahPit(currentPlayer), oppositePitStones + (isPitMineKalah(currentPit) ? 0 : 1));
        }

        // After finishing the current player's move
        // if the players last stone lands in his own Kalah, he gets another turn
        if (!isPitMineKalah(currentPit)) {
            // otherwise change the current player
            changePlayer();
        }

        // The game is over as soon as one of the sides run out of stones.
        if (countPlayerStones(getCurrentPlayer(), false) == 0) {

            // The player who still has stones in his/her pits keeps
            // them and puts them in his/hers Kalah.
            int oppositePlayer = getOppositePlayer();
            int oppositePlayerStones = countPlayerStones(oppositePlayer, false);

            // moving winner stones to his kalah
            int firstPit = oppositePlayer == 1 ? 1 : MAX_STONES + 2;
            for (int i = firstPit; i < firstPit + MAX_STONES; i++) {
                setPitStones(i, 0);
            }
            addPitStones(getPlayersKalahPit(oppositePlayer), oppositePlayerStones);

            // The winner of the game is the player who has the most stones in his Kalah.
            throw new KalahGameException(String.format(GAME_OVER,
                    getPitStones(getPlayersKalahPit(1)), getPitStones(getPlayersKalahPit(2)))
            );
        }

    }

    @Override
    public Long getGameId() {
        return gameId;
    }

    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public int setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        return currentPlayer;
    }

     public int getPitStones(int pit) {
        validatePitNumber(pit);
        return board.get(pit);
    }

    public void setPitStones(int pit, int stones) {
        validatePitNumber(pit);
        if (stones < 0) {
            throw new KalahGameException(YOU_CAN_NOT_PUT_LESS_THAN_0_STONES_IN_A_PIT);
        }
        board.put(pit, stones);
    }

    public void fillGameFieldWithSample(int[] sampleBoard) {
        if (sampleBoard.length < 1 || sampleBoard.length > MAX_PITS) {
            throw new IllegalArgumentException(INPUT_ARRAY_LENGTH_SIZE_IS_INVALID);
        }
        for (int i = 0; i < sampleBoard.length; i++) {
            board.put(i + 1, sampleBoard[i]);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 1; i <= MAX_PITS; i++) {
            sb.append(inQuotes(i)).append(':').append(inQuotes(getPitStones(i))).append(',');
        }
        sb.deleteCharAt(sb.length() - 1).append("}");
        return sb.toString();
    }
}