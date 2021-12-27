import { map } from 'rxjs/operators';

import { react } from './common';

import input from './input';

input
    .pipe(
        map(polymer => polymer.split('')),
        map(react),
        map(result => result.join('')),
        map(polymer => polymer.length))
    .subscribe(console.log);
