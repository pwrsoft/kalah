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
import com.pwr.kalah.model.KalahGame;
import com.pwr.kalah.model.KalahGameResponse;
import com.pwr.kalah.service.KalahGameService;
import com.pwr.kalah.view.KalahView;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.net.URI;

/**
 * {@link KalahGame} RESTful Web Service controller
 */
@RestController
@Validated
public class KalahGameController {

    private final KalahGameService gameService;

    public KalahGameController(KalahGameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping(path = "/games")
    @JsonView(KalahView.NewGame.class)
    public ResponseEntity<KalahGameResponse> createGame(HttpServletRequest request) {
        KalahGame newGame = gameService.createGame(request.getRequestURL().toString());
        return ResponseEntity.created(URI.create(newGame.getGameUri())).body(newGame.getResponse());
    }

    @PutMapping(path = "/games/{gameId}/pits/{pitId}")
    @JsonView(KalahView.GameMove.class)
    public ResponseEntity<KalahGameResponse> makeMove(
            @PathVariable @Digits(integer=19, fraction=0) @Min(1) @Max(Long.MAX_VALUE) Long gameId,
            @PathVariable @Digits(integer=2, fraction=0) @Min(1) @Max(14) int pitId) {
        KalahGame existingGame = gameService.makeMove(gameId, pitId);
        return ResponseEntity.ok(existingGame.getResponse());
    }

}
