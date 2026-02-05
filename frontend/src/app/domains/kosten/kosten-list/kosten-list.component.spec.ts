import {ComponentFixture, TestBed} from '@angular/core/testing';

import {KostenListComponent} from './kosten-list.component';

describe('KostenListComponent', () => {
  let component: KostenListComponent;
  let fixture: ComponentFixture<KostenListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KostenListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(KostenListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
