import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NotaContableDetailComponent } from './nota-contable-detail.component';

describe('NotaContable Management Detail Component', () => {
  let comp: NotaContableDetailComponent;
  let fixture: ComponentFixture<NotaContableDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NotaContableDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ notaContable: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(NotaContableDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(NotaContableDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load notaContable on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.notaContable).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
