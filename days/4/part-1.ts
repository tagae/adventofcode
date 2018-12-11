import { map } from 'rxjs/operators';

import { guardStatuses } from './common';

guardStatuses.pipe(
    map(statuses => {
        const [bestSleeperId, { sleepingMinutes }] =
            Object
                .entries(statuses)
                .sort(([id1, status1], [id2, status2]) => status2.sleepTime - status1.sleepTime)
                [0];
        const bestMinute =
            Object
                .entries(sleepingMinutes)
                .sort(([id1, min1]: any, [id2, min2]: any) => min2 - min1)
                [0][0];
        return +bestSleeperId * +bestMinute;
    }))
    .subscribe(console.log);
