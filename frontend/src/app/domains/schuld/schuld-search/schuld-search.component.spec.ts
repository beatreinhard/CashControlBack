import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SchuldSearchComponent} from './schuld-search.component';

describe('SchuldSearchComponent', () => {
  let component: SchuldSearchComponent;
  let fixture: ComponentFixture<SchuldSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SchuldSearchComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SchuldSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
