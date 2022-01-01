import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LoteDetailComponent } from './lote-detail.component';

describe('Lote Management Detail Component', () => {
  let comp: LoteDetailComponent;
  let fixture: ComponentFixture<LoteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LoteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ lote: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LoteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LoteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load lote on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.lote).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
