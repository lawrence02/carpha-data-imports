import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrgUnit, getOrgUnitIdentifier } from '../org-unit.model';

export type EntityResponseType = HttpResponse<IOrgUnit>;
export type EntityArrayResponseType = HttpResponse<IOrgUnit[]>;

@Injectable({ providedIn: 'root' })
export class OrgUnitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/org-units');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(orgUnit: IOrgUnit): Observable<EntityResponseType> {
    return this.http.post<IOrgUnit>(this.resourceUrl, orgUnit, { observe: 'response' });
  }

  update(orgUnit: IOrgUnit): Observable<EntityResponseType> {
    return this.http.put<IOrgUnit>(`${this.resourceUrl}/${getOrgUnitIdentifier(orgUnit) as number}`, orgUnit, { observe: 'response' });
  }

  partialUpdate(orgUnit: IOrgUnit): Observable<EntityResponseType> {
    return this.http.patch<IOrgUnit>(`${this.resourceUrl}/${getOrgUnitIdentifier(orgUnit) as number}`, orgUnit, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrgUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrgUnit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrgUnitToCollectionIfMissing(orgUnitCollection: IOrgUnit[], ...orgUnitsToCheck: (IOrgUnit | null | undefined)[]): IOrgUnit[] {
    const orgUnits: IOrgUnit[] = orgUnitsToCheck.filter(isPresent);
    if (orgUnits.length > 0) {
      const orgUnitCollectionIdentifiers = orgUnitCollection.map(orgUnitItem => getOrgUnitIdentifier(orgUnitItem)!);
      const orgUnitsToAdd = orgUnits.filter(orgUnitItem => {
        const orgUnitIdentifier = getOrgUnitIdentifier(orgUnitItem);
        if (orgUnitIdentifier == null || orgUnitCollectionIdentifiers.includes(orgUnitIdentifier)) {
          return false;
        }
        orgUnitCollectionIdentifiers.push(orgUnitIdentifier);
        return true;
      });
      return [...orgUnitsToAdd, ...orgUnitCollection];
    }
    return orgUnitCollection;
  }
}
