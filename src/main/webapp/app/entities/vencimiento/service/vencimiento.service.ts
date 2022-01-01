import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVencimiento, getVencimientoIdentifier } from '../vencimiento.model';

export type EntityResponseType = HttpResponse<IVencimiento>;
export type EntityArrayResponseType = HttpResponse<IVencimiento[]>;

@Injectable({ providedIn: 'root' })
export class VencimientoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vencimientos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vencimiento: IVencimiento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vencimiento);
    return this.http
      .post<IVencimiento>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vencimiento: IVencimiento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vencimiento);
    return this.http
      .put<IVencimiento>(`${this.resourceUrl}/${getVencimientoIdentifier(vencimiento) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(vencimiento: IVencimiento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vencimiento);
    return this.http
      .patch<IVencimiento>(`${this.resourceUrl}/${getVencimientoIdentifier(vencimiento) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVencimiento>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVencimiento[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVencimientoToCollectionIfMissing(
    vencimientoCollection: IVencimiento[],
    ...vencimientosToCheck: (IVencimiento | null | undefined)[]
  ): IVencimiento[] {
    const vencimientos: IVencimiento[] = vencimientosToCheck.filter(isPresent);
    if (vencimientos.length > 0) {
      const vencimientoCollectionIdentifiers = vencimientoCollection.map(vencimientoItem => getVencimientoIdentifier(vencimientoItem)!);
      const vencimientosToAdd = vencimientos.filter(vencimientoItem => {
        const vencimientoIdentifier = getVencimientoIdentifier(vencimientoItem);
        if (vencimientoIdentifier == null || vencimientoCollectionIdentifiers.includes(vencimientoIdentifier)) {
          return false;
        }
        vencimientoCollectionIdentifiers.push(vencimientoIdentifier);
        return true;
      });
      return [...vencimientosToAdd, ...vencimientoCollection];
    }
    return vencimientoCollection;
  }

  protected convertDateFromClient(vencimiento: IVencimiento): IVencimiento {
    return Object.assign({}, vencimiento, {
      fecha: vencimiento.fecha?.isValid() ? vencimiento.fecha.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fecha = res.body.fecha ? dayjs(res.body.fecha) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((vencimiento: IVencimiento) => {
        vencimiento.fecha = vencimiento.fecha ? dayjs(vencimiento.fecha) : undefined;
      });
    }
    return res;
  }
}
