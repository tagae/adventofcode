import { map, toArray } from 'rxjs/operators';
import { without } from 'ramda';

import { requisitesFromPairwiseDeps, workItemsFromRequisites } from './common';

import input from './input';

input
    .pipe(
        toArray(),
        map(requisitesFromPairwiseDeps),
        map(stepOrder),
        map(order => order.join('')))
    .subscribe(console.log);

export function stepOrder(requisites) {
    function topoSort(backlog: string[], visited: string[] = []) {
        const [head, ...tail] = backlog;
        if (!head) {
            return visited;
        } else if (visited.includes(head)) {
            return topoSort(tail, visited);
        } else {
            const canBeStarted = step => without(visited, requisites[step] || []).length === 0;
            const [ready] = backlog.filter(canBeStarted).sort();
            return topoSort(without([ready], backlog), [...visited, ready]);
        }
    }
    return topoSort(workItemsFromRequisites(requisites));
}
