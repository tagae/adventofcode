import { map } from 'rxjs/operators';

import { guardStatuses } from './common';

guardStatuses.pipe(
    map(statuses => {
        const [id, [minute]] =
            Object
                .entries(statuses)
                .map(([id, status]) => [id, mostSleptMinute(status)])
                .sort(([id1, [minute1, max1]]: any, [id2, [minute2, max2]]: any) => max2 - max1)
                [0];
        return +id * +minute;
    }))
    .subscribe(console.log);

function mostSleptMinute(status: any) {
    return Object
        .entries(status.sleepingMinutes)
        .reduce((max, slept) => slept[1] > max[1] ? slept : max, [null, 0]);
}
