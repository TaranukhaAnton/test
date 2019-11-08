import { IAuthor } from 'app/shared/model/author.model';

export interface INovel {
    id?: number;
    articleName?: string;
    authors?: IAuthor[];
}

export class Novel implements INovel {
    constructor(public id?: number, public articleName?: string, public authors?: IAuthor[]) {}
}
