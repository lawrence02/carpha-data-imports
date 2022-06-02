import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrgUnit, OrgUnit } from '../org-unit.model';

import { OrgUnitService } from './org-unit.service';

describe('OrgUnit Service', () => {
  let service: OrgUnitService;
  let httpMock: HttpTestingController;
  let elemDefault: IOrgUnit;
  let expectedResult: IOrgUnit | IOrgUnit[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrgUnitService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      orgUnitName: 'AAAAAAA',
      dhisOrgUnitName: 'AAAAAAA',
      dhisOrgUnitCode: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a OrgUnit', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new OrgUnit()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrgUnit', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          orgUnitName: 'BBBBBB',
          dhisOrgUnitName: 'BBBBBB',
          dhisOrgUnitCode: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrgUnit', () => {
      const patchObject = Object.assign(
        {
          orgUnitName: 'BBBBBB',
        },
        new OrgUnit()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrgUnit', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          orgUnitName: 'BBBBBB',
          dhisOrgUnitName: 'BBBBBB',
          dhisOrgUnitCode: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a OrgUnit', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOrgUnitToCollectionIfMissing', () => {
      it('should add a OrgUnit to an empty array', () => {
        const orgUnit: IOrgUnit = { id: 123 };
        expectedResult = service.addOrgUnitToCollectionIfMissing([], orgUnit);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orgUnit);
      });

      it('should not add a OrgUnit to an array that contains it', () => {
        const orgUnit: IOrgUnit = { id: 123 };
        const orgUnitCollection: IOrgUnit[] = [
          {
            ...orgUnit,
          },
          { id: 456 },
        ];
        expectedResult = service.addOrgUnitToCollectionIfMissing(orgUnitCollection, orgUnit);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrgUnit to an array that doesn't contain it", () => {
        const orgUnit: IOrgUnit = { id: 123 };
        const orgUnitCollection: IOrgUnit[] = [{ id: 456 }];
        expectedResult = service.addOrgUnitToCollectionIfMissing(orgUnitCollection, orgUnit);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orgUnit);
      });

      it('should add only unique OrgUnit to an array', () => {
        const orgUnitArray: IOrgUnit[] = [{ id: 123 }, { id: 456 }, { id: 38327 }];
        const orgUnitCollection: IOrgUnit[] = [{ id: 123 }];
        expectedResult = service.addOrgUnitToCollectionIfMissing(orgUnitCollection, ...orgUnitArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orgUnit: IOrgUnit = { id: 123 };
        const orgUnit2: IOrgUnit = { id: 456 };
        expectedResult = service.addOrgUnitToCollectionIfMissing([], orgUnit, orgUnit2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orgUnit);
        expect(expectedResult).toContain(orgUnit2);
      });

      it('should accept null and undefined values', () => {
        const orgUnit: IOrgUnit = { id: 123 };
        expectedResult = service.addOrgUnitToCollectionIfMissing([], null, orgUnit, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orgUnit);
      });

      it('should return initial array if no OrgUnit is added', () => {
        const orgUnitCollection: IOrgUnit[] = [{ id: 123 }];
        expectedResult = service.addOrgUnitToCollectionIfMissing(orgUnitCollection, undefined, null);
        expect(expectedResult).toEqual(orgUnitCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
