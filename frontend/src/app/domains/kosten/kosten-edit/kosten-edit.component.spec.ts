import {ComponentFixture, TestBed} from '@angular/core/testing';

import {KostenEditComponent} from './kosten-edit.component';

describe('KostenEditComponent', () => {
  let component: KostenEditComponent;
  let fixture: ComponentFixture<KostenEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KostenEditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(KostenEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
