import { Store, Action } from "redux";
import { Dispatch } from "react";

import { State, Interaction } from "./state-types";

import { NextInteractionAction } from "./reducer-next-interaction";

let establishedWebSocket: WebSocket;

interface AvailableAction {
    available: boolean,
    description: string,
    id: number
}

interface NewGameMessage {
    type: 'NEW_GAME_STATE',
    lastRoomId: number,
    result?: string,
    description: string,
    allActions: AvailableAction[]
}

export default (store: Store<State>) =>
    (next: Dispatch<Action<string>>) =>
        (action: Action<string>) => {
            if (!['LOADED_SESSION', 'CHOOSE_OPTION'].includes(action.type)) {
                const state = store.getState()

                // close if there is an session and the screen leaves the game screen
                if (state.screen != 'game' && establishedWebSocket != undefined) {
                    establishedWebSocket.close();
                    establishedWebSocket = undefined;
                }

                return next(action);
            }

            (async () => {
                if (action.type == 'LOADED_SESSION') {
                    if (establishedWebSocket) {
                        establishedWebSocket.close();
                        establishedWebSocket = undefined;
                    }

                    const { protocol, host, pathname } = window.location;
                    const wsProtocol = protocol == 'https:' ? 'wss:' : 'ws:';
                    const wsHost = host;
                    const pathnameWOSlash = pathname[pathname.length - 1] == '/' ? pathname.slice(0, -1) : pathname;
                    const wsPathname = pathnameWOSlash + '/game/';

                    const url = wsProtocol + '//' + wsHost + wsPathname;

                    establishedWebSocket = new WebSocket(url);

                    establishedWebSocket.addEventListener('open', () => {
                        console.info('established websocket connection');
                    });

                    establishedWebSocket.addEventListener('message', ({ data }) => {
                        const parsed = JSON.parse(data);

                        if (!('type' in parsed))
                            console.error('received a message which had no type field', parsed);

                        console.info('received', parsed);
                        if (parsed.type == "NEW_GAME_STATE") {
                            const ngsMessage = parsed as NewGameMessage;
                            // store.dispatch({
                            //     type: 'RECEIVED_MESSAGE',
                            //     message: parsed
                            // });

                            const interaction: Interaction = {
                                lastRoomId: ngsMessage.lastRoomId,
                                information: ngsMessage.description,
                                originResult: ngsMessage.result,
                                options: ngsMessage.allActions.map(a => ({
                                    id: a.id,
                                    option: a.description,
                                    available: a.available
                                }))
                            };

                            store.dispatch({
                                type: 'NEXT_INTERACTION',
                                interaction
                            } as NextInteractionAction);

                        } else if (parsed.type == "timer-message") {
                            store.dispatch({
                                type: 'TEN_MINUTES_NOTIFICATION'
                            });
                        }                         
                        else {
                            console.warn('received a message from the websocket channel which was not anticipated:', parsed);
                        }
                    })

                    establishedWebSocket.addEventListener('close', () => {
                        console.info('websocket connection was closed');
                        establishedWebSocket = undefined;
                        store.dispatch({
                            type: 'CONNECTION_CLOSED'
                        });
                    });
                }

                if (action.type == 'CHOOSE_OPTION') {
                    if (!establishedWebSocket) {
                        console.error('web socket connection is not established');
                        return;
                    }

                    const chooseOption = (action as any).chooseOption;
                    if (typeof chooseOption != 'number') {
                        console.error('chooseOption is not a number', action);
                        return;
                    }

                    const message = JSON.stringify({ type: 'CHOSE_OPTION', option: chooseOption });
                    console.log('sending', message);
                    establishedWebSocket.send(message);
                }

            })();

            next(action);
        }