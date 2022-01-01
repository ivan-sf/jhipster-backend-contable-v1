import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaquete, getPaqueteIdentifier } from '../paquete.model';

export type EntityResponseType = HttpResponse<IPaquete>;
export type EntityArrayResponseType = HttpResponse<IPaquete[]>;

@Injectable({ providedIn: 'root' })
export class PaqueteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/paquetes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paquete: IPaquete): Observable<EntityResponseType> {
    return this.http.post<IPaquete>(this.resourceUrl, paquete, { observe: 'response' });
  }

  update(paquete: IPaquete): Observable<EntityResponseType> {
    return this.http.put<IPaquete>(`${this.resourceUrl}/${getPaqueteIdentifier(paquete) as number}`, paquete, { observe: 'response' });
  }

  partialUpdate(paquete: IPaquete): Observable<EntityResponseType> {
    return this.http.patch<IPaquete>(`${this.resourceUrl}/${getPaqueteIdentifier(paquete) as number}`, paquete, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaquete>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaquete[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPaqueteToCollectionIfMissing(paqueteCollection: IPaquete[], ...paquetesToCheck: (IPaquete | null | undefined)[]): IPaquete[] {
    const paquetes: IPaquete[] = paquetesToCheck.filter(isPresent);
    if (paquetes.length > 0) {
      const paqueteCollectionIdentifiers = paqueteCollection.map(paqueteItem => getPaqueteIdentifier(paqueteItem)!);
      const paquetesToAdd = paquetes.filter(paqueteItem => {
        const paqueteIdentifier = getPaqueteIdentifier(paqueteItem);
        if (paqueteIdentifier == null || paqueteCollectionIdentifiers.includes(paqueteIdentifier)) {
          return false;
        }
        paqueteCollectionIdentifiers.push(paqueteIdentifier);
        return true;
      });
      return [...paquetesToAdd, ...paqueteCollection];
    }
    return paqueteCollection;
  }
}
