const { floor } = Math;

function powerLevel([x, y], serial) {
    const rackId = x + 10;
    return floor(((rackId * y + serial) * rackId % 1000) / 100) - 5;
}

export function gridPowerLevel(serial) {
    let gridPower = [];
    for (let x = 1; x <= 300; x++) {
        gridPower[x] = [];
        for (let y = 1; y <= 300; y++) {
            gridPower[x][y] = powerLevel([x, y], serial)
        }
    }
    return gridPower;
}

export function maxPowerSquare(gridPower, squareWidth = 3, squareHeight = 3) {
    let maxSquare = { power: -Infinity, position: null };
    for (let x = 1; x <= 300 - squareWidth + 1; x++) {
        for (let y = 1; y <= 300 - squareHeight + 1; y++) {
            let squarePower = 0;
            for (let i = 0; i < squareWidth; i++) {
                for (let j = 0; j < squareHeight; j++) {
                    squarePower += gridPower[x + i][y + j];
                }
            }
            if (squarePower > maxSquare.power) {
                maxSquare = { power: squarePower, position: [x, y] };
            }
        }
    }
    return maxSquare;
}
