import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AusgabeSearchComponent} from './ausgabe-search.component';

describe('AusgabeSearchComponent', () => {
  let component: AusgabeSearchComponent;
  let fixture: ComponentFixture<AusgabeSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AusgabeSearchComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AusgabeSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
