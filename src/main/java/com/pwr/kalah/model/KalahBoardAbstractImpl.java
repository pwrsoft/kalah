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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class KalahBoardAbstractImpl extends KalahBoardAbstract implements KalahErrorMessages {

    private final Map<Integer, Integer> board = new ConcurrentHashMap<>();
    private Player currentPlayer;

    private BoardStatus boardStatus = BoardStatus.INITIAL;

    private int currentPit;

    public KalahBoardAbstractImpl() {
        super(6);
    }

    public void makeNextMove(int pit) {
        if (boardStatus == BoardStatus.FINISHED) {
            returnGameOver();
        }
        validatePitNumber(pit);

        // Get the first player based on the first move pit
        defineCurrentPlayer(pit);

        // The player who begins picks up all the stones in any of their own pits, and sows the stones on
        // to the right, one in each of the following pits, including his own Kalah
        pickAndSowTheStones(pit);

        // When the last stone lands in an own empty pit, the player captures this stone and all stones
        // in the opposite pit (the other players' pit) and puts them in his own Kalah
        captureStones();

        // After finishing the current player's move
        // if the players last stone lands in his own Kalah, he gets another turn
        changePlayerOrMakeAnotherMove();

        // The game is over as soon as one of the sides run out of stones.
        checkEndOfGame();
    }

    private void defineCurrentPlayer(int pit) {
        if (boardStatus == BoardStatus.INITIAL) {
            if (pit > maxPits / 2) {
                setCurrentPlayer(Player.SECOND);
            }
            boardStatus = BoardStatus.STARTED;
        }
    }

    private void pickAndSowTheStones(int pit) {
        currentPit = pit;
        int availablePitStones = getPitStones(currentPit);
        if (availablePitStones == 0 || isPitKalah(pit) || !isPitMine(pit)) {
            throw new KalahGameException(INVALID_MOVE);
        }
        setPitStones(currentPit, 0);
        while (availablePitStones > 0) {

            // cycle through the pits
            currentPit++;
            if (currentPit > maxPits) {
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
    }

    private void captureStones() {
        int oppositePitStones = 0;
        if (!isPitKalah(currentPit)) {
            oppositePitStones = getPitStones(getOppositeSidePitNumber(currentPit));
        }
        if (isPitMine(currentPit) && !isPitMineKalah(currentPit) && getPitStones(currentPit) == 1 && oppositePitStones > 0) {

            // acquire stones from the opposite pit plus mine one stone put before
            setPitStones(getOppositeSidePitNumber(currentPit), 0);
            setPitStones(currentPit, 0);

            // put acquired stones in his own Kalah
            addPitStones(getPlayersKalahPit(getCurrentPlayer()), oppositePitStones + (isPitMineKalah(currentPit) ? 0 : 1));
        }
    }

    private void changePlayerOrMakeAnotherMove() {
        if (!isPitMineKalah(currentPit)) {
            // otherwise change the current player
            changePlayer();
        }
    }

    private void checkEndOfGame() {
        if (countPlayerStones(getCurrentPlayer(), false) == 0) {

            // The player who still has stones in his/her pits keeps
            // them and puts them in his/hers Kalah.
            Player oppositePlayer = getOppositePlayer();
            int oppositePlayerStones = countPlayerStones(oppositePlayer, false);

            // moving winner stones to his kalah
            int firstPit = oppositePlayer == Player.FIRST ? 1 : maxStones + 2;
            for (int i = firstPit; i < firstPit + maxStones; i++) {
                setPitStones(i, 0);
            }
            addPitStones(getPlayersKalahPit(oppositePlayer), oppositePlayerStones);

            // The winner of the game is the player who has the most stones in his Kalah.
            setBoardStatus(BoardStatus.FINISHED);
            returnGameOver();
        }
    }

    private void returnGameOver() {
        throw new KalahGameException(String.format(GAME_OVER,
                getPitStones(getPlayersKalahPit(Player.FIRST)), getPitStones(getPlayersKalahPit(Player.SECOND)))
        );
    }

    public void fillGameFieldWithSample(int[] sampleBoard) {
        boardStatus = BoardStatus.INITIAL;
        if (sampleBoard.length < 1 || sampleBoard.length > maxPits) {
            throw new IllegalArgumentException(INPUT_ARRAY_LENGTH_SIZE_IS_INVALID);
        }
        for (int i = 0; i < sampleBoard.length; i++) {
            board.put(i + 1, sampleBoard[i]);
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        return currentPlayer;
    }

    public BoardStatus getBoardStatus() {
        return boardStatus;
    }

    public void setBoardStatus(BoardStatus boardStatus) {
        this.boardStatus = boardStatus;
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

    public HashMap<Integer, Integer> getStatus() {
        return new HashMap<>(board);
    }

    public String toString() {
        return "{"+board.entrySet().stream()
                .map(e -> "\""+ e.getKey() + "\"" + ":\"" + e.getValue() + "\"")
                .collect(Collectors.joining(","))+"}";
    }

}
