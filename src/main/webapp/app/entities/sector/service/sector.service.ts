import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISector, getSectorIdentifier } from '../sector.model';

export type EntityResponseType = HttpResponse<ISector>;
export type EntityArrayResponseType = HttpResponse<ISector[]>;

@Injectable({ providedIn: 'root' })
export class SectorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sectors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sector: ISector): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sector);
    return this.http
      .post<ISector>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sector: ISector): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sector);
    return this.http
      .put<ISector>(`${this.resourceUrl}/${getSectorIdentifier(sector) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(sector: ISector): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sector);
    return this.http
      .patch<ISector>(`${this.resourceUrl}/${getSectorIdentifier(sector) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISector>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISector[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSectorToCollectionIfMissing(sectorCollection: ISector[], ...sectorsToCheck: (ISector | null | undefined)[]): ISector[] {
    const sectors: ISector[] = sectorsToCheck.filter(isPresent);
    if (sectors.length > 0) {
      const sectorCollectionIdentifiers = sectorCollection.map(sectorItem => getSectorIdentifier(sectorItem)!);
      const sectorsToAdd = sectors.filter(sectorItem => {
        const sectorIdentifier = getSectorIdentifier(sectorItem);
        if (sectorIdentifier == null || sectorCollectionIdentifiers.includes(sectorIdentifier)) {
          return false;
        }
        sectorCollectionIdentifiers.push(sectorIdentifier);
        return true;
      });
      return [...sectorsToAdd, ...sectorCollection];
    }
    return sectorCollection;
  }

  protected convertDateFromClient(sector: ISector): ISector {
    return Object.assign({}, sector, {
      fechaRegistro: sector.fechaRegistro?.isValid() ? sector.fechaRegistro.toJSON() : undefined,
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
      res.body.forEach((sector: ISector) => {
        sector.fechaRegistro = sector.fechaRegistro ? dayjs(sector.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
