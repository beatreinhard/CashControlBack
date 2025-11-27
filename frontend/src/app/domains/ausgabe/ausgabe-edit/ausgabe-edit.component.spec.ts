import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AusgabeEditComponent} from './ausgabe-edit.component';

describe('AusgabeEditComponent', () => {
  let component: AusgabeEditComponent;
  let fixture: ComponentFixture<AusgabeEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AusgabeEditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AusgabeEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
