import { map } from 'rxjs/operators';

import { lines } from '../lines';

export default lines(__dirname, 'input.txt')
    .pipe(
        map(line => line.split(',')),
        map(([x, y]) => [+x, +y] as [number, number]));
