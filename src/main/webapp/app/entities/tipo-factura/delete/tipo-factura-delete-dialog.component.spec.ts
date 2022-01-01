jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TipoFacturaService } from '../service/tipo-factura.service';

import { TipoFacturaDeleteDialogComponent } from './tipo-factura-delete-dialog.component';

describe('TipoFactura Management Delete Component', () => {
  let comp: TipoFacturaDeleteDialogComponent;
  let fixture: ComponentFixture<TipoFacturaDeleteDialogComponent>;
  let service: TipoFacturaService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TipoFacturaDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(TipoFacturaDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TipoFacturaDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TipoFacturaService);
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
