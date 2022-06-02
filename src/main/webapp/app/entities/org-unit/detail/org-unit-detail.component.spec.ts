import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrgUnitDetailComponent } from './org-unit-detail.component';

describe('OrgUnit Management Detail Component', () => {
  let comp: OrgUnitDetailComponent;
  let fixture: ComponentFixture<OrgUnitDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrgUnitDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ orgUnit: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OrgUnitDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OrgUnitDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load orgUnit on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.orgUnit).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
