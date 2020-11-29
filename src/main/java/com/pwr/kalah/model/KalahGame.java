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

/**
 * A game of 6-stone Kalah implementation
 */
public class KalahGame {

    private final Long gameId;

    private final String gameUri;

    private final KalahBoard board;

    /**
     * Kalah game constructor
     *
     * @param gameId  game ID
     * @param gameUri game URI
     */
    public KalahGame(Long gameId, String gameUri) {
        this.gameId = gameId;
        this.gameUri = gameUri;
        board = new KalahBoardSixStonesImpl();
        board.initGameField();
    }

    /**
     * This constructor is used only in unit tests to simplify test class creation
     *
     * @param gameId game ID
     */
    public KalahGame(Long gameId) {
        this(gameId, "http://localhost:8080" + "/games/" + gameId);
    }


    public void makeNextMove(int pit) {
        board.makeNextMove(pit);
    }

    public KalahBoard getBoard() {
        return board;
    }

    public String getGameUri() {
        return gameUri;
    }

    public Long getGameId() {
        return gameId;
    }

    public KalahGameResponse getResponse() {
        return new KalahGameResponse(gameId, gameUri, board.getStatus());
    }
}