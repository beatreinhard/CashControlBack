import {TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of} from 'rxjs';

import {AusgabeComponent} from './ausgabe.component';
import {AusgabeControllerApi} from '../../../generated';

describe('AusgabeComponent', () => {
  it('zeigt Suche und einen Link zum Erfassen', async () => {
    const api = jasmine.createSpyObj<AusgabeControllerApi>('AusgabeControllerApi', ['getAllAusgabeView']);
    api.getAllAusgabeView.and.returnValue(of([]) as never);

    await TestBed.configureTestingModule({
      imports: [AusgabeComponent],
      providers: [provideRouter([]), provideNoopAnimations(), { provide: AusgabeControllerApi, useValue: api }]
    }).compileComponents();

    const fixture = TestBed.createComponent(AusgabeComponent);
    fixture.detectChanges();
    const element = fixture.nativeElement as HTMLElement;

    expect(element.querySelector('app-ausgabe-search')).not.toBeNull();
    expect(element.querySelector('a[href="/ausgabe/create"]')?.textContent).toContain('Ausgabe erfassen');
  });
});
