import { Source } from 'postcss';
import { Journalist } from './journalist';
import { Posting } from './posting';

export class Commentary {
  constructor(
    public id: string,
    public version: number,
    public score: number,
    public vote: 1 | 0 | -1 = 0,
    public text: string,
    public published: Date,
    public publishedBy: Journalist,
    public relatedPost: Posting,
    public sources: Source[],
  ) {}
}
