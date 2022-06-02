import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDesease, Desease } from '../desease.model';
import { DeseaseService } from '../service/desease.service';

@Injectable({ providedIn: 'root' })
export class DeseaseRoutingResolveService implements Resolve<IDesease> {
  constructor(protected service: DeseaseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDesease> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((desease: HttpResponse<Desease>) => {
          if (desease.body) {
            return of(desease.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Desease());
  }
}
