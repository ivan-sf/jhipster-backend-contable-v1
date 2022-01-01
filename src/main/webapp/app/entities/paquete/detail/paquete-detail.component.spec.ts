import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaqueteDetailComponent } from './paquete-detail.component';

describe('Paquete Management Detail Component', () => {
  let comp: PaqueteDetailComponent;
  let fixture: ComponentFixture<PaqueteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaqueteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ paquete: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PaqueteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PaqueteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load paquete on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.paquete).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
