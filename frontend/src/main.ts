import {bootstrapApplication} from '@angular/platform-browser';
import {appConfig} from './app/app.config';
import {AppComponent} from './app/app.component';
import {registerLocaleData} from '@angular/common';
import localeDeCh from '@angular/common/locales/de-CH';

// Registriere die Lokalisierungsdaten für de-CH
registerLocaleData(localeDeCh);

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
