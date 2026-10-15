import {ComponentFixture} from '@angular/core/testing';

/** Kleine DOM-Helfer für Komponenten-Tests. */
export function el(fixture: ComponentFixture<unknown>): HTMLElement {
  return fixture.nativeElement as HTMLElement;
}

export function rows(fixture: ComponentFixture<unknown>, selector = 'tr[mat-row]'): HTMLElement[] {
  return Array.from(el(fixture).querySelectorAll<HTMLElement>(selector));
}

export function setInputValue(fixture: ComponentFixture<unknown>, controlName: string, value: string): void {
  const input = el(fixture).querySelector<HTMLInputElement | HTMLTextAreaElement>(`[formcontrolname="${controlName}"]`)!;
  input.value = value;
  input.dispatchEvent(new Event('input'));
  input.dispatchEvent(new Event('blur'));
  fixture.detectChanges();
}

export function buttonByText(fixture: ComponentFixture<unknown>, text: string): HTMLButtonElement | undefined {
  return Array.from(el(fixture).querySelectorAll<HTMLButtonElement>('button'))
    .find(b => b.textContent?.trim() === text);
}

export function submitButton(fixture: ComponentFixture<unknown>): HTMLButtonElement {
  return el(fixture).querySelector<HTMLButtonElement>('button[type="submit"]')!;
}

export function submit(fixture: ComponentFixture<unknown>): void {
  submitButton(fixture).click();
  fixture.detectChanges();
}

export function errorTexts(fixture: ComponentFixture<unknown>): string[] {
  return Array.from(el(fixture).querySelectorAll('mat-error')).map(e => e.textContent!.trim());
}

/** Rendert, wartet auf asynchrone Resource-Loads und rendert erneut. */
export async function stabilize(fixture: ComponentFixture<unknown>): Promise<void> {
  fixture.detectChanges();
  await fixture.whenStable();
  fixture.detectChanges();
}
