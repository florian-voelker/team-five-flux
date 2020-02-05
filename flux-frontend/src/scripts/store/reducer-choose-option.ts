import { Action, Reducer } from "redux";

import { produce } from 'immer'
import { State } from "./state-types";

export interface ChooseOptionAction extends Action<'CHOOSE_OPTION'> {
    chooseOption: number;
}

/**
 * The reducer for the 'CHOOSE_OPTION' action. Adds the current interaction with the chosen option to the history and resets the current interaction.
 */
export default (function chooseOption(state: State, action: ChooseOptionAction) {
    if (action.type != 'CHOOSE_OPTION')
        return state;

    return produce(state, state => {
        const interaction = state.currentInteraction;

        state.terminalHistory.push({
            id: state.terminalHistory.length,
            chosen: action.chooseOption,
            ...interaction
        });

        state.currentInteraction = null;
    });
}) as Reducer<State, ChooseOptionAction>;