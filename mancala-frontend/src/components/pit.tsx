import {useCallback} from "react";
import {PitState} from "../services/mancala-game-session.api.ts";

interface Props {
    pit: PitState;
    disabled: boolean;
    onClick: (pit: PitState) => void;
}

export function PitComponent(props: Props): JSX.Element {
    const onClick = useCallback((pit: PitState) => {
        if (props.disabled) return;
        props.onClick(pit);
    }, [props]);
    return (
        <div style={props.disabled ? {"cursor": "not-allowed"} : {}}
             className={props.pit.mancalaPit ? "pit store" : "pit"}
             onClick={() => onClick(props.pit)}>
            {!props.pit.mancalaPit && props.pit.stonesInside > 0 && <div className={"stones"}>
                {Array.from(Array(props.pit.stonesInside).keys()).map(() => {
                    return "🌕 ";
                })}
            </div>}
            <div className={"stones-count"}>{props.pit.stonesInside}</div>
        </div>
    )
}