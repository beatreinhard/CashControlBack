import {ComponentFixture, TestBed} from '@angular/core/testing';

import {VergabungSearchComponent} from './vergabung-search.component';

describe('VergabungSearchComponent', () => {
  let component: VergabungSearchComponent;
  let fixture: ComponentFixture<VergabungSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VergabungSearchComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VergabungSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
