import { reduce } from 'rxjs/operators';

import { lines } from '../lines';

const cartRegex = /[<>^v]/g;
const patch = { '<': '-', '>': '-', '^': '|', 'v': '|' };

export default lines(__dirname, 'input.txt')
    .pipe(
        reduce(({ tracks, carts }: any, line, rowNumber) => {
            const row = line.split('');
            while (true) {
                const match = cartRegex.exec(line);
                if (match) {
                    const { 0: cart, index: columnNumber } = match;
                    carts.push({ state: cart, position: [rowNumber, columnNumber], turn: 0 });
                    row[columnNumber] = patch[cart];
                } else {
                    break;
                }
            }
            return { tracks: [...tracks, row.join('')], carts };
        }, { tracks: [], carts: [] }));
