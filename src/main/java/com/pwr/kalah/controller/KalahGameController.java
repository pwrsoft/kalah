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

package com.pwr.kalah.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.pwr.kalah.model.KalahErrorMessages;
import com.pwr.kalah.exception.KalahGameException;
import com.pwr.kalah.model.KalahGame;
import com.pwr.kalah.view.KalahView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * {@link KalahGame} RESTful Web Service controller
 */
@RestController
@Validated
public class KalahGameController {

    private final Map<Long, KalahGame> games = new HashMap<>();
    private final AtomicLong gamesCount = new AtomicLong();

    public KalahGameController() { gamesCount.set(1L); }

    @PostMapping(path = "/games")
    @JsonView(KalahView.NewGame.class)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<KalahGame> createGame(HttpServletRequest request) {
        Long gameId = gamesCount.getAndIncrement();
        String gameUrl = request.getRequestURL().toString() + "/" + gameId;
        KalahGame newGame = new KalahGame(gameId, gameUrl);
        games.put(gameId, newGame);
        return ResponseEntity.created(URI.create(gameUrl)).body(newGame);
    }

    @PutMapping(path = "/games/{gameId}/pits/{pitId}")
    @JsonView(KalahView.GameMove.class)
    public ResponseEntity<KalahGame> makeMove(@PathVariable @Digits(integer=19, fraction=0) @Min(1) @Max(Long.MAX_VALUE) Long gameId,
                              @PathVariable @Digits(integer=2, fraction=0) @Min(1) @Max(14) int pitId) {
        validateGameNumber(gameId);
        KalahGame game = games.get(gameId);
        game.makeNextMove(pitId);
        return ResponseEntity.ok(game);
    }

    /**
     * Validate game number
     * @param pit game number
     */
    private void validateGameNumber(Long pit) {
        if (!games.containsKey(pit))
            throw new KalahGameException(KalahErrorMessages.INVALID_GAME_NUMBER);
    }

}
