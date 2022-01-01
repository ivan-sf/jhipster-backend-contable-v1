import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRegimen, getRegimenIdentifier } from '../regimen.model';

export type EntityResponseType = HttpResponse<IRegimen>;
export type EntityArrayResponseType = HttpResponse<IRegimen[]>;

@Injectable({ providedIn: 'root' })
export class RegimenService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/regimen');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(regimen: IRegimen): Observable<EntityResponseType> {
    return this.http.post<IRegimen>(this.resourceUrl, regimen, { observe: 'response' });
  }

  update(regimen: IRegimen): Observable<EntityResponseType> {
    return this.http.put<IRegimen>(`${this.resourceUrl}/${getRegimenIdentifier(regimen) as number}`, regimen, { observe: 'response' });
  }

  partialUpdate(regimen: IRegimen): Observable<EntityResponseType> {
    return this.http.patch<IRegimen>(`${this.resourceUrl}/${getRegimenIdentifier(regimen) as number}`, regimen, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRegimen>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRegimen[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRegimenToCollectionIfMissing(regimenCollection: IRegimen[], ...regimenToCheck: (IRegimen | null | undefined)[]): IRegimen[] {
    const regimen: IRegimen[] = regimenToCheck.filter(isPresent);
    if (regimen.length > 0) {
      const regimenCollectionIdentifiers = regimenCollection.map(regimenItem => getRegimenIdentifier(regimenItem)!);
      const regimenToAdd = regimen.filter(regimenItem => {
        const regimenIdentifier = getRegimenIdentifier(regimenItem);
        if (regimenIdentifier == null || regimenCollectionIdentifiers.includes(regimenIdentifier)) {
          return false;
        }
        regimenCollectionIdentifiers.push(regimenIdentifier);
        return true;
      });
      return [...regimenToAdd, ...regimenCollection];
    }
    return regimenCollection;
  }
}
