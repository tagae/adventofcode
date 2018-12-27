import { map } from 'rxjs/operators';

import input from './input';
import { cartMover } from './common';

input
    .pipe(
        map(({ tracks, carts }) => {
            const moveCart = cartMover(tracks);
            let crash = null;
            const crashed = (row, column) => {
                crash = [column, row];
                return 0;
            };
            const byPositionDetectingCrashes =
                ({ position: [r1, c1] }, { position: [r2, c2] }) => r1 - r2 || c1 - c2 || crashed(r1, c1);
            while (!crash) {
                carts = carts.map(moveCart).sort(byPositionDetectingCrashes);
            }
            return crash.join(',');

        }))
    .subscribe(console.log);
