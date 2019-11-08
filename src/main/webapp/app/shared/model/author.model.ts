export interface IAuthor {
    id?: number;
    countryName?: string;
}

export class Author implements IAuthor {
    constructor(public id?: number, public countryName?: string) {}
}
