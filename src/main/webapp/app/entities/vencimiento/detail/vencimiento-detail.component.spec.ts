import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VencimientoDetailComponent } from './vencimiento-detail.component';

describe('Vencimiento Management Detail Component', () => {
  let comp: VencimientoDetailComponent;
  let fixture: ComponentFixture<VencimientoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VencimientoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vencimiento: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VencimientoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VencimientoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vencimiento on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vencimiento).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
