import React from 'react';
import { connect } from 'react-redux';
import { State } from '../store/state-types';

const correctMap = (map => {
  const preloadElements: HTMLLinkElement[] = Array.from(document.querySelectorAll('[rel="preload"][href*="room-"]'));

  const idMapper = /.*room-(\d+)/;

  ke: for (const key of map.keys()) {
    const fileRoomId = idMapper.exec(map.get(key))[1]
    for (const elem of preloadElements) {
      const hrefRoomId = idMapper.exec(elem.href)[1];
      if (hrefRoomId == fileRoomId) {
        map.set(key, elem.href);
        continue ke;
      }
    }
  }

  return map;

})(new Map([
  [1, 'room-1.png'],
  [2, 'room-1.png'],
  [3, 'room-2.png'],
  [4, 'room-3.png'],
  [5, 'room-4.png']
]));

export const availableMinimapsForRooms = Array.from(correctMap.keys());

export interface MinimapProps {
  room: number;
}

export const Minimap = (props: MinimapProps) =>
  <div id="minimap">
    <img src={correctMap.get(props.room)} alt={"Room-Minimap for Room #" + props.room} />
  </div>

export default connect((state: State) => ({ room: state.room }))(Minimap);