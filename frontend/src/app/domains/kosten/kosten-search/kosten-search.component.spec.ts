import {ComponentFixture, TestBed} from '@angular/core/testing';

import {KostenSearchComponent} from './kosten-search.component';

describe('KostenSearchComponent', () => {
  let component: KostenSearchComponent;
  let fixture: ComponentFixture<KostenSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KostenSearchComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(KostenSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
