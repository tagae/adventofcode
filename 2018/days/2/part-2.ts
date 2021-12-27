import { map, toArray } from 'rxjs/operators';

import input from './input';

input.pipe(toArray(), map(findWithMaxDifferences(1))).subscribe(console.log);

function findWithMaxDifferences(maxDifferences) {
    return (strings: string[]) => {
        strings.sort();
        let common = null;
        for (let i = 0; i < strings.length - 1 && !common; i++) {
            common = commonPart(strings[i], strings[i + 1], maxDifferences);
        }
        return common;
    }
}

function commonPart(str1, str2, maxDifferences) {
    let common = '';
    for (let i = 0; i < str1.length; i++) {
        if (str1[i] === str2[i]) {
            common += str1[i];
        } else {
            if (maxDifferences-- === 0) {
                return null;
            }
        }
    }
    return common;
}
