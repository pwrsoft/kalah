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

import com.pwr.kalah.controller.KalahGameController;

/**
 * A collection of error messages used both in {@link KalahGame} as well as in {@link KalahGameController}
 * classes
 */
public final class KalahErrorMessages {

    public static final String INVALID_GAME_NUMBER = "This game is not created yet";
    public static final String NON_NUMERIC_VALUE = "Game id and pit number should be numeric and valid";
    public static final String YOU_CAN_NOT_PUT_LESS_THAN_0_STONES_IN_A_PIT = "You can not put less then 0 stones in a pit";
    public static final String INPUT_ARRAY_LENGTH_SIZE_IS_INVALID = "Input array length size is invalid";
    public static final String INVALID_MOVE = "Invalid move";
    public static final String GAME_OVER = "Game over! Score is %s:%s";

    private KalahErrorMessages() {
        throw new IllegalStateException("Utility class");
    }
}
