import {Component,OnInit} from '@angular/core';
import {navigationMenuComponent} from './shared/navigation-menu.component';
import {requestDetailComponent} from './request-detail.component';
import {headerComponent} from './shared/header.component';
import {menuComponent} from './shared/menu.component';
import {requestService} from './request.service';

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
						<h3 class="animated fadeInLeft">Received Requests</h3>
						<p class="animated fadeInDown">
                         	Here you can find and answer all the received requests 
						 <span class="fa-angle-right fa"></span> 
						</p>
					</div>
				</div>
			</div>
			<div class="col-md-12 card-wrap padding-0"></div>
				<ul>
					<li *ngFor="let request of requests">
						<request-detail [name]=request.name
						[email]=request.email
						[id]=request.id
						[comments]=request.comments
						[language]=request.language
						[programme]=request.programme
						[purpose]=request.purpose></request-detail>
						<hr>
					</li>
					
				</ul>
			</div>
			
	</div>

	<!--div id="mimin-mobile" class="reverse" th:include="fragments/menu :: mobileMenu"></div>
	<button id="mimin-mobile-menu-opener" class="animated rubberBand btn btn-circle btn-danger">
		<span class="fa fa-bars"></span>
	</button-->`,
    directives:[navigationMenuComponent,requestDetailComponent, headerComponent,menuComponent],
	providers:[requestService]
})

export class consultantHomeComponent implements OnInit{
	requests;
	constructor(private _http:requestService){}
	ngOnInit(){
		this._http.getRequest().subscribe(res=>this.requests=res)
		console.log(this.requests);
	}

}