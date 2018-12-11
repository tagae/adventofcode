import { map } from 'rxjs/operators';

import { lines } from '../lines';

export default lines(__dirname, 'input.txt')
    .pipe(
        map(line => line.match(/^.\d{4}-(\d\d)-(\d\d) (\d\d):(\d\d). (.*)$/)),
        map(([_, month, day, hour, minute, description]) =>
            ({ month: +month, day: +day, hour: +hour, minute: +minute, description })));
