jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { VencimientoService } from '../service/vencimiento.service';

import { VencimientoDeleteDialogComponent } from './vencimiento-delete-dialog.component';

describe('Vencimiento Management Delete Component', () => {
  let comp: VencimientoDeleteDialogComponent;
  let fixture: ComponentFixture<VencimientoDeleteDialogComponent>;
  let service: VencimientoService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [VencimientoDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(VencimientoDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VencimientoDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(VencimientoService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
