import {FullNamePipe} from './full-name.pipe';

describe('FullNamePipe', () => {
  const pipe = new FullNamePipe();

  it('setzt Name und Vorname zusammen', () => {
    expect(pipe.transform('Gerber', 'Max')).toBe('Gerber Max');
  });

  it('lässt fehlende Teile weg', () => {
    expect(pipe.transform('Gerber', undefined)).toBe('Gerber');
    expect(pipe.transform(undefined, null)).toBe('');
  });
});
