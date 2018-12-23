import { map } from 'rxjs/operators';

import input from './input';

const { max } = Math;

input
    .pipe(
        map(({ playerCount, marbleCount }) => {
            let scores = [];
            let circle: any = { value: 0 };
            circle.next = circle;
            circle.prev = circle;
            let current = circle;
            let player = 0;
            for (let marble = 1; marble <= marbleCount; marble++) {
                if (marble % 23 === 0) {
                    const minus7 = current.prev.prev.prev.prev.prev.prev.prev;
                    minus7.prev.next = minus7.next;
                    scores[player] = (scores[player] || 0) + marble + minus7.value;
                    current = minus7.next;
                } else {
                    const placed = { value: marble, prev: current.next, next: current.next.next };
                    current.next.next.prev = placed;
                    current.next.next = placed;
                    current = placed;
                }
                player = (player + 1) % playerCount;
            }
            return max(...scores.filter(score => !!score));
        }))
    .subscribe(console.log);

function printCircle(circle, length) {
    if (length) {
        return ('' + circle.value).padStart(3, ' ') + printCircle(circle.next, length - 1);
    } else {
        return '';
    }
}
