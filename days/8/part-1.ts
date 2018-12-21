import { map } from 'rxjs/operators';
import { sum } from 'ramda';

import input from './input';
import { decode } from './common';

input
    .pipe(
        map(encoding => decode(encoding)[0]),
        map(sumMetadata))
    .subscribe(console.log);

function sumMetadata({ children, metadata }) {
    return sum(metadata) + children.reduce((total, child) => total + sumMetadata(child), 0);
}
