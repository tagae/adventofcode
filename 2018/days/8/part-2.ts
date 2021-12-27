import { map } from 'rxjs/operators';
import { sum } from 'ramda';

import input from './input';
import { decode } from './common';

input
    .pipe(
        map(encoding => decode(encoding)[0]),
        map(nodeValue))
    .subscribe(console.log);

function nodeValue({ children, metadata }) {
    if (children.length === 0) {
        return sum(metadata)
    } else {
        return sum(
            metadata
                .map(index => children[index - 1])
                .filter(child => !!child)
                .map(nodeValue));
    }
}
