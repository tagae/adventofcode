import { from } from 'rxjs';
import { count, flatMap, map, reduce } from 'rxjs/operators';

import { paint } from './common';
import input from './input';

input
    .pipe(
        map(line => line.match(/#(\d+) @ (\d+),(\d+): (\d+)x(\d+)/)),
        reduce((canvas, [_, id, left, top, width, height]: any) => paint(canvas, [+id, +left, +top, +width, +height]), {}),
        flatMap(canvas => from(Object.values(canvas)).pipe(flatMap(row => from(Object.values(row))))), // flatten to pixels
        count(square => square.length > 1)) // count pixels painted with more than one color
    .subscribe(console.log);
