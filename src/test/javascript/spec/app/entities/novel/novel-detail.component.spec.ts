import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Jh9TestModule } from '../../../test.module';
import { NovelDetailComponent } from 'app/entities/novel/novel-detail.component';
import { Novel } from 'app/shared/model/novel.model';

describe('Component Tests', () => {
    describe('Novel Management Detail Component', () => {
        let comp: NovelDetailComponent;
        let fixture: ComponentFixture<NovelDetailComponent>;
        const route = ({ data: of({ novel: new Novel(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Jh9TestModule],
                declarations: [NovelDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(NovelDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(NovelDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.novel).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
