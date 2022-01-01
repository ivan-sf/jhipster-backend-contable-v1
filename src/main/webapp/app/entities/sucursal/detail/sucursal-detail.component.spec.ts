import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SucursalDetailComponent } from './sucursal-detail.component';

describe('Sucursal Management Detail Component', () => {
  let comp: SucursalDetailComponent;
  let fixture: ComponentFixture<SucursalDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SucursalDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sucursal: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SucursalDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SucursalDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sucursal on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sucursal).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
