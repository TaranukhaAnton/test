import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INovel } from 'app/shared/model/novel.model';

@Component({
    selector: 'jhi-novel-detail',
    templateUrl: './novel-detail.component.html'
})
export class NovelDetailComponent implements OnInit {
    novel: INovel;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ novel }) => {
            this.novel = novel;
        });
    }

    previousState() {
        window.history.back();
    }
}
