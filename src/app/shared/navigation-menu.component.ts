import {Component} from '@angular/core';
import {ROUTER_DIRECTIVES} from '@angular/router';

@Component({
    selector:'nav-menu',
    template:`<nav class="navbar navbar-default">
  <div class="container-fluid">
        <div class="container">
        <div class="navbar-header">
          <a class="navbar-brand" [routerLink]="['/home']">CAWC</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
          <ul class="nav navbar-nav">
            <li class="active"><a [routerLink]="['/home']">Home <span class="sr-only">(current)</span></a></li>
            <li><a [routerLink]="['/request']">Incoming Requests</a></li>
            <li><a href="#">to-do</a></li>
            <li><a href="#">appointment</a></li>
            <li><a href="#">materials</a></li>
            <li><a href="#">dashboard</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">My account <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="#">Profile</a></li>
                <li role="separator" class="divider"></li>
                <li><a href="#">Setting</a></li>
                <li><a href="#">Log out</a></li>
              </ul>
            </li>
          </ul>
        </div><!-- /.navbar-collapse -->
    </div>    
  </div><!-- /.container-fluid -->
</nav>`,
directives:[ROUTER_DIRECTIVES]
})

export class navigationMenuComponent{}