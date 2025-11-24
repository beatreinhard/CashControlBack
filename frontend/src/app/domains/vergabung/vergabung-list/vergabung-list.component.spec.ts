import {ComponentFixture, TestBed} from '@angular/core/testing';

import {VergabungListComponent} from './vergabung-list.component';

describe('VergabungListComponent', () => {
  let component: VergabungListComponent;
  let fixture: ComponentFixture<VergabungListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VergabungListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VergabungListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
