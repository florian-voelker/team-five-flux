export interface SaveGame {
  id: number,
  timestamp: number;
  name: string;
  savegame: number[];
}

export interface Interaction {
  lastRoomId: number | null;
  originResult?: string;
  information: string;
  options: {
    id: number,
    option: string,
    available: boolean
  }[];
}

export interface TerminalChoiceHistoryItem extends Interaction {
  id: number;
  chosen: number;
}

type AuthenticationStates = 'uninitialized' | 'successful' | 'failed' | 'loading'

export interface State {
  terminalHistory: TerminalChoiceHistoryItem[],
  currentInteraction: Interaction | null,
  room: number,
  screen: 'login+register' | 'load+new' | 'game',
  tenMinutes: boolean,
  authentication: {
    loggedIn: boolean,
    lastLoginResult: AuthenticationStates,
    lastLoginReason: string | null,
    lastRegisterResult: AuthenticationStates,
    lastRegisterReason: string | null
  },
  savegames: SaveGame[]
}