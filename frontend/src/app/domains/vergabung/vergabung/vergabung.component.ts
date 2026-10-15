import {ChangeDetectionStrategy, Component} from '@angular/core';
import {VergabungSearchComponent} from '../vergabung-search/vergabung-search.component';

@Component({
  selector: 'app-vergabung',
  imports: [
    VergabungSearchComponent
  ],
  templateUrl: './vergabung.component.html',
  styleUrl: './vergabung.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VergabungComponent {

}
