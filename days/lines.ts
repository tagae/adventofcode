import { Observable } from 'rxjs';
import { join } from 'path';

const readline = require('readline');
const fs = require('fs');

export function lines(...path): Observable<string> {
    return Observable.create(observer =>
        readline
            .createInterface({
                input: fs.createReadStream(join(...path)),
                crlfDelay: Infinity
            })
            .on('line', line => observer.next(line as string))
            .on('close', () => observer.complete()));
}
