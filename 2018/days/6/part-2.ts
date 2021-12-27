import input from './input';
import { map, toArray } from 'rxjs/operators';

const { abs, ceil, floor } = Math;

input
    .pipe(
        toArray(),
        map(coords => {
            const distance = (x, y) => coords.reduce((total, [cx, cy]) => total + abs(cx - x) + abs(cy - y), 0);

            const centerX = coords.reduce((totalX, [x, y]) => totalX + x, 0) / coords.length;
            const centerY = coords.reduce((totalY, [x, y]) => totalY + y, 0) / coords.length;

            let left = floor(centerX);
            let right = ceil(centerX);
            let bottom = floor(centerY);
            let top = ceil(centerY);

            let regionSize = 0, prevRegionSize = null;

            const maxDistance = 10000;

            while (regionSize !== prevRegionSize) {
                prevRegionSize = regionSize;
                for (let x = left; x <= right; x++) {
                    if (distance(x, bottom) < maxDistance) regionSize++;
                    if (distance(x, top) < maxDistance) regionSize++;
                }
                for (let y = bottom + 1; y < top; y++) { // avoid counting the corners twice
                    if (distance(left, y) < maxDistance) regionSize++;
                    if (distance(right, y) < maxDistance) regionSize++;
                }
                left--;
                right++;
                bottom--;
                top++;
            }

            return regionSize;
        })
    ).subscribe(console.log);
