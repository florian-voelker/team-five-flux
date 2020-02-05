import { Action, Reducer } from "redux";

import { produce } from 'immer'
import { State, Interaction } from "./state-types";

export interface LoginResultAction extends Action<'LOGIN_RESULT'> {
    result: boolean,
    reason: string | null
}

export interface LoginAttemptAction extends Action<'LOGIN_ATTEMPT'> {
    username: string,
    password: string
}

export interface AlreadyLoginAction extends Action<'ALREADY_LOGIN'> { }
export interface BackToLoginAction extends Action<'BACK_TO_LOGIN'> { }

/**
 * The reducer for every login related action. Manipulates the login related areas of the store and might lead to a view change.
 */
export default (function nextInteraction(state: State, action: LoginResultAction | LoginAttemptAction | AlreadyLoginAction | BackToLoginAction) {
    if (action.type == 'ALREADY_LOGIN')
        return produce(state, state => {
            state.screen = 'load+new';
        });

    if (action.type == 'LOGIN_RESULT')
        return produce(state, state => {
            state.authentication.lastLoginResult = action.result ? 'successful' : 'failed';
            state.authentication.lastLoginReason = action.reason;

            if (action.result)
                state.screen = 'load+new';
        });

    if (action.type == 'LOGIN_ATTEMPT')
        return produce(state, state => {
            state.authentication.lastLoginResult = 'loading';
        });

    if (action.type == 'BACK_TO_LOGIN')
        return produce(state, state => {
            state.authentication.loggedIn = false;
            state.screen = 'login+register';

            state.authentication.lastLoginResult = 'uninitialized';
            state.authentication.lastRegisterResult = 'uninitialized';

            state.authentication.lastRegisterReason = null;
            state.authentication.lastLoginReason = null;
        });

    return state;
}) as Reducer<State, LoginResultAction | LoginAttemptAction>