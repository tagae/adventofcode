import { Observable, of } from 'rxjs';
import { reduce } from 'rxjs/operators';

import input from './input';

let source: Observable<number> = input;

source
    .pipe(reduce((total, frequency) => total + frequency, 0))
    .subscribe(total => console.log('frequency', total));
