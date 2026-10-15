import {format, parseISO} from 'date-fns';

/** Wandelt ein Backend-Datum (ISO, yyyy-MM-dd) in ein Date um. */
export function parseBackendDate(value?: string | null): Date | null {
  return value ? parseISO(value) : null;
}

/** Wandelt ein Date in das Backend-Format yyyy-MM-dd um. */
export function toBackendDate(date: Date | null): string | undefined {
  return date ? format(date, 'yyyy-MM-dd') : undefined;
}
