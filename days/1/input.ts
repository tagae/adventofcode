import { Observable } from 'rxjs';

export default Observable.create(observer => {
        const path = require('path');
        const readline = require('readline');
        const fs = require('fs');
        return readline
            .createInterface({
                input: fs.createReadStream(path.join(process.cwd(), 'days', '1', 'input.txt')),
                crlfDelay: Infinity
            })
            .on('line', line => observer.next(+line))
            .on('close', () => observer.complete());
    });
