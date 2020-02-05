import { Store, Action } from "redux";
import { Dispatch } from "react";

import { State } from "./state-types";


export default (store: Store<State>) =>
    (next: Dispatch<Action<string>>) =>
        (action: Action<string>) => {
            if (!['SAVE', 'BACK_TO_LOAD'].includes(action.type))
                return next(action);

            (async () => {
                if (action.type == 'SAVE') {
                    const encodedName = encodeURIComponent((action as any).name);
                    await fetch('./api/savegame/save?name=' + encodedName, { method: 'PUT' });
                    store.dispatch({ type: 'LOAD_SAVEGAMES' });
                }
            })();

            next(action);
        }