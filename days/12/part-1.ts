import { map } from 'rxjs/operators';
import { pathOr, range } from 'ramda';

import input from './input';
import { stateRepresentation, stateValue, trimState } from './common';

input
    .pipe(
        map(({ state, transitions }) => {
            const totalGenerations = 20;
            for (let generation = 1; generation <= totalGenerations; generation++) {
                state = transition(state, transitions);
                console.log(generation, stateRepresentation(state), state.start, state.start + state.pots.length);
            }
            return stateValue(state);
        }))
    .subscribe(console.log);

function transition({ start, pots }, transitions) {
    const wideState = [false, false, false, false, ...pots, false, false, false, false];
    return trimState({
        start: start - 2,
        pots: range(0, pots.length + 4)
            .map(index => wideState.slice(index, index + 5))
            .map(window => pathOr(false, window, transitions))
    });
}
