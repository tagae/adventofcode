import { scan, startWith, toArray } from 'rxjs/operators';
import { Observable, of } from 'rxjs';

import input from './input';

let source: Observable<number> = input;

// source = of(+1, -1);
// source = of(+3, +3, +4, -2, -4);
// source = of(-6, +3, +8, +5, -6);
// source = of(+7, +7, -2, -7, -4);

source
    .pipe(
        scan((total, frequency) => total + frequency, 0),
        startWith(0),
        toArray())
    .subscribe(D => {
        const t = D[D.length - 1];
        for (let k = 1; true; k++) {
            for (let i = 0; i < D.length - 1; i++) {
                for (let j = 0; j < D.length - 1; j++) {
                    if (k * t === D[j] - D[i]) {
                        console.log('first repeated', k * t + D[i]);
                        return;
                    }
                }
            }
        }
    });
