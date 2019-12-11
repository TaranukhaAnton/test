import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IArticle, Article } from 'app/shared/model/article.model';
import { ArticleService } from './article.service';
import { IAuthor } from 'app/shared/model/author.model';
import { AuthorService } from 'app/entities/author/author.service';

@Component({
    selector: 'jhi-article-update',
    templateUrl: './article-update.component.html'
})
export class ArticleUpdateComponent implements OnInit {
    isSaving: boolean;

    authors: IAuthor[];

    editForm = this.fb.group({
        id: [],
        articleName: [],
        authors: []
    });

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected articleService: ArticleService,
        protected authorService: AuthorService,
        protected activatedRoute: ActivatedRoute,
        private fb: FormBuilder
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ article }) => {
            this.updateForm(article);
        });
        this.authorService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IAuthor[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAuthor[]>) => response.body)
            )
            .subscribe((res: IAuthor[]) => (this.authors = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    updateForm(article: IArticle) {
        this.editForm.patchValue({
            id: article.id,
            articleName: article.articleName,
            authors: article.authors
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        const article = this.createFromForm();
        if (article.id !== undefined) {
            this.subscribeToSaveResponse(this.articleService.update(article));
        } else {
            this.subscribeToSaveResponse(this.articleService.create(article));
        }
    }

    private createFromForm(): IArticle {
        return {
            ...new Article(),
            id: this.editForm.get(['id']).value,
            articleName: this.editForm.get(['articleName']).value,
            authors: this.editForm.get(['authors']).value
        };
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IArticle>>) {
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
