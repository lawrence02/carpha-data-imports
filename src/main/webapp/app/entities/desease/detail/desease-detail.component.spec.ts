import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeseaseDetailComponent } from './desease-detail.component';

describe('Desease Management Detail Component', () => {
  let comp: DeseaseDetailComponent;
  let fixture: ComponentFixture<DeseaseDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeseaseDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ desease: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DeseaseDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DeseaseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load desease on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.desease).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
