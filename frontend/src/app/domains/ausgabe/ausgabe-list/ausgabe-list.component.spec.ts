import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AusgabeListComponent} from './ausgabe-list.component';

describe('AusgabeListComponent', () => {
  let component: AusgabeListComponent;
  let fixture: ComponentFixture<AusgabeListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AusgabeListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AusgabeListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
