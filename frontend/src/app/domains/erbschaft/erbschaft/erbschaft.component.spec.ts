import {TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {of} from 'rxjs';

import {ErbschaftComponent} from './erbschaft.component';
import {ErbschaftControllerApi} from '../../../generated';

describe('ErbschaftComponent', () => {
  it('zeigt Suche und einen Link zum Erfassen', async () => {
    const api = jasmine.createSpyObj<ErbschaftControllerApi>('ErbschaftControllerApi', ['getAllErbschaft']);
    api.getAllErbschaft.and.returnValue(of([]) as never);

    await TestBed.configureTestingModule({
      imports: [ErbschaftComponent],
      providers: [provideRouter([]), provideNoopAnimations(), { provide: ErbschaftControllerApi, useValue: api }]
    }).compileComponents();

    const fixture = TestBed.createComponent(ErbschaftComponent);
    fixture.detectChanges();
    const element = fixture.nativeElement as HTMLElement;

    expect(element.querySelector('app-erbschaft-search')).not.toBeNull();
    expect(element.querySelector('a[href="/erbschaft/create"]')?.textContent).toContain('Erbschaft erfassen');
  });
});
