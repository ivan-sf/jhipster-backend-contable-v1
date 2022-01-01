import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DetallesObjetoDetailComponent } from './detalles-objeto-detail.component';

describe('DetallesObjeto Management Detail Component', () => {
  let comp: DetallesObjetoDetailComponent;
  let fixture: ComponentFixture<DetallesObjetoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DetallesObjetoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ detallesObjeto: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DetallesObjetoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DetallesObjetoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load detallesObjeto on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.detallesObjeto).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
