import { map } from 'rxjs/operators';
import { repeat, zipWith } from 'ramda';

import input from './input';

const { min, max } = Math;

input
    .pipe(
        map(entries => {
            let positions = entries.map(({ position }) => position as [number, number]);
            const velocities = entries.map(({ velocity }) => velocity as [number, number]);
            let width = Infinity, height = Infinity;
            for (let seconds = 0; true; seconds++) {
                const xs = positions.map(([x, y]) => x);
                const ys = positions.map(([x, y]) => y);
                const minX = min(...xs);
                const maxX = max(...xs);
                const minY = min(...ys);
                const maxY = max(...ys);
                const prevWidth = width;
                const prevHeight = height;
                width = maxX - minX + 1;
                height = maxY - minY + 1;
                if (width > prevWidth && height > prevHeight) {
                    break;
                }
                console.log('width', width, 'height', height);
                if (height < 30) {
                    console.log('seconds', seconds);
                    show(positions, minX, maxX, minY, maxY);
                }
                positions = zipWith(([x, y], [vx, vy]) => [x + vx, y + vy] as [number, number], positions, velocities);
            }
        }))
    .subscribe(console.log);

function show(positions, minX, maxX, minY, maxY) {
    const sky =
        positions.reduce((sky, [x, y]) => {
            if (!sky[x]) sky[x] = [];
            sky[x][y] = true;
            return sky;
        }, {});
    for (let y = minY; y <= maxY; y++) {
        let row = '';
        for (let x = minX; x <= maxX; x++) {
            row += sky[x] && sky[x][y] ? '#' : '.';
        }
        console.log(row);
    }
}
