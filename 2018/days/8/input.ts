import { from } from 'rxjs';
import { flatMap, map, toArray } from 'rxjs/operators';

import { lines } from '../lines';

export default lines(__dirname, 'input.txt')
    .pipe(
        flatMap(line => from(line.split(' '))),
        map(entry => +entry),
        toArray());
