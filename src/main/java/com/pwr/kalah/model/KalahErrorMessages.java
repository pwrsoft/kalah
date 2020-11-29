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
 * This interface is a collection of error messages used both in {@link KalahGame} as well as in {@link KalahGameController} classes
 */
public interface KalahErrorMessages {

    String INVALID_GAME_NUMBER = "This game is not created yet";
    String INVALID_PIT_NUMBER = "Invalid pit number";
    String NON_NUMERIC_VALUE = "Game id and pit number should be numeric and valid";
    String YOU_CAN_NOT_PUT_LESS_THAN_0_STONES_IN_A_PIT = "You can not put less then 0 stones in a pit";
    String INPUT_ARRAY_LENGTH_SIZE_IS_INVALID = "Input array length size is invalid";
    String INVALID_MOVE = "Invalid move";
    String GAME_OVER = "Game over! Score is %s:%s";
}
