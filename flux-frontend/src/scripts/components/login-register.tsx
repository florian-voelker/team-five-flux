import React from 'react';
import { connect } from 'react-redux';

import { State } from "../store/state-types";
import { LoginAttemptAction } from '../store/reducer-login';

interface AuthenticationHandler {
    (username: string, password: string): void
}

interface LoginRegisterProps {
    screen: State['screen'],
    authentication: State['authentication'],
    attemptLogin: AuthenticationHandler,
    attemptRegister: AuthenticationHandler
}

function createListener(ah: AuthenticationHandler) {
    return (ev: React.FormEvent<HTMLFormElement>) => {
        ev.preventDefault();

        const form = ev.nativeEvent.target as HTMLFormElement;

        const username = (form.querySelector('[type="text"]') as HTMLInputElement).value;
        const password = (form.querySelector('[type="password"]') as HTMLInputElement).value;

        ah(username, password);
    }
}

export const LoginRegister = (props: LoginRegisterProps) => {
    return <div id="login-register">
        <div className="register-wrapper">
            <h2>Registrieren</h2>
            <form className="register" onSubmit={createListener(props.attemptRegister)}>
                <label htmlFor="register-username">Benutzername</label>
                <input type="text" id="register-username" placeholder="Benutzername" />
                <label htmlFor="register-password">Passwort</label>
                <input type="password" id="register-password" placeholder="Passwort" />
                <button type="submit">Registrieren</button>
            </form>
            <div className={props.authentication.lastRegisterResult + ' register-state'}></div>
            <span className="reason">{props.authentication.lastRegisterReason}</span>
        </div>

        <div className="login-wrapper">
            <h2>Anmelden</h2>
            <form className="login" onSubmit={createListener(props.attemptLogin)}>
                <label htmlFor="login-username">Benutzername</label>
                <input type="text" id="login-username" placeholder="Benutzername" />
                <label htmlFor="login-password">Passwort</label>
                <input type="password" id="login-password" placeholder="Passwort" />
                <button type="submit">Anmelden</button>
            </form>
            <div className={props.authentication.lastLoginResult + ' login-state'}></div>
            <span className="reason">{props.authentication.lastLoginReason}</span>
        </div>
    </div>
}

const loginAttemptActionCreator = (username: string, password: string) => ({ type: 'LOGIN_ATTEMPT', username, password }) as LoginAttemptAction
const registerAttemptActionCreator = (username: string, password: string) => ({ type: 'REGISTER_ATTEMPT', username, password }) as RegisterAttemptAction

export default connect((state: State) => ({
    screen: state.screen,
    authentication: state.authentication
}), {
    attemptLogin: loginAttemptActionCreator,
    attemptRegister: registerAttemptActionCreator
})(LoginRegister);