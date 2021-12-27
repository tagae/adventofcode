import { map } from 'rxjs/operators';

import input from './input';

const { max } = Math;

input
    .pipe(
        map(({ playerCount, marbleCount }) => {
            let scores = [];
            let circle = [0];
            let current = 0, position;
            let player = 0;
            for (let marble = 1; marble <= marbleCount; marble++) {
                if (marble % 23 === 0) {
                    position = (current - 7 + circle.length) % circle.length;
                    const taken = circle[position];
                    circle = [...circle.slice(0, position), ...circle.slice(position + 1, circle.length)];
                    scores[player] = (scores[player] || 0) + marble + taken;
                    current = position;
                } else {
                    if (current + 1 === circle.length) {
                        position = 1;
                    } else {
                        position = current + 2;
                    }
                    circle = [...circle.slice(0, position), marble, ...circle.slice(position, circle.length)];
                    current = position;
                }
                console.log(circle.reduce((s, m) => s + ('' + m).padStart(3, ' '), ''));
                player = (player + 1) % playerCount;
            }
            return max(...scores.filter(score => !!score));
        }))
    .subscribe(console.log);
