import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInventario, getInventarioIdentifier } from '../inventario.model';

export type EntityResponseType = HttpResponse<IInventario>;
export type EntityArrayResponseType = HttpResponse<IInventario[]>;

@Injectable({ providedIn: 'root' })
export class InventarioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inventarios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(inventario: IInventario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inventario);
    return this.http
      .post<IInventario>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(inventario: IInventario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inventario);
    return this.http
      .put<IInventario>(`${this.resourceUrl}/${getInventarioIdentifier(inventario) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(inventario: IInventario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inventario);
    return this.http
      .patch<IInventario>(`${this.resourceUrl}/${getInventarioIdentifier(inventario) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInventario>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInventario[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInventarioToCollectionIfMissing(
    inventarioCollection: IInventario[],
    ...inventariosToCheck: (IInventario | null | undefined)[]
  ): IInventario[] {
    const inventarios: IInventario[] = inventariosToCheck.filter(isPresent);
    if (inventarios.length > 0) {
      const inventarioCollectionIdentifiers = inventarioCollection.map(inventarioItem => getInventarioIdentifier(inventarioItem)!);
      const inventariosToAdd = inventarios.filter(inventarioItem => {
        const inventarioIdentifier = getInventarioIdentifier(inventarioItem);
        if (inventarioIdentifier == null || inventarioCollectionIdentifiers.includes(inventarioIdentifier)) {
          return false;
        }
        inventarioCollectionIdentifiers.push(inventarioIdentifier);
        return true;
      });
      return [...inventariosToAdd, ...inventarioCollection];
    }
    return inventarioCollection;
  }

  protected convertDateFromClient(inventario: IInventario): IInventario {
    return Object.assign({}, inventario, {
      fechaRegistro: inventario.fechaRegistro?.isValid() ? inventario.fechaRegistro.toJSON() : undefined,
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
      res.body.forEach((inventario: IInventario) => {
        inventario.fechaRegistro = inventario.fechaRegistro ? dayjs(inventario.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
