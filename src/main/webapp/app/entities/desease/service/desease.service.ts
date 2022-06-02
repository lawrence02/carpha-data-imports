import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDesease, getDeseaseIdentifier } from '../desease.model';

export type EntityResponseType = HttpResponse<IDesease>;
export type EntityArrayResponseType = HttpResponse<IDesease[]>;

@Injectable({ providedIn: 'root' })
export class DeseaseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/deseases');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(desease: IDesease): Observable<EntityResponseType> {
    return this.http.post<IDesease>(this.resourceUrl, desease, { observe: 'response' });
  }

  update(desease: IDesease): Observable<EntityResponseType> {
    return this.http.put<IDesease>(`${this.resourceUrl}/${getDeseaseIdentifier(desease) as number}`, desease, { observe: 'response' });
  }

  partialUpdate(desease: IDesease): Observable<EntityResponseType> {
    return this.http.patch<IDesease>(`${this.resourceUrl}/${getDeseaseIdentifier(desease) as number}`, desease, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDesease>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDesease[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDeseaseToCollectionIfMissing(deseaseCollection: IDesease[], ...deseasesToCheck: (IDesease | null | undefined)[]): IDesease[] {
    const deseases: IDesease[] = deseasesToCheck.filter(isPresent);
    if (deseases.length > 0) {
      const deseaseCollectionIdentifiers = deseaseCollection.map(deseaseItem => getDeseaseIdentifier(deseaseItem)!);
      const deseasesToAdd = deseases.filter(deseaseItem => {
        const deseaseIdentifier = getDeseaseIdentifier(deseaseItem);
        if (deseaseIdentifier == null || deseaseCollectionIdentifiers.includes(deseaseIdentifier)) {
          return false;
        }
        deseaseCollectionIdentifiers.push(deseaseIdentifier);
        return true;
      });
      return [...deseasesToAdd, ...deseaseCollection];
    }
    return deseaseCollection;
  }
}
