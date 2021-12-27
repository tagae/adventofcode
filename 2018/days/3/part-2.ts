import { count, filter, flatMap, groupBy, map, mergeMap, reduce, tap, toArray } from 'rxjs/operators';
import { from } from 'rxjs';

import { paint } from './common';
import input from './input';

let sizes = {};

input
    .pipe(
        map(line => line.match(/#(\d+) @ (\d+),(\d+): (\d+)x(\d+)/)),
        map(([_, id, left, top, width, height]) => [+id, +left, +top, +width, +height]),
        tap(([id, left, top, width, height]) => sizes[id] = width * height),
        reduce((canvas, [id, left, top, width, height]: any) => paint(canvas, [id, left, top, width, height]), {}),
        flatMap(canvas => from(Object.values(canvas)).pipe(flatMap(row => from(Object.values(row))))), // flatten to pixels
        filter(colors => colors.length === 1), // pick pixels painted with only one color
        map(colors => colors[0]),
        groupBy(color => color), // group by color
        mergeMap(color => color.pipe(toArray())),
        filter(group => group.length === sizes[group[0]]), // total occurrences of unique color === total claim size
        map(group => group[0])) // get color id
    .subscribe(console.log);
