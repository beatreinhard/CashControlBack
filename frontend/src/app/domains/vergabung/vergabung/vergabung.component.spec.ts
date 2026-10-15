import {TestBed} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of} from 'rxjs';

import {VergabungComponent} from './vergabung.component';
import {VergabungControllerApi} from '../../../generated';

describe('VergabungComponent', () => {
  it('zeigt Titel und Suche', async () => {
    const api = jasmine.createSpyObj<VergabungControllerApi>('VergabungControllerApi', ['getVergabungen']);
    api.getVergabungen.and.returnValue(of([]) as never);

    await TestBed.configureTestingModule({
      imports: [VergabungComponent],
      providers: [provideNoopAnimations(), { provide: VergabungControllerApi, useValue: api }]
    }).compileComponents();

    const fixture = TestBed.createComponent(VergabungComponent);
    fixture.detectChanges();
    const element = fixture.nativeElement as HTMLElement;

    expect(element.querySelector('h1')?.textContent).toContain('Vergabungen');
    expect(element.querySelector('app-vergabung-search')).not.toBeNull();
  });
});
