import {ChangeDetectionStrategy, Component} from '@angular/core';
import {VergabungSearchComponent} from '../vergabung-search/vergabung-search.component';
import {PageHeaderComponent} from '../../../shared/page-header/page-header.component';

@Component({
  selector: 'app-vergabung',
  imports: [
    VergabungSearchComponent,
    PageHeaderComponent
  ],
  templateUrl: './vergabung.component.html',
  styleUrl: './vergabung.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: { class: 'cc-page' }
})
export class VergabungComponent {

}
