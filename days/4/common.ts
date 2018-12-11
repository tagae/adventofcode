import input from './input';
import { map, shareReplay, toArray } from 'rxjs/operators';

export type GuardStatus = {
    sleepTime: number,
    sleepingMinutes: { [minute: number]: number },
    sleepingSince?: number
};

export const guardStatuses =
    input
        .pipe(
            toArray(),
            map(events => events.sort((a, b) =>
                a.month - b.month || a.day - b.day || a.hour - b.hour || a.minute - b.minute)),
            map(events => {
                let currentGuardId = null;
                let statuses: { [id: number]: GuardStatus } = {};
                for (let { description, minute } of events) {
                    const beginsShift = description.match(/guard #(\d+) begins shift/i);
                    if (beginsShift) {
                        currentGuardId = +beginsShift[1];
                        if (!statuses[currentGuardId]) {
                            statuses[currentGuardId] = { sleepTime: 0, sleepingMinutes: {} };
                        }
                    } else {
                        const status = statuses[currentGuardId];
                        if (description.match(/falls asleep/i)) {
                            status.sleepingSince = minute;
                        } else if (description.match(/wakes up/i)) {
                            status.sleepTime += minute - status.sleepingSince;
                            for (let i = status.sleepingSince; i < minute; i++) {
                                status.sleepingMinutes[i] = (status.sleepingMinutes[i] || 0) + 1;
                            }
                        }
                    }
                }
                return statuses;
            }));
