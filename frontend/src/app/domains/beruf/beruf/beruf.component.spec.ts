import {TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of} from 'rxjs';

import {BerufComponent} from './beruf.component';
import {BerufControllerApi, PersonControllerApi} from '../../../generated';

describe('BerufComponent', () => {
  it('zeigt Suche und einen Link zum Erfassen', async () => {
    const api = jasmine.createSpyObj<BerufControllerApi>('BerufControllerApi', ['getAllBeruf']);
    api.getAllBeruf.and.returnValue(of([]) as never);
    const personApi = jasmine.createSpyObj<PersonControllerApi>('PersonControllerApi', ['getAllPerson']);
    personApi.getAllPerson.and.returnValue(of([]) as never);

    await TestBed.configureTestingModule({
      imports: [BerufComponent],
      providers: [provideRouter([]), provideNoopAnimations(), { provide: BerufControllerApi, useValue: api },
        { provide: PersonControllerApi, useValue: personApi }]
    }).compileComponents();

    const fixture = TestBed.createComponent(BerufComponent);
    fixture.detectChanges();
    const element = fixture.nativeElement as HTMLElement;

    expect(element.querySelector('app-beruf-search')).not.toBeNull();
    expect(element.querySelector('a[href="/beruf/create"]')?.textContent).toContain('Beruf erfassen');
  });
});
