import {Component} from '@angular/core';
import {navigationMenuComponent} from './shared/navigation-menu.component';

@Component({
    selector:'home',
    template:`<nav-menu></nav-menu>`,
    directives:[navigationMenuComponent]
})

export class homeComponent{}