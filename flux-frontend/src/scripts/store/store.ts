import { createStore, applyMiddleware, compose, Action } from 'redux';
import { Reducer } from 'react';

import { chainReducer } from '../util';

import chooseOption from './reducer-choose-option';
import nextInteraction from './reducer-next-interaction';
import login from './reducer-login';
import register from './reducer-register';
import savegameReducer from './reducer-savegame';
import notification from "./reducer-notification";

import loginRegister from './middleware-login-register';
import savegame from './middleware-savegame';
import gamestate from './middleware-gamestate';
import save from './middleware-save';

import { State } from './state-types';

const initialState: State = {
  terminalHistory: [],
  currentInteraction: null,
  room: undefined,
  screen: 'login+register',
  tenMinutes: false,
  authentication: {
    loggedIn: false,
    lastLoginResult: 'uninitialized',
    lastLoginReason: null,
    lastRegisterResult: 'uninitialized',
    lastRegisterReason: null
  },
  savegames: []
}

const extName = '__REDUX_DEVTOOLS_EXTENSION_COMPOSE__';
const composeFnc = extName in window ? (window as any)[extName].bind(window) : compose;

const reducer = chainReducer<Reducer<State, any>>(
  chooseOption,
  nextInteraction,
  login,
  register,
  savegameReducer,
  notification
);

export default createStore(reducer, initialState, composeFnc(applyMiddleware(
  loginRegister,
  savegame,
  gamestate,
  save
)));  