import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CodigoDetailComponent } from './codigo-detail.component';

describe('Codigo Management Detail Component', () => {
  let comp: CodigoDetailComponent;
  let fixture: ComponentFixture<CodigoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CodigoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ codigo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CodigoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CodigoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load codigo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.codigo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
