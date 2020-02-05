import React, { Component, useState } from 'react';

import { Interaction } from '../store/state-types';
import { TerminalOptionDisplay } from './terminal-option-display';

interface TerminalInteractionProps {
  interaction: Interaction | null;
  chooseOption: (choose: number) => void;
}

interface TerminalInteractionState {
  hovering: number;
}

//shift: boolean | number = true, currentHovering = this.state.hovering, skipFirst = false
function getNextAvailableOption({
  shift = true,
  skipFirst = false,
  currentHovering,
  options
}: {
  shift?: boolean | number,
  skipFirst?: boolean,
  currentHovering: number,
  options: Interaction['options']
}) {
  const shiftAmount = +shift > 0 ? 1 : -1;

  let i = 0;
  let hovering = currentHovering;

  // console.log('hovering', hovering);
  while (true) {
    if (skipFirst) {
      skipFirst = false;
    } else {
      if (i >= options.length)
        throw new Error('could not find any available options');

      if (options[hovering].available)
        return hovering;
    }

    i++;
    hovering = (options.length + currentHovering + i * shiftAmount) % options.length;
  }
}

/**
 * This is the way the user can interact with the game
 */
export class TerminalInteraction extends Component<TerminalInteractionProps, TerminalInteractionState>{

  constructor(props) {
    super(props);

    this.state = { hovering: 0 };
  }

  private keyDownListener = (e: KeyboardEvent) => this.keyDownHandler(e);

  componentDidMount() {
    document.addEventListener('keydown', this.keyDownListener);
  }

  componentWillUnmount() {
    document.removeEventListener('keydown', this.keyDownListener);
  }

  static getDerivedStateFromProps(props: TerminalInteractionProps, state: TerminalInteractionState): TerminalInteractionState {
    if (!props.interaction)
      return null;

    const nextHovering = getNextAvailableOption({
      shift: 1,
      skipFirst: false,
      options: props.interaction.options,
      currentHovering: state.hovering
    });

    if (nextHovering != state.hovering)
      return { ...state, hovering: nextHovering };

    return null;
  }

  keyDownHandler(event: KeyboardEvent) {
    if (!this.props.interaction)
      return;

    const key = event.key;
    if (key == 'ArrowDown' || key == 'ArrowUp') {
      const hovering = getNextAvailableOption({
        shift: key == 'ArrowDown',
        skipFirst: true,
        options: this.props.interaction.options,
        currentHovering: this.state.hovering
      });

      this.setState({ hovering });
    } else if (key == 'Enter') {
      const option = this.props.interaction.options[this.state.hovering];
      this.props.chooseOption(option.id);
      this.setState({
        hovering: 0
      })
    }
  }

  render() {
    const interaction = this.props.interaction;
    const hover = this.state.hovering;
    const hoveringId = interaction ? interaction.options[hover].id : null;

    const className = "input" + (interaction ? "" : " no-input blinking-typehead");
    return <div className={className}>
      {interaction ?
        <TerminalOptionDisplay
          {...interaction}
          chosen={hoveringId}
          active={true}
        /> : null}
    </div>;
  }
}