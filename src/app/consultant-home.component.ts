import {Component} from '@angular/core';
import {navigationMenuComponent} from './shared/navigation-menu.component';
import {requestDetailComponent} from './request-detail.component';
import {headerComponent} from './shared/header.component';
import {menuComponent} from './shared/menu.component';

@Component({
    selector:'home',
    template:`
    <header></header>            
	<div class="container-fluid mimin-wrapper">
 
      <menu></menu>
    
		<div id="content">
			<div class="panel box-shadow-none content-header">
				<div class="panel-body">
					<div class="col-md-12">
						<h3 class="animated fadeInLeft">Index page</h3>
						<p class="animated fadeInDown">
                         Index page <span class="fa-angle-right fa"></span>
                         Index page
                            
						</p>
					</div>
				</div>
			</div>

			<div class="col-md-12" style="padding: 20px;">
				<div class="col-md-12 padding-0">
					<div class="col-md-8 padding-0"></div>

				</div>

				<div class="col-md-12 card-wrap padding-0"></div>
                    <ul>
                        <li>
                            <request-detail></request-detail>
                        </li>
                    </ul>
			</div>
		</div>

	</div>

	<div id="mimin-mobile" class="reverse" th:include="fragments/menu :: mobileMenu"></div>
	<button id="mimin-mobile-menu-opener" class="animated rubberBand btn btn-circle btn-danger">
		<span class="fa fa-bars"></span>
	</button>`,
    directives:[navigationMenuComponent,requestDetailComponent, headerComponent,menuComponent]
})

export class consultantHomeComponent{}