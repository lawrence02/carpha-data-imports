import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DeseaseService } from '../service/desease.service';
import { IDesease, Desease } from '../desease.model';

import { DeseaseUpdateComponent } from './desease-update.component';

describe('Desease Management Update Component', () => {
  let comp: DeseaseUpdateComponent;
  let fixture: ComponentFixture<DeseaseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deseaseService: DeseaseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DeseaseUpdateComponent],
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
      .overrideTemplate(DeseaseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeseaseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deseaseService = TestBed.inject(DeseaseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const desease: IDesease = { id: 456 };

      activatedRoute.data = of({ desease });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(desease));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Desease>>();
      const desease = { id: 123 };
      jest.spyOn(deseaseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ desease });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: desease }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(deseaseService.update).toHaveBeenCalledWith(desease);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Desease>>();
      const desease = new Desease();
      jest.spyOn(deseaseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ desease });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: desease }));
      saveSubject.complete();

      // THEN
      expect(deseaseService.create).toHaveBeenCalledWith(desease);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Desease>>();
      const desease = { id: 123 };
      jest.spyOn(deseaseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ desease });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deseaseService.update).toHaveBeenCalledWith(desease);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
