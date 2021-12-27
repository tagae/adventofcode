import { map } from 'rxjs/operators';
import { equals } from 'ramda';

import input from './input';
import { stateValue, trimState } from './common';

input
    .pipe(
        map(({ state, transitions }) => {
            const totalGenerations = 50000000000;
            let generation = 0;
            let previous = { start: 0, pots: [] };
            while (generation < totalGenerations && !equals(previous.pots, state.pots)) {
                previous = state;
                state = transition(state, transitions);
                generation++;
            }
            if (generation < totalGenerations) {
                const shift = state.start - previous.start;
                state = { start: state.start + shift * (totalGenerations - generation), pots: state.pots };
            }
            return stateValue(state);
        }))
    .subscribe(console.log);

function transition({ start, pots }, transitions) {
    const newPots = new Array(pots.length + 4);
    newPots[0] = transitions['false']['false']['false']['false'][pots[0]];
    newPots[1] = transitions['false']['false']['false'][pots[0]][pots[1]];
    newPots[2] = transitions['false']['false'][pots[0]][pots[1]][pots[2]];
    newPots[3] = transitions['false'][pots[0]][pots[1]][pots[2]][pots[3]];
    for (let i = 4; i < pots.length; i++) {
        newPots[i] = transitions[pots[i - 4]][pots[i - 3]][pots[i - 2]][pots[i - 1]][pots[i]];
    }
    const l = pots.length;
    newPots[l] = transitions[pots[l - 4]][pots[l - 3]][pots[l - 2]][pots[l - 1]]['false'];
    newPots[l + 1] = transitions[pots[l - 3]][pots[l - 2]][pots[l - 1]]['false']['false'];
    newPots[l + 2] = transitions[pots[l - 2]][pots[l - 1]]['false']['false']['false'];
    newPots[l + 3] = transitions[pots[l - 1]]['false']['false']['false']['false'];
    return trimState({ start: start - 2, pots: newPots });
}
