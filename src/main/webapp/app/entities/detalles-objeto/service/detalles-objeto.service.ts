import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDetallesObjeto, getDetallesObjetoIdentifier } from '../detalles-objeto.model';

export type EntityResponseType = HttpResponse<IDetallesObjeto>;
export type EntityArrayResponseType = HttpResponse<IDetallesObjeto[]>;

@Injectable({ providedIn: 'root' })
export class DetallesObjetoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/detalles-objetos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(detallesObjeto: IDetallesObjeto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(detallesObjeto);
    return this.http
      .post<IDetallesObjeto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(detallesObjeto: IDetallesObjeto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(detallesObjeto);
    return this.http
      .put<IDetallesObjeto>(`${this.resourceUrl}/${getDetallesObjetoIdentifier(detallesObjeto) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(detallesObjeto: IDetallesObjeto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(detallesObjeto);
    return this.http
      .patch<IDetallesObjeto>(`${this.resourceUrl}/${getDetallesObjetoIdentifier(detallesObjeto) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDetallesObjeto>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDetallesObjeto[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDetallesObjetoToCollectionIfMissing(
    detallesObjetoCollection: IDetallesObjeto[],
    ...detallesObjetosToCheck: (IDetallesObjeto | null | undefined)[]
  ): IDetallesObjeto[] {
    const detallesObjetos: IDetallesObjeto[] = detallesObjetosToCheck.filter(isPresent);
    if (detallesObjetos.length > 0) {
      const detallesObjetoCollectionIdentifiers = detallesObjetoCollection.map(
        detallesObjetoItem => getDetallesObjetoIdentifier(detallesObjetoItem)!
      );
      const detallesObjetosToAdd = detallesObjetos.filter(detallesObjetoItem => {
        const detallesObjetoIdentifier = getDetallesObjetoIdentifier(detallesObjetoItem);
        if (detallesObjetoIdentifier == null || detallesObjetoCollectionIdentifiers.includes(detallesObjetoIdentifier)) {
          return false;
        }
        detallesObjetoCollectionIdentifiers.push(detallesObjetoIdentifier);
        return true;
      });
      return [...detallesObjetosToAdd, ...detallesObjetoCollection];
    }
    return detallesObjetoCollection;
  }

  protected convertDateFromClient(detallesObjeto: IDetallesObjeto): IDetallesObjeto {
    return Object.assign({}, detallesObjeto, {
      fechaRegistro: detallesObjeto.fechaRegistro?.isValid() ? detallesObjeto.fechaRegistro.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaRegistro = res.body.fechaRegistro ? dayjs(res.body.fechaRegistro) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((detallesObjeto: IDetallesObjeto) => {
        detallesObjeto.fechaRegistro = detallesObjeto.fechaRegistro ? dayjs(detallesObjeto.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
