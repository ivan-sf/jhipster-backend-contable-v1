import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDepartamento, getDepartamentoIdentifier } from '../departamento.model';

export type EntityResponseType = HttpResponse<IDepartamento>;
export type EntityArrayResponseType = HttpResponse<IDepartamento[]>;

@Injectable({ providedIn: 'root' })
export class DepartamentoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/departamentos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(departamento: IDepartamento): Observable<EntityResponseType> {
    return this.http.post<IDepartamento>(this.resourceUrl, departamento, { observe: 'response' });
  }

  update(departamento: IDepartamento): Observable<EntityResponseType> {
    return this.http.put<IDepartamento>(`${this.resourceUrl}/${getDepartamentoIdentifier(departamento) as number}`, departamento, {
      observe: 'response',
    });
  }

  partialUpdate(departamento: IDepartamento): Observable<EntityResponseType> {
    return this.http.patch<IDepartamento>(`${this.resourceUrl}/${getDepartamentoIdentifier(departamento) as number}`, departamento, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDepartamento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDepartamento[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDepartamentoToCollectionIfMissing(
    departamentoCollection: IDepartamento[],
    ...departamentosToCheck: (IDepartamento | null | undefined)[]
  ): IDepartamento[] {
    const departamentos: IDepartamento[] = departamentosToCheck.filter(isPresent);
    if (departamentos.length > 0) {
      const departamentoCollectionIdentifiers = departamentoCollection.map(
        departamentoItem => getDepartamentoIdentifier(departamentoItem)!
      );
      const departamentosToAdd = departamentos.filter(departamentoItem => {
        const departamentoIdentifier = getDepartamentoIdentifier(departamentoItem);
        if (departamentoIdentifier == null || departamentoCollectionIdentifiers.includes(departamentoIdentifier)) {
          return false;
        }
        departamentoCollectionIdentifiers.push(departamentoIdentifier);
        return true;
      });
      return [...departamentosToAdd, ...departamentoCollection];
    }
    return departamentoCollection;
  }
}
