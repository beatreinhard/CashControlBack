import {ChangeDetectionStrategy, Component, computed, inject, signal} from '@angular/core';
import {DatePipe, DecimalPipe} from '@angular/common';
import {rxResource} from '@angular/core/rxjs-interop';
import {RouterLink} from '@angular/router';
import {MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {
  AusgabeControllerApi,
  AusgabeKategorieDto,
  BerufControllerApi,
  ErbschaftControllerApi,
  GrundstueckunterhaltControllerApi,
  KostenControllerApi,
  PersonControllerApi,
  SchuldControllerApi,
  VergabungControllerApi,
  VermoegenswertControllerApi
} from '../../../generated';
import {KATEGORIE_META, KategorieMeta} from '../../ausgabe/ausgabe-kategorie-meta';
import {PageHeaderComponent} from '../../../shared/page-header/page-header.component';
import {MIN_YEAR} from '../../../shared/year-filter/year-filter.component';

const MONATE = ['Jan', 'Feb', 'Mär', 'Apr', 'Mai', 'Jun', 'Jul', 'Aug', 'Sep', 'Okt', 'Nov', 'Dez'];
const TOP_KATEGORIEN = 6;

export interface MonatsBalken {
  label: string;
  total: number;
  /** Höhe in % des grössten Monats. */
  height: number;
}

export interface KategorieBalken {
  meta: KategorieMeta;
  total: number;
  /** Breite in % der grössten Kategorie. */
  width: number;
  /** Anteil am Jahrestotal in %. */
  share: number;
}

export interface BereichLink {
  label: string;
  icon: string;
  link: string;
  value: string;
  hint: string;
}

const sum = <T>(items: T[], f: (item: T) => number | undefined): number =>
  items.reduce((s, item) => s + (f(item) ?? 0), 0);

/** Übersicht mit Kennzahlen pro Steuerjahr. Alle Werte werden im Client aus den bestehenden APIs berechnet. */
@Component({
  selector: 'app-dashboard',
  imports: [DatePipe, DecimalPipe, MatIcon, MatIconButton, PageHeaderComponent, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: { class: 'cc-page' }
})
export class DashboardComponent {
  private readonly ausgabeApi = inject(AusgabeControllerApi);
  private readonly kostenApi = inject(KostenControllerApi);
  private readonly vergabungApi = inject(VergabungControllerApi);
  private readonly schuldApi = inject(SchuldControllerApi);
  private readonly vermoegenswertApi = inject(VermoegenswertControllerApi);
  private readonly erbschaftApi = inject(ErbschaftControllerApi);
  private readonly grundstueckApi = inject(GrundstueckunterhaltControllerApi);
  private readonly berufApi = inject(BerufControllerApi);
  private readonly personApi = inject(PersonControllerApi);

  protected readonly maxYear = new Date().getFullYear();
  protected readonly minYear = MIN_YEAR;
  protected readonly jahr = signal(this.maxYear);

  // --- Daten ---
  private readonly ausgaben = rxResource({ loader: () => this.ausgabeApi.getAllAusgabeView() });
  private readonly kosten = rxResource({ request: () => this.jahr(), loader: ({ request }) => this.kostenApi.getAllKostenView(request) });
  private readonly vergabungen = rxResource({ request: () => this.jahr(), loader: ({ request }) => this.vergabungApi.getVergabungen(request) });
  private readonly schulden = rxResource({ request: () => this.jahr(), loader: ({ request }) => this.schuldApi.getSchulden(request) });
  private readonly vermoegenswerte = rxResource({ loader: () => this.vermoegenswertApi.getAllVermoegenswert() });
  private readonly erbschaften = rxResource({ loader: () => this.erbschaftApi.getAllErbschaft() });
  private readonly grundstueck = rxResource({ loader: () => this.grundstueckApi.getAllGrundstueckunterhalt() });
  private readonly berufe = rxResource({ loader: () => this.berufApi.getAllBeruf() });
  private readonly personen = rxResource({ loader: () => this.personApi.getAllPerson() });

  protected readonly ausgabenLoading = this.ausgaben.isLoading;
  protected readonly ausgabenError = computed(() => !!this.ausgaben.error());

  private readonly ausgabenJahr = computed(() => this.ofYear(this.ausgaben.value(), this.jahr()));
  private readonly ausgabenVorjahr = computed(() => this.ofYear(this.ausgaben.value(), this.jahr() - 1));

  // --- Kennzahlen ---
  protected readonly ausgabenTotal = computed(() => sum(this.ausgabenJahr(), a => a.betrag));
  protected readonly ausgabenCount = computed(() => this.ausgabenJahr().length);
  /** Veränderung gegenüber Vorjahr in %; null, wenn das Vorjahr keine Ausgaben hat. */
  protected readonly ausgabenTrend = computed(() => {
    const vorjahr = sum(this.ausgabenVorjahr(), a => a.betrag);
    return vorjahr > 0 ? ((this.ausgabenTotal() - vorjahr) / vorjahr) * 100 : null;
  });

  protected readonly kostenTotal = computed(() => this.kosten.error() ? null : sum(this.kosten.value() ?? [], k => k.betrag));
  protected readonly kostenCount = computed(() => this.kosten.value()?.length ?? 0);
  protected readonly vergabungenTotal = computed(() => this.vergabungen.error() ? null : sum(this.vergabungen.value() ?? [], v => v.betrag));
  protected readonly vergabungenCount = computed(() => this.vergabungen.value()?.length ?? 0);
  protected readonly schuldenTotal = computed(() => this.schulden.error() ? null : sum(this.schulden.value() ?? [], s => s.betrag));
  protected readonly schuldzinsen = computed(() => sum(this.schulden.value() ?? [], s => s.zinsen));

  // --- Diagramme ---
  protected readonly monate = computed<MonatsBalken[]>(() => {
    const totals = new Array(12).fill(0);
    for (const a of this.ausgabenJahr()) {
      const monat = Number(a.datum.slice(5, 7)) - 1;
      if (monat >= 0 && monat < 12) totals[monat] += a.betrag ?? 0;
    }
    const max = Math.max(...totals);
    return totals.map((total, i) => ({ label: MONATE[i], total, height: max > 0 ? (total / max) * 100 : 0 }));
  });
  protected readonly monatMax = computed(() => Math.max(0, ...this.monate().map(m => m.total)));

  protected readonly kategorien = computed<KategorieBalken[]>(() => {
    const perKategorie = new Map<AusgabeKategorieDto, number>();
    for (const a of this.ausgabenJahr()) perKategorie.set(a.kategorie, (perKategorie.get(a.kategorie) ?? 0) + (a.betrag ?? 0));
    const sorted = [...perKategorie.entries()].sort((x, y) => y[1] - x[1]);
    const top = sorted.slice(0, TOP_KATEGORIEN);
    const rest = sorted.slice(TOP_KATEGORIEN).reduce((s, [, t]) => s + t, 0);
    const rows = top.map(([k, total]) => ({ meta: KATEGORIE_META[k], total }));
    if (rest > 0) rows.push({ meta: { label: 'Übrige', icon: 'more_horiz', color: '#9aa1ad' }, total: rest });
    const max = Math.max(0, ...rows.map(r => r.total));
    const all = this.ausgabenTotal();
    return rows.map(r => ({ ...r, width: max > 0 ? (r.total / max) * 100 : 0, share: all > 0 ? (r.total / all) * 100 : 0 }));
  });

  protected readonly letzteBuchungen = computed(() =>
    [...this.ausgabenJahr()].sort((a, b) => b.datum.localeCompare(a.datum)).slice(0, 5)
  );

  // --- Weitere Bereiche ---
  protected readonly bereiche = computed<BereichLink[]>(() => {
    const jahr = this.jahr();
    const chf = (n: number) => 'CHF ' + n.toLocaleString('de-CH', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    const vw = (this.vermoegenswerte.value() ?? []).filter(v => v.jahr === jahr);
    const eb = (this.erbschaften.value() ?? []).filter(e => e.jahr === jahr);
    const gu = (this.grundstueck.value() ?? []).filter(g => g.jahr === jahr);
    const bf = (this.berufe.value() ?? []).filter(b => b.jahr === jahr);
    const anzahl = (n: number, einzahl: string, mehrzahl: string) => `${n} ${n === 1 ? einzahl : mehrzahl}`;
    return [
      { label: 'Vermögenswerte', icon: 'account_balance_wallet', link: '/vermoegenswert', value: chf(sum(vw, v => v.anschaffungspreis)), hint: anzahl(vw.length, 'Position', 'Positionen') },
      { label: 'Grundstückunterhalt', icon: 'home_repair_service', link: '/grundstueckunterhalt', value: chf(sum(gu, g => g.anteilUnterhalt)), hint: anzahl(gu.length, 'Rechnung', 'Rechnungen') },
      { label: 'Erbschaften', icon: 'redeem', link: '/erbschaft', value: chf(sum(eb, e => e.betrag)), hint: anzahl(eb.length, 'Eintrag', 'Einträge') },
      { label: 'Berufe', icon: 'work', link: '/beruf', value: anzahl(bf.length, 'Anstellung', 'Anstellungen'), hint: `Steuerjahr ${jahr}` },
      { label: 'Personen', icon: 'group', link: '/person', value: anzahl(this.personen.value()?.length ?? 0, 'Person', 'Personen'), hint: 'im Haushalt' }
    ];
  });

  protected readonly meta = (k: AusgabeKategorieDto) => KATEGORIE_META[k];

  protected vorherigesJahr(): void {
    if (this.jahr() > this.minYear) this.jahr.update(j => j - 1);
  }

  protected naechstesJahr(): void {
    if (this.jahr() < this.maxYear) this.jahr.update(j => j + 1);
  }

  private ofYear<T extends { datum: string }>(items: T[] | undefined, jahr: number): T[] {
    const prefix = String(jahr);
    return (items ?? []).filter(i => i.datum?.startsWith(prefix));
  }
}
