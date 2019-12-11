import { IAuthor } from 'app/shared/model/author.model';

export interface IArticle {
    id?: number;
    articleName?: string;
    authors?: IAuthor[];
}

export class Article implements IArticle {
    constructor(public id?: number, public articleName?: string, public authors?: IAuthor[]) {}
}
