import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoObjeto, getTipoObjetoIdentifier } from '../tipo-objeto.model';

export type EntityResponseType = HttpResponse<ITipoObjeto>;
export type EntityArrayResponseType = HttpResponse<ITipoObjeto[]>;

@Injectable({ providedIn: 'root' })
export class TipoObjetoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-objetos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tipoObjeto: ITipoObjeto): Observable<EntityResponseType> {
    return this.http.post<ITipoObjeto>(this.resourceUrl, tipoObjeto, { observe: 'response' });
  }

  update(tipoObjeto: ITipoObjeto): Observable<EntityResponseType> {
    return this.http.put<ITipoObjeto>(`${this.resourceUrl}/${getTipoObjetoIdentifier(tipoObjeto) as number}`, tipoObjeto, {
      observe: 'response',
    });
  }

  partialUpdate(tipoObjeto: ITipoObjeto): Observable<EntityResponseType> {
    return this.http.patch<ITipoObjeto>(`${this.resourceUrl}/${getTipoObjetoIdentifier(tipoObjeto) as number}`, tipoObjeto, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoObjeto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoObjeto[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTipoObjetoToCollectionIfMissing(
    tipoObjetoCollection: ITipoObjeto[],
    ...tipoObjetosToCheck: (ITipoObjeto | null | undefined)[]
  ): ITipoObjeto[] {
    const tipoObjetos: ITipoObjeto[] = tipoObjetosToCheck.filter(isPresent);
    if (tipoObjetos.length > 0) {
      const tipoObjetoCollectionIdentifiers = tipoObjetoCollection.map(tipoObjetoItem => getTipoObjetoIdentifier(tipoObjetoItem)!);
      const tipoObjetosToAdd = tipoObjetos.filter(tipoObjetoItem => {
        const tipoObjetoIdentifier = getTipoObjetoIdentifier(tipoObjetoItem);
        if (tipoObjetoIdentifier == null || tipoObjetoCollectionIdentifiers.includes(tipoObjetoIdentifier)) {
          return false;
        }
        tipoObjetoCollectionIdentifiers.push(tipoObjetoIdentifier);
        return true;
      });
      return [...tipoObjetosToAdd, ...tipoObjetoCollection];
    }
    return tipoObjetoCollection;
  }
}
