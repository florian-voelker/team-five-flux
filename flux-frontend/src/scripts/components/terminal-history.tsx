import React from 'react';

import { TerminalChoiceHistoryItem } from "../store/state-types";
import { TerminalOptionDisplay } from './terminal-option-display';

interface TerminalHistoryProps {
  history: TerminalChoiceHistoryItem[];
}

/**
 * The terminal history displays the selections that the user has already made. This provides the user with the option to look up information without the need of repeating specific actions (if that is even possible in the first way).
 * @param props the history to be displayed
 */
export const TerminalHistory = (props: TerminalHistoryProps) =>
  <div className="history">
    {props.history.map((entry, index) =>
      <TerminalOptionDisplay
        key={index}
        {...entry}
      />
    )}
  </div>;
