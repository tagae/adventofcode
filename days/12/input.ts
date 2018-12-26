import { reduce } from 'rxjs/operators';
import { assocPath } from 'ramda';

import { lines } from '../lines';

export default lines(__dirname, 'input.txt')
    .pipe(
        reduce((input: any, line: string) => {
            const initialStateMatch = line.match(/initial state: ([#.]+)/);
            if (initialStateMatch) {
                return {
                    ...input,
                    state: { start: 0, pots: parseState(initialStateMatch[1]) }
                };
            } else {
                const transitionMatch = line.match(/([.#]+) => ([#.])/);
                if (transitionMatch) {
                    const neighbours = parseState(transitionMatch[1]);
                    const outcome = transitionMatch[2] === '#';
                    return {
                        ...input,
                        transitions: assocPath(neighbours as any, outcome, input.transitions)
                    }
                } else {
                    return input;
                }
            }
        }, {}));

function parseState(state: string) {
    return state.split('').map((indicator) => indicator === '#');
}
