import { map, toArray } from 'rxjs/operators';
import { assocPath, countBy, flatten, pathOr, reduce, uniq, values, without } from 'ramda';

import input from './input';

const { max, abs } = Math;

input
    .pipe(
        toArray(),
        map(coords => {
            const width = coords.reduce((width, [x, y]) => max(width, x), 0);
            const height = coords.reduce((height, [x, y]) => max(height, y), 0);
            const distances = findDistances(coords, width, height).filter(({ equal }) => !equal);
            const infinite = uniq(
                distances
                    .filter(({ x, y, equal }) => !equal && (x === 0 || y === 0 || x === width || y === height))
                    .map(({ closest }) => closest));
            const finite = without(infinite, distances.map(({ closest }) => closest));
            return reduce(max, 0, values(countBy(coord => coord, finite)));
        })
    ).subscribe(console.log);

function findDistances(coords, width, height) {
    let distances = [];
    for (let [x, y] of coords) {
        for (let i = 0; i <= width; i++) {
            for (let j = 0; j <= height; j++) {
                const currentDistance = abs(x - i) + abs(y - j);
                const cell = pathOr({ distance: Infinity }, [i, j], distances);
                if (currentDistance < cell.distance) {
                    distances = assocPath([i, j], { x: i, y: j, closest: [x, y], distance: currentDistance }, distances)
                } else if (currentDistance === cell.distance) {
                    distances = assocPath([i, j], { ...cell, equal: true }, distances);
                }
            }
        }
    }
    return flatten(distances);
}
