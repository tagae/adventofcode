import { map } from 'rxjs/operators';

import { react } from './common';

import input from './input';

input
    .pipe(
        map(reactRemovingTypes),
        map(polymer => polymer.length))
    .subscribe(console.log);

function reactRemovingTypes(polymer: string) {
    const elements = polymer.split('');
    let shortest = polymer;
    for (let unit of 'abcdefghijklmnopqrstuvwxyz') {
        const result = react(elements.filter(element => element.toLowerCase() !== unit)).join('');
        if (result.length < shortest.length) {
            shortest = result;
        }
    }
    return shortest;
}
