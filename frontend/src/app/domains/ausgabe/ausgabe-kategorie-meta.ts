import {AusgabeKategorieDto} from '../../generated';

/** Anzeige-Metadaten pro Ausgabe-Kategorie (Design-Vorschlag A). */
export interface KategorieMeta {
  label: string;
  icon: string;
  color: string;
}

export const KATEGORIE_META: Record<AusgabeKategorieDto, KategorieMeta> = {
  [AusgabeKategorieDto.VerkehrAutoTransport]: { label: 'Verkehr & Transport', icon: 'directions_car', color: '#2563eb' },
  [AusgabeKategorieDto.Haushalt]: { label: 'Haushalt', icon: 'home', color: '#0d9488' },
  [AusgabeKategorieDto.Ferien]: { label: 'Ferien', icon: 'beach_access', color: '#f59e0b' },
  [AusgabeKategorieDto.KommunikationMedien]: { label: 'Kommunikation & Medien', icon: 'wifi', color: '#7c3aed' },
  [AusgabeKategorieDto.UnfallKrankheit]: { label: 'Unfall & Krankheit', icon: 'medical_services', color: '#dc2626' },
  [AusgabeKategorieDto.PersoenlicheAusgaben]: { label: 'Persönliche Ausgaben', icon: 'shopping_bag', color: '#db2777' },
  [AusgabeKategorieDto.SparenVorsorge]: { label: 'Sparen & Vorsorge', icon: 'savings', color: '#16a34a' },
  [AusgabeKategorieDto.Steuern]: { label: 'Steuern', icon: 'account_balance', color: '#475569' },
  [AusgabeKategorieDto.Spenden]: { label: 'Spenden', icon: 'volunteer_activism', color: '#e11d48' },
  [AusgabeKategorieDto.Krankenkasse]: { label: 'Krankenkasse', icon: 'health_and_safety', color: '#0891b2' },
  [AusgabeKategorieDto.Beruf]: { label: 'Beruf', icon: 'work', color: '#4f46e5' },
  [AusgabeKategorieDto.Bankgebuehren]: { label: 'Bankgebühren', icon: 'credit_card', color: '#64748b' },
  [AusgabeKategorieDto.LiegenschaftBetrieb]: { label: 'Liegenschaft', icon: 'apartment', color: '#a16207' },
  [AusgabeKategorieDto.Diverses]: { label: 'Diverses', icon: 'category', color: '#6b7280' }
};

export function kategorieMeta(kategorie: AusgabeKategorieDto | null | undefined): KategorieMeta | null {
  return kategorie ? KATEGORIE_META[kategorie] ?? null : null;
}
