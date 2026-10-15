import {TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of} from 'rxjs';

import {SchuldComponent} from './schuld.component';
import {SchuldControllerApi} from '../../../generated';

describe('SchuldComponent', () => {
  it('zeigt Suche und einen Link zum Erfassen', async () => {
    const api = jasmine.createSpyObj<SchuldControllerApi>('SchuldControllerApi', ['getSchulden']);
    api.getSchulden.and.returnValue(of([]) as never);

    await TestBed.configureTestingModule({
      imports: [SchuldComponent],
      providers: [provideRouter([]), provideNoopAnimations(), { provide: SchuldControllerApi, useValue: api }]
    }).compileComponents();

    const fixture = TestBed.createComponent(SchuldComponent);
    fixture.detectChanges();
    const element = fixture.nativeElement as HTMLElement;

    expect(element.querySelector('app-schuld-search')).not.toBeNull();
    expect(element.querySelector('a[href="/schuld/create"]')?.textContent).toContain('Schuld erfassen');
  });
});
