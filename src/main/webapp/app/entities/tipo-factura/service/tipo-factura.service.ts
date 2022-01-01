import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoFactura, getTipoFacturaIdentifier } from '../tipo-factura.model';

export type EntityResponseType = HttpResponse<ITipoFactura>;
export type EntityArrayResponseType = HttpResponse<ITipoFactura[]>;

@Injectable({ providedIn: 'root' })
export class TipoFacturaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-facturas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tipoFactura: ITipoFactura): Observable<EntityResponseType> {
    return this.http.post<ITipoFactura>(this.resourceUrl, tipoFactura, { observe: 'response' });
  }

  update(tipoFactura: ITipoFactura): Observable<EntityResponseType> {
    return this.http.put<ITipoFactura>(`${this.resourceUrl}/${getTipoFacturaIdentifier(tipoFactura) as number}`, tipoFactura, {
      observe: 'response',
    });
  }

  partialUpdate(tipoFactura: ITipoFactura): Observable<EntityResponseType> {
    return this.http.patch<ITipoFactura>(`${this.resourceUrl}/${getTipoFacturaIdentifier(tipoFactura) as number}`, tipoFactura, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoFactura>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoFactura[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTipoFacturaToCollectionIfMissing(
    tipoFacturaCollection: ITipoFactura[],
    ...tipoFacturasToCheck: (ITipoFactura | null | undefined)[]
  ): ITipoFactura[] {
    const tipoFacturas: ITipoFactura[] = tipoFacturasToCheck.filter(isPresent);
    if (tipoFacturas.length > 0) {
      const tipoFacturaCollectionIdentifiers = tipoFacturaCollection.map(tipoFacturaItem => getTipoFacturaIdentifier(tipoFacturaItem)!);
      const tipoFacturasToAdd = tipoFacturas.filter(tipoFacturaItem => {
        const tipoFacturaIdentifier = getTipoFacturaIdentifier(tipoFacturaItem);
        if (tipoFacturaIdentifier == null || tipoFacturaCollectionIdentifiers.includes(tipoFacturaIdentifier)) {
          return false;
        }
        tipoFacturaCollectionIdentifiers.push(tipoFacturaIdentifier);
        return true;
      });
      return [...tipoFacturasToAdd, ...tipoFacturaCollection];
    }
    return tipoFacturaCollection;
  }
}
