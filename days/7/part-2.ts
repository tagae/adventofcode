import { map, toArray } from 'rxjs/operators';

import input from './input';
import { requisitesFromPairwiseDeps, workItemsFromRequisites } from './common';
import { without } from 'ramda';

input
    .pipe(
        toArray(),
        map(requisitesFromPairwiseDeps),
        map(doWork)
    ).subscribe(console.log);

const totalWorkers = 5;
const baseStepSize = 60;

function doWork(requisites) {
    let remaining = workItemsFromRequisites(requisites),
        ongoing = [],
        done = [],
        time = 0;

    const canBeStarted = step => without(done, requisites[step] || []).length === 0;
    const byDeadline = ({ deadline: d1 }, { deadline: d2 }) => d1 - d2;

    while (remaining.length + ongoing.length > 0) {
        const [next] = remaining.filter(canBeStarted).sort();

        if (next && ongoing.length < totalWorkers) {
            remaining = without([next], remaining);
            ongoing = [{ item: next, deadline: time + workItemDuration(next) }, ...ongoing];
        } else {
            const { deadline, item } = ongoing.sort(byDeadline).shift();
            time = deadline;
            done = [...done, item];
        }
    }
    return { steps: done.join(''), time };
}

function workItemDuration(workItem: string) {
    return baseStepSize + workItem.charCodeAt(0) - 'A'.charCodeAt(0) + 1;
}
