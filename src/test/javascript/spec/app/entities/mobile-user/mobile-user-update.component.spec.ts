import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MctTestModule } from '../../../test.module';
import { MobileUserUpdateComponent } from 'app/entities/mobile-user/mobile-user-update.component';
import { MobileUserService } from 'app/entities/mobile-user/mobile-user.service';
import { MobileUser } from 'app/shared/model/mobile-user.model';

describe('Component Tests', () => {
  describe('MobileUser Management Update Component', () => {
    let comp: MobileUserUpdateComponent;
    let fixture: ComponentFixture<MobileUserUpdateComponent>;
    let service: MobileUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MctTestModule],
        declarations: [MobileUserUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(MobileUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MobileUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MobileUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MobileUser(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new MobileUser();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
