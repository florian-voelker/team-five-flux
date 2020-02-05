import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';

import store from './store/store';
import ScreenChooser from './components/screen-chooser';

setTimeout(() => {
  store.dispatch({ type: 'TEST_ACTION', a: 22 });
}, 5000);

document.addEventListener('DOMContentLoaded', () => {
  ReactDOM.render(<Provider store={store}><ScreenChooser /></Provider>, document.getElementsByTagName('main')[0]);
  store.dispatch({ type: 'INITIALIZE' });
});