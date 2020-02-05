import { Store, Action } from "redux";
import { Dispatch } from "react";

import { State, Interaction } from "./state-types";

export default (store: Store<State>) =>
    (next: Dispatch<Action<string>>) =>
        (action: Action<string>) => {
            if (!['LOGIN_ATTEMPT', 'REGISTER_ATTEMPT', 'LOGOUT'].includes(action.type))
                return next(action);

            (async () => {
                // reminder, ALREADY_LOGIN will be fired on INITIALIZE when the session is still valid
                if (action.type == 'LOGIN_ATTEMPT') {
                    const { password, username } = action as any;

                    const loginReq = await fetch('./api/authentication/login', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ password, username })
                    });

                    store.dispatch({
                        type: 'LOGIN_RESULT',
                        result: loginReq.status == 200,
                        reason: loginReq.headers.get('reason')
                    });

                    if (loginReq.status == 200)
                        store.dispatch({
                            type: 'LOAD_SAVEGAMES'
                        });
                }

                if (action.type == 'REGISTER_ATTEMPT') {
                    const { password, username } = action as any;

                    const loginReq = await fetch('./api/authentication/register', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ password, username })
                    });

                    store.dispatch({
                        type: 'REGISTER_RESULT',
                        result: loginReq.status == 200,
                        reason: loginReq.headers.get('reason')
                    });
                }

                if (action.type == 'LOGOUT') {
                    await fetch('./api/authentication/logout', { method: 'POST' });
                    store.dispatch({
                        type: 'BACK_TO_LOGIN'
                    });
                }
            })();

            next(action);
        }