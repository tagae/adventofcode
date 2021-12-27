export function react(polymer) {
    let i = 0, j = i + 1;
    while (j < polymer.length) {
        const first = polymer[i];
        const second = polymer[j];
        if (first !== second && first.toLowerCase() === second.toLowerCase()) {
            delete polymer[i];
            delete polymer[j];
            if (i === 0) {
                i = j + 1;
                j = i + 1;
            } else {
                while (!polymer[i] && i > 0) i--;
                if (polymer[i]) {
                    j = j + 1;
                } else {
                    i = j + 1;
                    j = i + 1;
                }
            }
        } else {
            i = j;
            j = i + 1;
        }
    }
    return polymer;
}
