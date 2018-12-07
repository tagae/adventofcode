import { scan, startWith, toArray } from 'rxjs/operators';

import input from './input';

input
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
                        console.log(k * t + D[i]);
                        return;
                    }
                }
            }
        }
    });
