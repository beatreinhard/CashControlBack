import {TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of} from 'rxjs';

import {KostenComponent} from './kosten.component';
import {KostenControllerApi} from '../../../generated';

describe('KostenComponent', () => {
  it('zeigt Suche und einen Link zum Erfassen', async () => {
    const api = jasmine.createSpyObj<KostenControllerApi>('KostenControllerApi', ['getAllKostenView']);
    api.getAllKostenView.and.returnValue(of([]) as never);

    await TestBed.configureTestingModule({
      imports: [KostenComponent],
      providers: [provideRouter([]), provideNoopAnimations(), { provide: KostenControllerApi, useValue: api }]
    }).compileComponents();

    const fixture = TestBed.createComponent(KostenComponent);
    fixture.detectChanges();
    const element = fixture.nativeElement as HTMLElement;

    expect(element.querySelector('app-kosten-search')).not.toBeNull();
    expect(element.querySelector('a[href="/kosten/create"]')?.textContent).toContain('Kosten erfassen');
  });
});
