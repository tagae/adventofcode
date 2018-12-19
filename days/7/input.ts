import { map } from 'rxjs/operators';

import { lines } from '../lines';

export default lines(__dirname, 'input.txt')
    .pipe(
        map(line => line.match(/^step (\w) must be finished before step (\w) can begin/i)),
        map(([_, a, b]) => [a, b] as [string, string]));
