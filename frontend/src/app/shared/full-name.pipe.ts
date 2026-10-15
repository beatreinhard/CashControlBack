import {Pipe, PipeTransform} from '@angular/core';

/** Setzt Name und Vorname zusammen; fehlende Teile werden weggelassen. */
@Pipe({ name: 'fullName' })
export class FullNamePipe implements PipeTransform {
  transform(name?: string | null, vorname?: string | null): string {
    return [name, vorname].filter(Boolean).join(' ');
  }
}
