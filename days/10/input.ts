import { map, toArray } from 'rxjs/operators';

import { lines } from '../lines';

export default lines(__dirname, 'input.txt')
    .pipe(
        map(line => line.match(/^position=<\s*(-?\d+),\s*(-?\d+)> velocity=<\s*(-?\d+),\s*(-?\d+)>/i)),
        map(([_, x, y, vx, vy]) => ({ position: [+x, +y], velocity: [+vx, +vy] })),
        toArray());
