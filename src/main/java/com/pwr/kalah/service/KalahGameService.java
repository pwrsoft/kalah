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

package com.pwr.kalah.service;

import com.pwr.kalah.exception.KalahGameException;
import com.pwr.kalah.model.KalahErrorMessages;
import com.pwr.kalah.model.KalahGame;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class KalahGameService implements KalahGameServiceInterface {

    private final Map<Long, KalahGame> games = new HashMap<>();
    private final AtomicLong gamesCount = new AtomicLong();

    public KalahGameService() {
        gamesCount.set(1L);
    }

    @Override
    public KalahGame createGame(String requestUrl) {
        Long newGameId = gamesCount.getAndIncrement();
        KalahGame newGame = new KalahGame(newGameId, requestUrl + "/" + newGameId);
        games.put(newGameId, newGame);
        return newGame;
    }

    @Override
    public KalahGame makeMove(Long gameId, int pitId) {
        validateGameNumber(gameId);
        KalahGame game = games.get(gameId);
        game.makeNextMove(pitId);
        return game;
    }

    private void validateGameNumber(Long pit) {
        if (!games.containsKey(pit)) {
            throw new KalahGameException(KalahErrorMessages.INVALID_GAME_NUMBER);
        }
    }
}
