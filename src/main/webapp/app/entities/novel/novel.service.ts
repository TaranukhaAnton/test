import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { INovel } from 'app/shared/model/novel.model';

type EntityResponseType = HttpResponse<INovel>;
type EntityArrayResponseType = HttpResponse<INovel[]>;

@Injectable({ providedIn: 'root' })
export class NovelService {
    public resourceUrl = SERVER_API_URL + 'api/novels';

    constructor(protected http: HttpClient) {}

    create(novel: INovel): Observable<EntityResponseType> {
        return this.http.post<INovel>(this.resourceUrl, novel, { observe: 'response' });
    }

    update(novel: INovel): Observable<EntityResponseType> {
        return this.http.put<INovel>(this.resourceUrl, novel, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<INovel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<INovel[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
