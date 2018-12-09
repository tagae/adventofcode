export function paint(canvas, [id, left, top, width, height]) {
    for (let i = 0; i < width; i++) {
        for (let j = 0; j < height; j++) {
            if (!canvas[left + i]) {
                canvas[left + i] = {}
            }
            const ids = canvas[left + i][top + j] || [];
            canvas[left + i][top + j] = [...ids, id];
        }
    }
    return canvas;
}
