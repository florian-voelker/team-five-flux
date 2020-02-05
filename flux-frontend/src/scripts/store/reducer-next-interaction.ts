import { Action, Reducer } from "redux";

import { produce } from 'immer'
import { State, Interaction } from "./state-types";

export interface NextInteractionAction extends Action<'NEXT_INTERACTION'> {
    interaction: Interaction
}

/**
 * The reducer for the 'NEXT_INTERACTION' action. This changes the current interaction to the interaction
 * specified in the payload of the action. 
 */
export default (function nextInteraction(state: State, action: NextInteractionAction) {
    if (action.type != 'NEXT_INTERACTION')
        return state;

    return produce(state, state => {
        state.currentInteraction = action.interaction;
        state.room = action.interaction.lastRoomId;
    });
}) as Reducer<State, NextInteractionAction>