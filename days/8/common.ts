import { range } from 'ramda';

export function decode(treeEncoding, start = 0) {
    const childCount = treeEncoding[start];
    const metadataCount = treeEncoding[start + 1];
    const [children, metadataStart] =
        range(0, childCount)
            .reduce(([children, position]: any) => {
                const [child, end] = decode(treeEncoding, position);
                return [[...children, child], end];
            }, [[], start + 2]);
    const metadata = treeEncoding.slice(metadataStart, metadataStart + metadataCount);
    return [{ children, metadata }, metadataStart + metadataCount];
}
