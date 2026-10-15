import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of} from 'rxjs';

import {PersonComponent} from './person.component';
import {PersonControllerApi} from '../../../generated';

describe('PersonComponent', () => {
  let fixture: ComponentFixture<PersonComponent>;

  beforeEach(async () => {
    const api = jasmine.createSpyObj<PersonControllerApi>('PersonControllerApi', ['getAllPerson']);
    api.getAllPerson.and.returnValue(of([]) as never);

    await TestBed.configureTestingModule({
      imports: [PersonComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        { provide: PersonControllerApi, useValue: api }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PersonComponent);
    fixture.detectChanges();
  });

  it('zeigt Suche und einen Link zum Erfassen', () => {
    const element = fixture.nativeElement as HTMLElement;
    expect(element.querySelector('app-person-search')).not.toBeNull();
    expect(element.querySelector('a[href="/person/create"]')?.textContent).toContain('Person erfassen');
  });
});
