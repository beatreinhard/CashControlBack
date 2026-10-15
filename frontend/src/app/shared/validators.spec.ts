import {FormControl} from '@angular/forms';
import {AHV_PATTERN, notInFutureValidator} from './validators';

describe('validators', () => {
  it('AHV_PATTERN akzeptiert nur 756.XXXX.XXXX.XX', () => {
    expect(AHV_PATTERN.test('756.1234.5678.90')).toBeTrue();
    expect(AHV_PATTERN.test('7561234567890')).toBeFalse();
    expect(AHV_PATTERN.test('123.1234.5678.90')).toBeFalse();
  });

  it('notInFutureValidator erlaubt heute und lehnt morgen ab', () => {
    const validate = notInFutureValidator();
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);

    expect(validate(new FormControl<Date | null>(new Date()))).toBeNull();
    expect(validate(new FormControl<Date | null>(null))).toBeNull();
    expect(validate(new FormControl<Date | null>(tomorrow))).toEqual({ futureDate: true });
  });
});
