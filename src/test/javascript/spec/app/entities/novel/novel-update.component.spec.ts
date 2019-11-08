import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Jh9TestModule } from '../../../test.module';
import { NovelUpdateComponent } from 'app/entities/novel/novel-update.component';
import { NovelService } from 'app/entities/novel/novel.service';
import { Novel } from 'app/shared/model/novel.model';

describe('Component Tests', () => {
    describe('Novel Management Update Component', () => {
        let comp: NovelUpdateComponent;
        let fixture: ComponentFixture<NovelUpdateComponent>;
        let service: NovelService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Jh9TestModule],
                declarations: [NovelUpdateComponent],
                providers: [FormBuilder]
            })
                .overrideTemplate(NovelUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(NovelUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NovelService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Novel(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.updateForm(entity);
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Novel();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.updateForm(entity);
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
