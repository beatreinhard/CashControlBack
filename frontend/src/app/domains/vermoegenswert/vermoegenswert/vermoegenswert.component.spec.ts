import {TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of} from 'rxjs';

import {VermoegenswertComponent} from './vermoegenswert.component';
import {VermoegenswertControllerApi} from '../../../generated';

describe('VermoegenswertComponent', () => {
  it('zeigt Suche und einen Link zum Erfassen', async () => {
    const api = jasmine.createSpyObj<VermoegenswertControllerApi>('VermoegenswertControllerApi', ['getAllVermoegenswert']);
    api.getAllVermoegenswert.and.returnValue(of([]) as never);

    await TestBed.configureTestingModule({
      imports: [VermoegenswertComponent],
      providers: [provideRouter([]), provideNoopAnimations(), { provide: VermoegenswertControllerApi, useValue: api }]
    }).compileComponents();

    const fixture = TestBed.createComponent(VermoegenswertComponent);
    fixture.detectChanges();
    const element = fixture.nativeElement as HTMLElement;

    expect(element.querySelector('app-vermoegenswert-search')).not.toBeNull();
    expect(element.querySelector('a[href="/vermoegenswert/create"]')?.textContent).toContain('Vermögenswert erfassen');
  });
});
