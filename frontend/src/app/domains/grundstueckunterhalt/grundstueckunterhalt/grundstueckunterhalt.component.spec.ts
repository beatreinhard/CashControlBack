import {TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of} from 'rxjs';

import {GrundstueckunterhaltComponent} from './grundstueckunterhalt.component';
import {GrundstueckunterhaltControllerApi} from '../../../generated';

describe('GrundstueckunterhaltComponent', () => {
  it('zeigt Suche und einen Link zum Erfassen', async () => {
    const api = jasmine.createSpyObj<GrundstueckunterhaltControllerApi>(
      'GrundstueckunterhaltControllerApi', ['getAllGrundstueckunterhalt']
    );
    api.getAllGrundstueckunterhalt.and.returnValue(of([]) as never);

    await TestBed.configureTestingModule({
      imports: [GrundstueckunterhaltComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        { provide: GrundstueckunterhaltControllerApi, useValue: api }
      ]
    }).compileComponents();

    const fixture = TestBed.createComponent(GrundstueckunterhaltComponent);
    fixture.detectChanges();
    const element = fixture.nativeElement as HTMLElement;

    expect(element.querySelector('app-grundstueckunterhalt-search')).not.toBeNull();
    expect(element.querySelector('a[href="/grundstueckunterhalt/create"]')?.textContent)
      .toContain('Grundstückunterhalt erfassen');
  });
});
