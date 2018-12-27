import { map } from 'rxjs/operators';

import { cartMover } from './common';

import input from './input';

export const sameLocation =
    ({ position: [r1, c1] }) => ({ position: [r2, c2] }) => r1 === r2 && c1 === c2;

export const byPosition =
    ({ position: [r1, c1] }, { position: [r2, c2] }) => r1 - r2 || c1 - c2;

input
    .pipe(
        map(({ tracks, carts }) => {
                const moveCart = cartMover(tracks);
                while (carts.length > 1) {
                    for (let i = 0; i < carts.length; i++) {
                        const next = carts.shift();
                        if (next.state !== 'X') {
                            const moved: any = moveCart(next);
                            const crash = carts.findIndex(sameLocation(moved));
                            if (crash >= 0) {
                                moved.state = 'X';
                                carts[crash].state = 'X';
                            }
                            carts.push(moved);
                        } else {
                            carts.push(next);
                        }
                    }
                    carts = carts.filter(cart => cart.state !== 'X');
                    carts.sort(byPosition);
                }

                const [row, column] = carts[0].position;
                return [column, row].join(',');
            }
        ))
    .subscribe(console.log);
