import {JSX, useCallback} from "react";
import {mancalaGameSessionApi} from "../services/mancala-game-session.api.ts";
import {useNavigate} from "react-router-dom";

export default function StartPage(): JSX.Element {
    const navigate = useNavigate();
    const onCreateClick = useCallback(() => {
        mancalaGameSessionApi.create()
            .then((result) => {
                navigate(`/game/${result.id}`);
            });
    }, []);
    return (
        <div>
            <button onClick={onCreateClick}>Create new game</button>
        </div>
    )
}