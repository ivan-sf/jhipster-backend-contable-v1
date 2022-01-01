import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDetalleFactura, getDetalleFacturaIdentifier } from '../detalle-factura.model';

export type EntityResponseType = HttpResponse<IDetalleFactura>;
export type EntityArrayResponseType = HttpResponse<IDetalleFactura[]>;

@Injectable({ providedIn: 'root' })
export class DetalleFacturaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/detalle-facturas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(detalleFactura: IDetalleFactura): Observable<EntityResponseType> {
    return this.http.post<IDetalleFactura>(this.resourceUrl, detalleFactura, { observe: 'response' });
  }

  update(detalleFactura: IDetalleFactura): Observable<EntityResponseType> {
    return this.http.put<IDetalleFactura>(`${this.resourceUrl}/${getDetalleFacturaIdentifier(detalleFactura) as number}`, detalleFactura, {
      observe: 'response',
    });
  }

  partialUpdate(detalleFactura: IDetalleFactura): Observable<EntityResponseType> {
    return this.http.patch<IDetalleFactura>(
      `${this.resourceUrl}/${getDetalleFacturaIdentifier(detalleFactura) as number}`,
      detalleFactura,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDetalleFactura>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDetalleFactura[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDetalleFacturaToCollectionIfMissing(
    detalleFacturaCollection: IDetalleFactura[],
    ...detalleFacturasToCheck: (IDetalleFactura | null | undefined)[]
  ): IDetalleFactura[] {
    const detalleFacturas: IDetalleFactura[] = detalleFacturasToCheck.filter(isPresent);
    if (detalleFacturas.length > 0) {
      const detalleFacturaCollectionIdentifiers = detalleFacturaCollection.map(
        detalleFacturaItem => getDetalleFacturaIdentifier(detalleFacturaItem)!
      );
      const detalleFacturasToAdd = detalleFacturas.filter(detalleFacturaItem => {
        const detalleFacturaIdentifier = getDetalleFacturaIdentifier(detalleFacturaItem);
        if (detalleFacturaIdentifier == null || detalleFacturaCollectionIdentifiers.includes(detalleFacturaIdentifier)) {
          return false;
        }
        detalleFacturaCollectionIdentifiers.push(detalleFacturaIdentifier);
        return true;
      });
      return [...detalleFacturasToAdd, ...detalleFacturaCollection];
    }
    return detalleFacturaCollection;
  }
}
