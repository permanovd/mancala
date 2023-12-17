import {JSX, useEffect, useState} from "react";
import {MancalaGameSession, mancalaGameSessionApi} from "../services/mancala-game-session.api.ts";
import {Link, useParams} from "react-router-dom";
import {Board} from "./board.tsx";

export default function GamePage(): JSX.Element {
    const [session, setSession] = useState<MancalaGameSession | null>(null);
    const {id} = useParams();
    useEffect(() => {
        if (id) {
            mancalaGameSessionApi.get(id)
                .then((result) => {
                    setSession(result);
                });
        }
    }, []);
    if (session === null) return <div>Loading...</div>
    return (
        <>
            <Link to="/">Back to main page</Link>
            <div>
                {session && <>
                    <pre>Game ID: {session.id}</pre>
                    <Board session={session}/>
                </>}
            </div>
        </>
    )
}