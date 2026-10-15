import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideNoopAnimations} from '@angular/platform-browser/animations';

import {YearFilterComponent} from './year-filter.component';
import {errorTexts, setInputValue, submit} from '../../testing/dom-helpers';

describe('YearFilterComponent', () => {
  let fixture: ComponentFixture<YearFilterComponent>;
  let emitted: (number | undefined)[];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [YearFilterComponent],
      providers: [provideNoopAnimations()]
    }).compileComponents();

    fixture = TestBed.createComponent(YearFilterComponent);
    emitted = [];
    fixture.componentInstance.jahrChange.subscribe(jahr => emitted.push(jahr));
    fixture.detectChanges();
  });

  it('meldet das Jahr bzw. undefined für "alle"', () => {
    setInputValue(fixture, 'jahr', '2025');
    submit(fixture);
    setInputValue(fixture, 'jahr', '');
    submit(fixture);

    expect(emitted).toEqual([2025, undefined]);
  });

  it('meldet nichts bei ungültigem Jahr', () => {
    setInputValue(fixture, 'jahr', '1999');
    submit(fixture);

    expect(emitted).toEqual([]);
    expect(errorTexts(fixture)).toContain('Jahr muss 2000 oder grösser sein');
  });
});
