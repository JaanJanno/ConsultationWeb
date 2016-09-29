import { bootstrap } from '@angular/platform-browser-dynamic';
import { enableProdMode } from '@angular/core';
import { AppComponent, environment } from './app/';
import {App_Routes} from './app/app.routes';
import {ROUTER_DIRECTIVES} from '@angular/router';
import {HTTP_PROVIDERS} from '@angular/http';


if (environment.production) {
  enableProdMode();
}


bootstrap(AppComponent,[App_Routes,ROUTER_DIRECTIVES,HTTP_PROVIDERS]);
