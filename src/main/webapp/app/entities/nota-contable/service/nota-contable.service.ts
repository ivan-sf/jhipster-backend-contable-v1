import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INotaContable, getNotaContableIdentifier } from '../nota-contable.model';

export type EntityResponseType = HttpResponse<INotaContable>;
export type EntityArrayResponseType = HttpResponse<INotaContable[]>;

@Injectable({ providedIn: 'root' })
export class NotaContableService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/nota-contables');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(notaContable: INotaContable): Observable<EntityResponseType> {
    return this.http.post<INotaContable>(this.resourceUrl, notaContable, { observe: 'response' });
  }

  update(notaContable: INotaContable): Observable<EntityResponseType> {
    return this.http.put<INotaContable>(`${this.resourceUrl}/${getNotaContableIdentifier(notaContable) as number}`, notaContable, {
      observe: 'response',
    });
  }

  partialUpdate(notaContable: INotaContable): Observable<EntityResponseType> {
    return this.http.patch<INotaContable>(`${this.resourceUrl}/${getNotaContableIdentifier(notaContable) as number}`, notaContable, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INotaContable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INotaContable[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNotaContableToCollectionIfMissing(
    notaContableCollection: INotaContable[],
    ...notaContablesToCheck: (INotaContable | null | undefined)[]
  ): INotaContable[] {
    const notaContables: INotaContable[] = notaContablesToCheck.filter(isPresent);
    if (notaContables.length > 0) {
      const notaContableCollectionIdentifiers = notaContableCollection.map(
        notaContableItem => getNotaContableIdentifier(notaContableItem)!
      );
      const notaContablesToAdd = notaContables.filter(notaContableItem => {
        const notaContableIdentifier = getNotaContableIdentifier(notaContableItem);
        if (notaContableIdentifier == null || notaContableCollectionIdentifiers.includes(notaContableIdentifier)) {
          return false;
        }
        notaContableCollectionIdentifiers.push(notaContableIdentifier);
        return true;
      });
      return [...notaContablesToAdd, ...notaContableCollection];
    }
    return notaContableCollection;
  }
}
