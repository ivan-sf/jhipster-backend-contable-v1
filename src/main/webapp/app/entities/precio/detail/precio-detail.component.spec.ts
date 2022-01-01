import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrecioDetailComponent } from './precio-detail.component';

describe('Precio Management Detail Component', () => {
  let comp: PrecioDetailComponent;
  let fixture: ComponentFixture<PrecioDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrecioDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ precio: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PrecioDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PrecioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load precio on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.precio).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
