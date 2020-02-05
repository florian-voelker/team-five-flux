import React from 'react';
import { connect } from 'react-redux';

import { State, SaveGame } from "../store/state-types";

interface LoadNewProps {
    savegames: SaveGame[];
    loadGame: (sg: SaveGame) => void;
    createNewGame: () => void;
    logout: () => void;
}

const convertToReadable = (ts: number) => {
    const d = new Date(ts);
    return d.getDate() + '.' + (d.getMonth() + 1) + '.' + d.getFullYear() + ' ' + d.getHours() + ':' + d.getMinutes() + ':' + d.getSeconds()
}

interface SaveGameProps {
    savegame: SaveGame;
    loadGame: (savegame: SaveGame) => void;
}

const SaveGameComp = ({ savegame, loadGame }: SaveGameProps) =>
    <div className="savegame">
        <span className="name">{savegame.name}</span>
        <span className="savetime">{convertToReadable(savegame.timestamp)}</span>
        <button onClick={() => loadGame(savegame)}>Spiel laden</button>
    </div>

export const LoadNew = (props: LoadNewProps) => {
    return <div id="load-new">
        <div className="savegames">
            {props.savegames.map((g, i) => <SaveGameComp key={i} savegame={g} loadGame={props.loadGame} />)}
        </div>
        <div className="create-new">
            <button onClick={() => props.createNewGame()} >neues Spiel erstellen</button>
        </div>
        <button onClick={() => props.logout()}>Logout</button>
    </div>
}

const loadGameActionCreator = (savegame: SaveGame) => ({ type: 'LOAD_SAVEGAME', savegame })
const createGameActionCreator = () => ({ type: 'CREATE_GAME' })
const logoutActionCreator = () => ({ type: 'LOGOUT' })

export default connect((state: State) => ({
    savegames: state.savegames
}), {
    loadGame: loadGameActionCreator,
    createNewGame: createGameActionCreator,
    logout: logoutActionCreator
})(LoadNew);