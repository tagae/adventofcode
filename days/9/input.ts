import { map, tap } from 'rxjs/operators';

import { lines } from '../lines';

export default lines(__dirname, 'input.txt')
    .pipe(
        map(line => line.match(/^(\d+) players; last marble is worth (\d+) points(: high score is (\d+))?/i)),
        map(([_, playerCount, marbleCount, __, highScore]) => ({
            playerCount: +playerCount, marbleCount: +marbleCount, highScore: +highScore })));
