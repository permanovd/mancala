import axios from "axios";

// TODO - move to .env
const API_URL = "http://localhost:8080";

export const mancalaGameSessionApi = {
    create: async (): Promise<MancalaGameSession> => {
        const axiosResponse = await axios.post<MancalaGameSession>(API_URL + '/api/game-session/mancala/create');
        return axiosResponse.data;
    },
    get: async (id: string): Promise<MancalaGameSession> => {
        const axiosResponse = await axios.get<MancalaGameSession>(API_URL + '/api/game-session/mancala/' + id);
        return axiosResponse.data;
    },
    move: async (id: string, player: number, pitNumber: number): Promise<MancalaGameSession> => {
        const axiosResponse = await axios.post<MancalaGameSession>(API_URL + '/api/game-session/mancala/' + id + '/move',
            {player: player, fromPit: pitNumber});
        return axiosResponse.data;
    },
}

export interface MancalaGameSession {
    id: string
    startedAt: string
    endedAt?: string,
    boardState: MancalaBoardState,
    nextPlayerToMove: number,
    gameIsOver: boolean,
    winner: number,
    playerOneScore: number,
    playerTwoScore: number,
}

export interface PitState {
    number: number;
    stonesInside: number;
    mancalaPit: boolean;
}

export interface MancalaBoardState {
    size: number
    initialStonesNumberPerPit: number
    playerOnePits: Array<PitState>
    playerTwoPits: Array<PitState>
    moveLogs: Array<Log>
}

export interface Log {
    id: number
    player: number
    fromPit: number
    nextPlayerToMove: number
    scoreAdded: number
    gameIsOver: boolean
}
