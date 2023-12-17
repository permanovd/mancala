import {JSX, useCallback, useState} from "react";
import {
    MancalaBoardState,
    MancalaGameSession,
    mancalaGameSessionApi,
    PitState
} from "../services/mancala-game-session.api.ts";
import {PitComponent as Pit} from "./pit.tsx";
import {toast} from "react-toastify";

export interface Props {
    session: MancalaGameSession;
}

export function Board(props: Props): JSX.Element {
    const [player, setPlayer] = useState<number>(props.session.nextPlayerToMove);
    const [board, setBoard] = useState<MancalaBoardState>(props.session.boardState);
    const [session, setSession] = useState<MancalaGameSession>(props.session);
    const onClick = useCallback((pit: PitState) => {
        mancalaGameSessionApi.move(props.session.id, player, pit.number).then((result) => {
            toast("Player " + player + " moved from pit " + pit.number + ". next one is " + result.nextPlayerToMove);
            setPlayer(result.nextPlayerToMove);
            setBoard(result.boardState);
            setSession(result);
        });
    }, [player, board]);
    return (
        <>
            {session.gameIsOver ? <h3>Game is over, winner is player {session.winner}</h3> :
                <h3>Current player to move: {player}</h3>}
            <div className={"board active-player-" + player}>
                <div className={"player-2 player-side " + (player == 2 ? "active" : "inactive")}>
                    <Pit pit={board.playerTwoPits[6]} disabled={player == 1} onClick={onClick}></Pit>
                    <Pit pit={board.playerTwoPits[5]} disabled={player == 1} onClick={onClick}></Pit>
                    <Pit pit={board.playerTwoPits[4]} disabled={player == 1} onClick={onClick}></Pit>
                    <Pit pit={board.playerTwoPits[3]} disabled={player == 1} onClick={onClick}></Pit>
                    <Pit pit={board.playerTwoPits[2]} disabled={player == 1} onClick={onClick}></Pit>
                    <Pit pit={board.playerTwoPits[1]} disabled={player == 1} onClick={onClick}></Pit>
                    <Pit pit={board.playerTwoPits[0]} disabled={player == 1} onClick={onClick}></Pit>
                </div>

                <div className={"player-1 player-side " + (player == 1 ? "active" : "inactive")}>
                    <Pit pit={board.playerOnePits[0]} disabled={player == 2} onClick={onClick}></Pit>
                    <Pit pit={board.playerOnePits[1]} disabled={player == 2} onClick={onClick}></Pit>
                    <Pit pit={board.playerOnePits[2]} disabled={player == 2} onClick={onClick}></Pit>
                    <Pit pit={board.playerOnePits[3]} disabled={player == 2} onClick={onClick}></Pit>
                    <Pit pit={board.playerOnePits[4]} disabled={player == 2} onClick={onClick}></Pit>
                    <Pit pit={board.playerOnePits[5]} disabled={player == 2} onClick={onClick}></Pit>
                    <Pit pit={board.playerOnePits[6]} disabled={player == 2} onClick={onClick}></Pit>
                </div>
            </div>
            <div className={"move-logs"}>
                <table>
                    <thead>
                    <tr>
                        <th>Move number</th>
                        <th>Player</th>
                        <th>Moved from pit</th>
                        <th>Score added</th>
                        <th>Next player to move</th>
                    </tr>
                    </thead>
                    <tbody>
                    {board.moveLogs.map(item => {
                        return (
                            <tr key={item.id}>
                                <td>{ item.id }</td>
                                <td>{ item.player }</td>
                                <td>{ item.fromPit }</td>
                                <td>{ item.scoreAdded }</td>
                                <td>{ item.nextPlayerToMove }</td>
                            </tr>
                        );
                    })}
                    </tbody>
                </table>
            </div>
        </>
    );
}