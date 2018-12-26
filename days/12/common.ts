import { findIndex, findLastIndex } from 'ramda';

export function trimState({ start, pots }) {
    const firstPlantIndex = findIndex(x => x as any, pots);
    const lastPlantIndex = findLastIndex(x => x as any, pots);
    return {
        start: start + firstPlantIndex,
        pots: pots.slice(firstPlantIndex, lastPlantIndex + 1)
    };
}

export function stateValue(state) {
    return state.pots
        .reduce((sum, hasPlant, index) => sum + (hasPlant ? index + state.start : 0), 0);
}

export function stateRepresentation(state) {
    return state.pots.reduce((pretty, hasPlant) => pretty + (hasPlant ? '#' : '.'), '');
}
