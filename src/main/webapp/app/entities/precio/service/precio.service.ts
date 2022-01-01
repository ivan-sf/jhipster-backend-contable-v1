import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrecio, getPrecioIdentifier } from '../precio.model';

export type EntityResponseType = HttpResponse<IPrecio>;
export type EntityArrayResponseType = HttpResponse<IPrecio[]>;

@Injectable({ providedIn: 'root' })
export class PrecioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/precios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(precio: IPrecio): Observable<EntityResponseType> {
    return this.http.post<IPrecio>(this.resourceUrl, precio, { observe: 'response' });
  }

  update(precio: IPrecio): Observable<EntityResponseType> {
    return this.http.put<IPrecio>(`${this.resourceUrl}/${getPrecioIdentifier(precio) as number}`, precio, { observe: 'response' });
  }

  partialUpdate(precio: IPrecio): Observable<EntityResponseType> {
    return this.http.patch<IPrecio>(`${this.resourceUrl}/${getPrecioIdentifier(precio) as number}`, precio, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPrecio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrecio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPrecioToCollectionIfMissing(precioCollection: IPrecio[], ...preciosToCheck: (IPrecio | null | undefined)[]): IPrecio[] {
    const precios: IPrecio[] = preciosToCheck.filter(isPresent);
    if (precios.length > 0) {
      const precioCollectionIdentifiers = precioCollection.map(precioItem => getPrecioIdentifier(precioItem)!);
      const preciosToAdd = precios.filter(precioItem => {
        const precioIdentifier = getPrecioIdentifier(precioItem);
        if (precioIdentifier == null || precioCollectionIdentifiers.includes(precioIdentifier)) {
          return false;
        }
        precioCollectionIdentifiers.push(precioIdentifier);
        return true;
      });
      return [...preciosToAdd, ...precioCollection];
    }
    return precioCollection;
  }
}
