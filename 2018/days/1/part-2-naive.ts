import { filter, map, repeat, scan, startWith, take } from 'rxjs/operators';

import input from './input';

input
    .pipe(
        repeat(),
        startWith(0),
        scan((total, frequency) => total + frequency, 0),
        scan((seen, total: number) => ([...seen, total]), []),
        map(seen => seen.sort()),
        map(findRepeated),
        filter(repeated => typeof repeated === 'number'),
        take(1))
    .subscribe(console.log);

function findRepeated(sortedElements: number[]) {
    let last = undefined;
    for (const element of sortedElements) {
        if (element === last) {
            return element;
        }
        last = element;
    }
    return null;
}
