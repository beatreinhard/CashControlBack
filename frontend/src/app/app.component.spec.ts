import {TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';
import {provideNoopAnimations} from '@angular/platform-browser/animations';
import {Observable, of, throwError} from 'rxjs';
import {AppComponent} from './app.component';
import {VersionControllerApi, VersionDto} from './generated';

describe('AppComponent', () => {
  const setup = async (version$: Observable<VersionDto>) => {
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        {provide: VersionControllerApi, useValue: {getVersion: () => version$}}
      ]
    }).compileComponents();

    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    return fixture;
  };

  const toolbarOf = (element: unknown) => (element as HTMLElement).querySelector('mat-toolbar');

  it('should create the app', async () => {
    const fixture = await setup(of({version: '1.2.3'}));
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should render the toolbar title', async () => {
    const fixture = await setup(of({version: '1.2.3'}));
    expect(toolbarOf(fixture.nativeElement)?.textContent).toContain('Cash Control');
  });

  it('should render the version in the toolbar', async () => {
    const fixture = await setup(of({version: '1.2.3'}));
    expect(toolbarOf(fixture.nativeElement)?.textContent).toContain('Version: 1.2.3');
  });

  it('should render no version if the request fails', async () => {
    const fixture = await setup(throwError(() => new Error('nicht erreichbar')));
    expect(toolbarOf(fixture.nativeElement)?.textContent).toContain('Cash Control');
    expect(toolbarOf(fixture.nativeElement)?.textContent).not.toContain('Version');
  });
});
