import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { INovel, Novel } from 'app/shared/model/novel.model';
import { NovelService } from './novel.service';
import { IAuthor } from 'app/shared/model/author.model';
import { AuthorService } from 'app/entities/author/author.service';

@Component({
    selector: 'jhi-novel-update',
    templateUrl: './novel-update.component.html'
})
export class NovelUpdateComponent implements OnInit {
    isSaving: boolean;

    authors: IAuthor[];

    editForm = this.fb.group({
        id: [],
        articleName: [],
        authors: []
    });

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected novelService: NovelService,
        protected authorService: AuthorService,
        protected activatedRoute: ActivatedRoute,
        private fb: FormBuilder
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ novel }) => {
            this.updateForm(novel);
        });
        this.authorService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IAuthor[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAuthor[]>) => response.body)
            )
            .subscribe((res: IAuthor[]) => (this.authors = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    updateForm(novel: INovel) {
        this.editForm.patchValue({
            id: novel.id,
            articleName: novel.articleName,
            authors: novel.authors
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        const novel = this.createFromForm();
        if (novel.id !== undefined) {
            this.subscribeToSaveResponse(this.novelService.update(novel));
        } else {
            this.subscribeToSaveResponse(this.novelService.create(novel));
        }
    }

    private createFromForm(): INovel {
        return {
            ...new Novel(),
            id: this.editForm.get(['id']).value,
            articleName: this.editForm.get(['articleName']).value,
            authors: this.editForm.get(['authors']).value
        };
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<INovel>>) {
        result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackAuthorById(index: number, item: IAuthor) {
        return item.id;
    }

    getSelected(selectedVals: any[], option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
