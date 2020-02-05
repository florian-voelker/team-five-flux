import { State } from "./state-types";
import { Action } from "redux";
import produce from "immer";

export interface TenMinutesNotification extends Action<'TEN_MINUTES_NOTIFICATION'> { };
export interface DismissNotificationAction extends Action<'DISMISS_NOTIFICATION'> { };

export default (function notification(state: State, action: DismissNotificationAction | TenMinutesNotification) {
	switch (action.type) {
		case 'DISMISS_NOTIFICATION':
			return produce(state, state => {
				state.tenMinutes = false;
			});
		case 'TEN_MINUTES_NOTIFICATION':
			return produce(state, state => {
				state.tenMinutes = true;
			});
		default:
			return state;

	}

});