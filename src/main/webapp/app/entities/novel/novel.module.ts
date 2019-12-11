import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Jh9SharedModule } from 'app/shared/shared.module';
import { NovelComponent } from './novel.component';
import { NovelDetailComponent } from './novel-detail.component';
import { NovelUpdateComponent } from './novel-update.component';
import { NovelDeletePopupComponent, NovelDeleteDialogComponent } from './novel-delete-dialog.component';
import { novelRoute, novelPopupRoute } from './novel.route';

const ENTITY_STATES = [...novelRoute, ...novelPopupRoute];

@NgModule({
    imports: [Jh9SharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [NovelComponent, NovelDetailComponent, NovelUpdateComponent, NovelDeleteDialogComponent, NovelDeletePopupComponent],
    entryComponents: [NovelDeleteDialogComponent]
})
export class Jh9NovelModule {}
