import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SchuldListComponent} from './schuld-list.component';

describe('SchuldListComponent', () => {
  let component: SchuldListComponent;
  let fixture: ComponentFixture<SchuldListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SchuldListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SchuldListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
