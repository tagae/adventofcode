import { reduce } from 'rxjs/operators';

import input from './input';

input
    .pipe(reduce((total, frequency) => total + frequency, 0))
    .subscribe(console.log);
