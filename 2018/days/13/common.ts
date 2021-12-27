const { isArray } = Array;

const transitions = {
    '>': { move: [0, 1], state: { '-': '>', '\\': 'v', '/': '^', '+': ['^', '>', 'v'] } },
    '<': { move: [0, -1], state: { '-': '<', '\\': '^', '/': 'v', '+': ['v', '<', '^'] } },
    'v': { move: [1, 0], state: { '|': 'v', '\\': '>', '/': '<', '+': ['>', 'v', '<'] } },
    '^': { move: [-1, 0], state: { '|': '^', '\\': '<', '/': '>', '+': ['<', '^', '>'] } }
};

export function cartMover(tracks) {
    return function moveCart({ state, position, turn }) {
        const transition = transitions[state];
        const [row, column] = position;
        const [dy, dx] = transition.move;
        const nextPosition = [row + dy, column + dx];
        const [nextRow, nextColumn] = nextPosition;
        const trackAhead = tracks[nextRow][nextColumn];
        let nextTurn = turn;
        let nextState = transition.state[trackAhead];
        if (isArray(nextState)) {
            nextState = nextState[turn];
            nextTurn = (turn + 1) % 3;
        }
        return {
            state: nextState,
            position: nextPosition,
            turn: nextTurn
        };
    }
}
