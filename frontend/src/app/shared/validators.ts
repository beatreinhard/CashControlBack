import {AbstractControl, ValidationErrors, ValidatorFn} from '@angular/forms';
import {startOfDay} from 'date-fns';

/** AHV-Nummer im Format 756.XXXX.XXXX.XX */
export const AHV_PATTERN = /^756\.\d{4}\.\d{4}\.\d{2}$/;

/** Fehler 'futureDate', wenn das Datum nach heute liegt. */
export function notInFutureValidator(): ValidatorFn {
  return (control: AbstractControl<Date | null>): ValidationErrors | null => {
    const value = control.value;
    if (!value) return null;
    return startOfDay(value) > startOfDay(new Date()) ? { futureDate: true } : null;
  };
}
