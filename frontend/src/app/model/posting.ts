import { Platform } from '@angular/cdk/platform';
import { Commentary } from './commentary';
import { Source } from './source';
import { Topic } from './topic';

export class Posting {
  constructor(
    public id: string,
    public summary: string,
    public published: Date,
    public topic: Topic,
    public originatesFrom: Platform,
    public commentaries: Commentary[],
    public sources: Source[],
  ) {}
}
