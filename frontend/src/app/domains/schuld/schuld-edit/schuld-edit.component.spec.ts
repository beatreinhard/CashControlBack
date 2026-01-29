import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SchuldEditComponent} from './schuld-edit.component';

describe('SchuldEditComponent', () => {
  let component: SchuldEditComponent;
  let fixture: ComponentFixture<SchuldEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SchuldEditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SchuldEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
