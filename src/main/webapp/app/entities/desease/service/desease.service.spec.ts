import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDesease, Desease } from '../desease.model';

import { DeseaseService } from './desease.service';

describe('Desease Service', () => {
  let service: DeseaseService;
  let httpMock: HttpTestingController;
  let elemDefault: IDesease;
  let expectedResult: IDesease | IDesease[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeseaseService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      country: 'AAAAAAA',
      deseaseId: 'AAAAAAA',
      caseInfo: 'AAAAAAA',
      year: 'AAAAAAA',
      week: 'AAAAAAA',
      weekEnding: 'AAAAAAA',
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

    it('should create a Desease', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Desease()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Desease', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          country: 'BBBBBB',
          deseaseId: 'BBBBBB',
          caseInfo: 'BBBBBB',
          year: 'BBBBBB',
          week: 'BBBBBB',
          weekEnding: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Desease', () => {
      const patchObject = Object.assign(
        {
          country: 'BBBBBB',
          year: 'BBBBBB',
          weekEnding: 'BBBBBB',
        },
        new Desease()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Desease', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          country: 'BBBBBB',
          deseaseId: 'BBBBBB',
          caseInfo: 'BBBBBB',
          year: 'BBBBBB',
          week: 'BBBBBB',
          weekEnding: 'BBBBBB',
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

    it('should delete a Desease', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDeseaseToCollectionIfMissing', () => {
      it('should add a Desease to an empty array', () => {
        const desease: IDesease = { id: 123 };
        expectedResult = service.addDeseaseToCollectionIfMissing([], desease);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(desease);
      });

      it('should not add a Desease to an array that contains it', () => {
        const desease: IDesease = { id: 123 };
        const deseaseCollection: IDesease[] = [
          {
            ...desease,
          },
          { id: 456 },
        ];
        expectedResult = service.addDeseaseToCollectionIfMissing(deseaseCollection, desease);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Desease to an array that doesn't contain it", () => {
        const desease: IDesease = { id: 123 };
        const deseaseCollection: IDesease[] = [{ id: 456 }];
        expectedResult = service.addDeseaseToCollectionIfMissing(deseaseCollection, desease);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(desease);
      });

      it('should add only unique Desease to an array', () => {
        const deseaseArray: IDesease[] = [{ id: 123 }, { id: 456 }, { id: 35699 }];
        const deseaseCollection: IDesease[] = [{ id: 123 }];
        expectedResult = service.addDeseaseToCollectionIfMissing(deseaseCollection, ...deseaseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const desease: IDesease = { id: 123 };
        const desease2: IDesease = { id: 456 };
        expectedResult = service.addDeseaseToCollectionIfMissing([], desease, desease2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(desease);
        expect(expectedResult).toContain(desease2);
      });

      it('should accept null and undefined values', () => {
        const desease: IDesease = { id: 123 };
        expectedResult = service.addDeseaseToCollectionIfMissing([], null, desease, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(desease);
      });

      it('should return initial array if no Desease is added', () => {
        const deseaseCollection: IDesease[] = [{ id: 123 }];
        expectedResult = service.addDeseaseToCollectionIfMissing(deseaseCollection, undefined, null);
        expect(expectedResult).toEqual(deseaseCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
