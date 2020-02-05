import { Action, Reducer } from "redux";

import { produce } from 'immer'
import { State } from "./state-types";

export interface RegisterResultAction extends Action<'REGISTER_RESULT'> {
    result: boolean,
    reason: string | null
}

export interface RegisterAttemptAction extends Action<'REGISTER_ATTEMPT'> {
    username: string,
    password: string
}

export default (function nextInteraction(state: State, action: RegisterResultAction | RegisterAttemptAction) {
    if (action.type == 'REGISTER_RESULT')
        return produce(state, state => {
            state.authentication.lastRegisterResult = action.result ? 'successful' : 'failed';
            state.authentication.lastRegisterReason = action.reason;
        });

    if (action.type == 'REGISTER_ATTEMPT')
        return produce(state, state => {
            state.authentication.lastRegisterResult = 'loading';
        });

    return state;
}) as Reducer<State, RegisterResultAction | RegisterAttemptAction>