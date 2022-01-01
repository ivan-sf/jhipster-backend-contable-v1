import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RegimenDetailComponent } from './regimen-detail.component';

describe('Regimen Management Detail Component', () => {
  let comp: RegimenDetailComponent;
  let fixture: ComponentFixture<RegimenDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegimenDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ regimen: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RegimenDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RegimenDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load regimen on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.regimen).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
