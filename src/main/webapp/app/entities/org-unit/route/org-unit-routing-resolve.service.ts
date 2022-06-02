import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrgUnit, OrgUnit } from '../org-unit.model';
import { OrgUnitService } from '../service/org-unit.service';

@Injectable({ providedIn: 'root' })
export class OrgUnitRoutingResolveService implements Resolve<IOrgUnit> {
  constructor(protected service: OrgUnitService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrgUnit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((orgUnit: HttpResponse<OrgUnit>) => {
          if (orgUnit.body) {
            return of(orgUnit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrgUnit());
  }
}
