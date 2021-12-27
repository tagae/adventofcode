import { flatten, uniq } from 'ramda';

export function requisitesFromPairwiseDeps(pairwiseDeps) {
    return pairwiseDeps.reduce((requisites, [a, b]) => ({
        ...requisites,
        [b]: [...(requisites[b] || []), a]
    }), {})
}

export function workItemsFromRequisites(requisites) {
    return uniq(flatten(Object.entries(requisites) as any)) as string[];
}
