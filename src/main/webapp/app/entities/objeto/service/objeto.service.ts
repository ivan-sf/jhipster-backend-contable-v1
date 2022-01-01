import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IObjeto, getObjetoIdentifier } from '../objeto.model';

export type EntityResponseType = HttpResponse<IObjeto>;
export type EntityArrayResponseType = HttpResponse<IObjeto[]>;

@Injectable({ providedIn: 'root' })
export class ObjetoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/objetos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(objeto: IObjeto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(objeto);
    return this.http
      .post<IObjeto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(objeto: IObjeto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(objeto);
    return this.http
      .put<IObjeto>(`${this.resourceUrl}/${getObjetoIdentifier(objeto) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(objeto: IObjeto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(objeto);
    return this.http
      .patch<IObjeto>(`${this.resourceUrl}/${getObjetoIdentifier(objeto) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IObjeto>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IObjeto[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addObjetoToCollectionIfMissing(objetoCollection: IObjeto[], ...objetosToCheck: (IObjeto | null | undefined)[]): IObjeto[] {
    const objetos: IObjeto[] = objetosToCheck.filter(isPresent);
    if (objetos.length > 0) {
      const objetoCollectionIdentifiers = objetoCollection.map(objetoItem => getObjetoIdentifier(objetoItem)!);
      const objetosToAdd = objetos.filter(objetoItem => {
        const objetoIdentifier = getObjetoIdentifier(objetoItem);
        if (objetoIdentifier == null || objetoCollectionIdentifiers.includes(objetoIdentifier)) {
          return false;
        }
        objetoCollectionIdentifiers.push(objetoIdentifier);
        return true;
      });
      return [...objetosToAdd, ...objetoCollection];
    }
    return objetoCollection;
  }

  protected convertDateFromClient(objeto: IObjeto): IObjeto {
    return Object.assign({}, objeto, {
      vencimiento: objeto.vencimiento?.isValid() ? objeto.vencimiento.toJSON() : undefined,
      fechaRegistro: objeto.fechaRegistro?.isValid() ? objeto.fechaRegistro.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.vencimiento = res.body.vencimiento ? dayjs(res.body.vencimiento) : undefined;
      res.body.fechaRegistro = res.body.fechaRegistro ? dayjs(res.body.fechaRegistro) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((objeto: IObjeto) => {
        objeto.vencimiento = objeto.vencimiento ? dayjs(objeto.vencimiento) : undefined;
        objeto.fechaRegistro = objeto.fechaRegistro ? dayjs(objeto.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
