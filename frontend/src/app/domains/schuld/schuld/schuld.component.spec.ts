import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SchuldComponent} from './schuld.component';

describe('SchuldComponent', () => {
  let component: SchuldComponent;
  let fixture: ComponentFixture<SchuldComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SchuldComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SchuldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
