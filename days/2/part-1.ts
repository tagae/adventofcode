import { from } from 'rxjs';
import { flatMap, map, reduce } from 'rxjs/operators';

import input from './input';

input
    .pipe(
        map(charCount),
        map(repetitions),
        map(Object.entries),
        flatMap(entries => from(entries)),
        reduce((totals, [category, count]: [string, number]) => {
            totals[category] = (totals[category] || 0) + count;
            return totals;
        }, {}),
        map(totals => Object.values(totals).reduce((checksum: number, total: number) => checksum * total, 1)))
    .subscribe(console.log);

function charCount(line: string) {
    let counts = {};
    for (let c of line) {
        counts[c] = (counts[c] || 0) + 1;
    }
    return counts;
}

function repetitions(counts) {
    return Object.values(counts).reduce((categories, count: number) => {
        if (count > 1) {
            categories[count] = 1;
        }
        return categories;
    }, {})
}
