import React from 'react';

import Footer from './footer';
import Terminal from './terminal';
import Save from './save';
import Minimap from './minimap';
import Notification from './notification';


/**
 * The ui consists out of two main elements: The terminal which is the means of the user to interact with the game and the minimap which strives to give the user a visual representation of his surroundings.
 */
export default () => <>
  <Terminal />
  <Minimap />
  <Save />
  <Footer />
  <Notification/>
</>;