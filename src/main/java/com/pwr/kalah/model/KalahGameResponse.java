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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.pwr.kalah.view.KalahView;

import java.util.HashMap;

public class KalahGameResponse {

    @JsonView({KalahView.GameMove.class, KalahView.NewGame.class})
    @JsonProperty("id")
    Long id;

    @JsonView({KalahView.GameMove.class, KalahView.NewGame.class})
    @JsonProperty("uri")
    String uri;

    @JsonView({KalahView.GameMove.class})
    @JsonProperty("status")
    HashMap<Integer, Integer> status;

    /**
     * This constructor is used in integration tests for JSON deserialization
     */
    public KalahGameResponse() {
    }

    public KalahGameResponse(Long id, String uri, HashMap<Integer, Integer> status) {
        this.id = id;
        this.uri = uri;
        this.status = status;
    }

    public Long getId() {
        return id;
    }
}
