import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TipoFacturaDetailComponent } from './tipo-factura-detail.component';

describe('TipoFactura Management Detail Component', () => {
  let comp: TipoFacturaDetailComponent;
  let fixture: ComponentFixture<TipoFacturaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TipoFacturaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tipoFactura: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TipoFacturaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TipoFacturaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tipoFactura on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tipoFactura).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
