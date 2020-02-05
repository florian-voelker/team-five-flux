import { Store, Action } from "redux";
import { Dispatch } from "react";

import { State } from "./state-types";

// const ig = new InteractionGenerator();

export default (store: Store<State>) =>
    (next: Dispatch<Action<string>>) =>
        (action: Action<string>) => {
            if (!['LOAD_SAVEGAMES', 'LOAD_SAVEGAME', 'CREATE_GAME', 'INITIALIZE'].includes(action.type))
                return next(action);

            (async () => {
                if (action.type == 'INITIALIZE' || action.type == 'LOAD_SAVEGAMES') {
                    const testLoadReq = await fetch('./api/savegame/all');

                    if (testLoadReq.status == 200) {
                        if (action.type == 'INITIALIZE')
                            store.dispatch({
                                type: 'ALREADY_LOGIN'
                            });

                        const savegames = await testLoadReq.json();

                        store.dispatch({
                            type: 'LOADED_SAVEGAMES',
                            savegames
                        });
                    } else if (action.type == 'LOAD_SAVEGAMES')
                        console.error('could not load savegames', testLoadReq);
                }

                if (action.type == 'LOAD_SAVEGAME') {
                    const id = (action as any).savegame.id;

                    const loadSaveGame = await fetch('./api/savegame/load?savegame-id=' + id, { method: 'PUT' })

                    if (loadSaveGame.status == 200)
                        store.dispatch({
                            type: 'LOADED_SESSION'
                        })
                }

                if (action.type == 'CREATE_GAME') {
                    const createSaveGame = await fetch('./api/savegame/new-game');

                    if (createSaveGame.status >= 200 && createSaveGame.status <= 300)
                        store.dispatch({
                            type: 'LOADED_SESSION'
                        })
                }

            })();

            next(action);
        }