import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Novel } from 'app/shared/model/novel.model';
import { NovelService } from './novel.service';
import { NovelComponent } from './novel.component';
import { NovelDetailComponent } from './novel-detail.component';
import { NovelUpdateComponent } from './novel-update.component';
import { NovelDeletePopupComponent } from './novel-delete-dialog.component';
import { INovel } from 'app/shared/model/novel.model';

@Injectable({ providedIn: 'root' })
export class NovelResolve implements Resolve<INovel> {
    constructor(private service: NovelService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<INovel> {
        const id = route.params['id'];
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Novel>) => response.ok),
                map((novel: HttpResponse<Novel>) => novel.body)
            );
        }
        return of(new Novel());
    }
}

export const novelRoute: Routes = [
    {
        path: '',
        component: NovelComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jh9App.novel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: NovelDetailComponent,
        resolve: {
            novel: NovelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jh9App.novel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: NovelUpdateComponent,
        resolve: {
            novel: NovelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jh9App.novel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: NovelUpdateComponent,
        resolve: {
            novel: NovelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jh9App.novel.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const novelPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: NovelDeletePopupComponent,
        resolve: {
            novel: NovelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jh9App.novel.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
