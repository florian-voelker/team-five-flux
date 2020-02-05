import React from 'react';
import { connect } from 'react-redux';

import { State } from "../store/state-types";

interface LoginRegisterProps {
    save: (name: string) => void;
    back: () => void;
}

function createListener(ah: (name: string) => void) {
    return (ev: React.FormEvent<HTMLFormElement>) => {
        ev.preventDefault();

        const form = ev.nativeEvent.target as HTMLFormElement;

        const nameInput = form.querySelector('[type="text"]') as HTMLInputElement;
        const name = nameInput.value;
        nameInput.value = "";

        ah(name);
    }
}

export const Save = (props: LoginRegisterProps) => {
    return <div id="save">
        <h2>Spielstand speichern</h2>
        <form onSubmit={createListener(props.save)}>
            <label htmlFor="name"></label>
            <input type="text" id="name" placeholder="Spielstand Name" />
            <button type="submit">Spielstand Speichern</button>
        </form>
        <button onClick={() => props.back()}>zurück zum Menü</button>
    </div>
}

const saveActionCreator = (name: string) => ({ type: 'SAVE', name });
const backToMenuActionCreator = () => ({ type: 'BACK_TO_MENU', name });

export default connect((state: State) => ({}), {
    save: saveActionCreator,
    back: backToMenuActionCreator
})(Save);