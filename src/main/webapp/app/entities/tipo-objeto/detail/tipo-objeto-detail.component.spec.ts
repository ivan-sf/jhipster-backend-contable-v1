import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TipoObjetoDetailComponent } from './tipo-objeto-detail.component';

describe('TipoObjeto Management Detail Component', () => {
  let comp: TipoObjetoDetailComponent;
  let fixture: ComponentFixture<TipoObjetoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TipoObjetoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tipoObjeto: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TipoObjetoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TipoObjetoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tipoObjeto on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tipoObjeto).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
