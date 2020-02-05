import React from 'react';
import { connect } from "react-redux";

import { ChooseOptionAction } from '../store/reducer-choose-option';
import { State, Interaction } from '../store/state-types';

import { TerminalHistory } from './terminal-history';
import { TerminalInteraction } from './terminal-interaction';

interface TerminalProps {
    interaction: State['currentInteraction'];
    terminalHistory: State['terminalHistory'];
    chooseOption: (choose: number) => void;
}

/**
 * End component, will be used when no options to choose from are available
 */
const End = ({ interaction }: { interaction: Interaction }) => {
    return <div>
        <div>{interaction.information}</div>
        <h3>Ende</h3>
    </div>
}

/**
 * The terminal functions as the output and the user's input possibility.  
 * @param props 
 */
export const Terminal = (props: TerminalProps) =>
    <div id="terminal">
        <TerminalHistory history={props.terminalHistory} />
        {!props.interaction ? null :
            props.interaction.options.length == 0 ?
                <End interaction={props.interaction} /> :
                <TerminalInteraction interaction={props.interaction} chooseOption={props.chooseOption} />}
    </div>;

const chooseOptionActionCreator = (number: number) => ({ type: 'CHOOSE_OPTION', chooseOption: number }) as ChooseOptionAction;

export default connect((s: State) => ({
    interaction: s.currentInteraction,
    terminalHistory: s.terminalHistory
}), {
    chooseOption: chooseOptionActionCreator
})(Terminal)