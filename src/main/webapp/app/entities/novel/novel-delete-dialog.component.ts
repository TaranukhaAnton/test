import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { INovel } from 'app/shared/model/novel.model';
import { NovelService } from './novel.service';

@Component({
    selector: 'jhi-novel-delete-dialog',
    templateUrl: './novel-delete-dialog.component.html'
})
export class NovelDeleteDialogComponent {
    novel: INovel;

    constructor(protected novelService: NovelService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.novelService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'novelListModification',
                content: 'Deleted an novel'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-novel-delete-popup',
    template: ''
})
export class NovelDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ novel }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(NovelDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.novel = novel;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/novel', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/novel', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
