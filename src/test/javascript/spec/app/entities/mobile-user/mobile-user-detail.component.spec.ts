import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MctTestModule } from '../../../test.module';
import { MobileUserDetailComponent } from 'app/entities/mobile-user/mobile-user-detail.component';
import { MobileUser } from 'app/shared/model/mobile-user.model';

describe('Component Tests', () => {
  describe('MobileUser Management Detail Component', () => {
    let comp: MobileUserDetailComponent;
    let fixture: ComponentFixture<MobileUserDetailComponent>;
    const route = ({ data: of({ mobileUser: new MobileUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MctTestModule],
        declarations: [MobileUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(MobileUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MobileUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mobileUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mobileUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
