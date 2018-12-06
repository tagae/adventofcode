import { Observable, of } from 'rxjs';
import { filter, map, repeat, scan, startWith, take } from 'rxjs/operators';

import input from './input';

let source: Observable<number> = input;

// source = of(+1, -1);
// source = of(+3, +3, +4, -2, -4);
// source = of(-6, +3, +8, +5, -6);
// source = of(+7, +7, -2, -7, -4);

source.pipe(
    repeat(),
    startWith(0),
    scan((total, frequency) => total + frequency, 0),
    scan((seen, total: number) => ([...seen, total]), []),
    map(seen => seen.sort()),
    map(findRepeated),
    filter(repeated => typeof repeated === 'number'),
    take(1))
    .subscribe(firstRepeated => console.log('first repeated', firstRepeated));

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
