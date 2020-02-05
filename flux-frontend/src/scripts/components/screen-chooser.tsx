import React from 'react';
import { connect } from 'react-redux';
import { State } from "../store/state-types";
import GameUI from './game-ui';
import LoginRegister from './login-register';
import LoadNew from './load-new';

interface ScreenProps {
    screen: State['screen']
}

export const ScreenChooser = (props: ScreenProps) => {
    let uiElement;

    switch (props.screen) {
        case 'game':
            uiElement = <GameUI />;
            break;

        case 'login+register':
            uiElement = <LoginRegister />;
            break;

        case 'load+new':
            uiElement = <LoadNew />;
            break;

        default:
            uiElement = <span>No component for screen {props.screen}</span>;
            break;
    }

    return <div id="ui-wrap" data-type={props.screen}>{uiElement}</div>;
}

export default connect((state: State) => ({ screen: state.screen }))(ScreenChooser);