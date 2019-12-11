import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Jh9SharedModule } from 'app/shared/shared.module';
import { ArticleComponent } from './article.component';
import { ArticleDetailComponent } from './article-detail.component';
import { ArticleUpdateComponent } from './article-update.component';
import { ArticleDeletePopupComponent, ArticleDeleteDialogComponent } from './article-delete-dialog.component';
import { articleRoute, articlePopupRoute } from './article.route';

const ENTITY_STATES = [...articleRoute, ...articlePopupRoute];

@NgModule({
    imports: [Jh9SharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ArticleComponent,
        ArticleDetailComponent,
        ArticleUpdateComponent,
        ArticleDeleteDialogComponent,
        ArticleDeletePopupComponent
    ],
    entryComponents: [ArticleDeleteDialogComponent]
})
export class Jh9ArticleModule {}
