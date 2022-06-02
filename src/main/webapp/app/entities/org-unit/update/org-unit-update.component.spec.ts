import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrgUnitService } from '../service/org-unit.service';
import { IOrgUnit, OrgUnit } from '../org-unit.model';

import { OrgUnitUpdateComponent } from './org-unit-update.component';

describe('OrgUnit Management Update Component', () => {
  let comp: OrgUnitUpdateComponent;
  let fixture: ComponentFixture<OrgUnitUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orgUnitService: OrgUnitService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrgUnitUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OrgUnitUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrgUnitUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orgUnitService = TestBed.inject(OrgUnitService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const orgUnit: IOrgUnit = { id: 456 };

      activatedRoute.data = of({ orgUnit });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(orgUnit));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrgUnit>>();
      const orgUnit = { id: 123 };
      jest.spyOn(orgUnitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgUnit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orgUnit }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(orgUnitService.update).toHaveBeenCalledWith(orgUnit);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrgUnit>>();
      const orgUnit = new OrgUnit();
      jest.spyOn(orgUnitService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgUnit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orgUnit }));
      saveSubject.complete();

      // THEN
      expect(orgUnitService.create).toHaveBeenCalledWith(orgUnit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrgUnit>>();
      const orgUnit = { id: 123 };
      jest.spyOn(orgUnitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgUnit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orgUnitService.update).toHaveBeenCalledWith(orgUnit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
