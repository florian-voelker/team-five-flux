import React from 'react';
import { Interaction } from '../store/state-types';

interface TerminalOptionDisplayProps extends Interaction {
  chosen?: number;

  active?: boolean;
  wrapAt?: number;
}

export const TerminalOptionDisplay = ({
  originResult,
  information,
  options,

  chosen,
  active = false,
  wrapAt = 2
}: TerminalOptionDisplayProps) => {
  const style: React.CSSProperties = (wrapAt | 0) <= 0 ?
    {
      display: 'block'
    }
    : {
      // gridTemplateRows: "repeat(" + (wrapAt | 0) + ", 1fr)"
      // gridTemplateColumns: "repeat(" + (wrapAt | 0) + ", 1fr)"
    };

  const informationClassName = ['information'];

  if (active)
    informationClassName.push('blinking-typehead');

  return <div className="option-display">
    {originResult ? <p className="origin-result">{originResult}</p> : null}
    <p className={informationClassName.join(' ')}>{information}</p>
    <div className="options" style={style}>
      {options.map((option, optionIndex) => {
        const optionClasses = ['option'];

        if (option.id == chosen)
          optionClasses.push('choose');

        if (!option.available)
          optionClasses.push('locked')

        return <div className={optionClasses.join(' ')} key={optionIndex}>
          <div className="selected-marker">&gt;</div>
          <div className="option-text">{option.option}</div>
        </div>;
      })
      }
    </div>
  </div>
}