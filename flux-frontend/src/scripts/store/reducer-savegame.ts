import { Action, Reducer } from "redux";

import { produce } from 'immer'
import { State, Interaction, SaveGame } from "./state-types";

export interface LoadedSavegamesAction extends Action<'LOADED_SAVEGAMES'> {
    savegames: SaveGame[]
}

export interface LoginAttemptAction extends Action<'LOGIN_ATTEMPT'> {
    username: string,
    password: string
}

export interface LoadSavegameAction extends Action<'LOAD_SAVEGAME'> {
    savegame: SaveGame
}

export interface LoadedSessionAction extends Action<'LOADED_SESSION'> { }

export interface AlreadyLoginAction extends Action<'ALREADY_LOGIN'> { }

export interface BackToMenuAction extends Action<'BACK_TO_MENU'> { }

export default (function nextInteraction(state: State, action: LoadedSavegamesAction | LoadedSessionAction | BackToMenuAction) {
    if (action.type == 'LOADED_SAVEGAMES')
        return produce(state, state => {
            state.savegames = action.savegames;
        });

    if (action.type == 'LOADED_SESSION')
        return produce(state, state => {
            state.screen = 'game';
        });

    if (action.type == 'BACK_TO_MENU')
        return produce(state, state => {
            state.screen = 'load+new';
            state.currentInteraction = null;
            state.terminalHistory = [];
        })

    return state;
}) as Reducer<State, LoadedSavegamesAction>