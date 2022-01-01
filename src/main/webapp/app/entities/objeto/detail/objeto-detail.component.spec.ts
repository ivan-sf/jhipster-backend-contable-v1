import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ObjetoDetailComponent } from './objeto-detail.component';

describe('Objeto Management Detail Component', () => {
  let comp: ObjetoDetailComponent;
  let fixture: ComponentFixture<ObjetoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ObjetoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ objeto: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ObjetoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ObjetoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load objeto on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.objeto).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
