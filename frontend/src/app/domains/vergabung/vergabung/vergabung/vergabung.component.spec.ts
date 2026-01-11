import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VergabungComponent } from './vergabung.component';

describe('VergabungComponent', () => {
  let component: VergabungComponent;
  let fixture: ComponentFixture<VergabungComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VergabungComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VergabungComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
