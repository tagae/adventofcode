import { gridPowerLevel, maxPowerSquare } from './common';

const gridPower = gridPowerLevel(7857);

let maxPower = { power: -Infinity, side: 0 };
for (let side = 1; side <= 300; side++) {
    const squarePower = maxPowerSquare(gridPower, side, side);
    if (squarePower.power > maxPower.power) {
        maxPower = { ...squarePower, side };
    }
}

console.log(maxPower);
