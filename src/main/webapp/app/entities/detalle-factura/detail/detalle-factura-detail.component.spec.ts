import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DetalleFacturaDetailComponent } from './detalle-factura-detail.component';

describe('DetalleFactura Management Detail Component', () => {
  let comp: DetalleFacturaDetailComponent;
  let fixture: ComponentFixture<DetalleFacturaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DetalleFacturaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ detalleFactura: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DetalleFacturaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DetalleFacturaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load detalleFactura on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.detalleFactura).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
