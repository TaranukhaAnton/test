import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { Jh9TestModule } from '../../../test.module';
import { NovelDeleteDialogComponent } from 'app/entities/novel/novel-delete-dialog.component';
import { NovelService } from 'app/entities/novel/novel.service';

describe('Component Tests', () => {
    describe('Novel Management Delete Component', () => {
        let comp: NovelDeleteDialogComponent;
        let fixture: ComponentFixture<NovelDeleteDialogComponent>;
        let service: NovelService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Jh9TestModule],
                declarations: [NovelDeleteDialogComponent]
            })
                .overrideTemplate(NovelDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(NovelDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NovelService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
