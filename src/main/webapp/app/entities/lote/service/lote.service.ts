import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILote, getLoteIdentifier } from '../lote.model';

export type EntityResponseType = HttpResponse<ILote>;
export type EntityArrayResponseType = HttpResponse<ILote[]>;

@Injectable({ providedIn: 'root' })
export class LoteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lotes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lote: ILote): Observable<EntityResponseType> {
    return this.http.post<ILote>(this.resourceUrl, lote, { observe: 'response' });
  }

  update(lote: ILote): Observable<EntityResponseType> {
    return this.http.put<ILote>(`${this.resourceUrl}/${getLoteIdentifier(lote) as number}`, lote, { observe: 'response' });
  }

  partialUpdate(lote: ILote): Observable<EntityResponseType> {
    return this.http.patch<ILote>(`${this.resourceUrl}/${getLoteIdentifier(lote) as number}`, lote, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILote>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILote[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLoteToCollectionIfMissing(loteCollection: ILote[], ...lotesToCheck: (ILote | null | undefined)[]): ILote[] {
    const lotes: ILote[] = lotesToCheck.filter(isPresent);
    if (lotes.length > 0) {
      const loteCollectionIdentifiers = loteCollection.map(loteItem => getLoteIdentifier(loteItem)!);
      const lotesToAdd = lotes.filter(loteItem => {
        const loteIdentifier = getLoteIdentifier(loteItem);
        if (loteIdentifier == null || loteCollectionIdentifiers.includes(loteIdentifier)) {
          return false;
        }
        loteCollectionIdentifiers.push(loteIdentifier);
        return true;
      });
      return [...lotesToAdd, ...loteCollection];
    }
    return loteCollection;
  }
}
