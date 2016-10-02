import {Component,Input} from '@angular/core';

@Component({
    selector:"request-detail",
    template:`
        <div class="container">
                <div class="zippy__title" (click)="toggle()">
                    <div class="row">
                        <div class="col-sm-1">
                            <label><strong>Name:</strong></label>
                        </div>
                        <div class="col-sm-5">    
                            <label>{{name}}</label>
                        </div>
                        <div class="col-sm-1">
                            <label><strong>Email:</strong></label>
                        </div>
                        <div class="col-sm-4">    
                            <label>{{email}}</label>
                        </div>
                        <div class="col-sm-1">    
                            <i class="fa fa-expand" aria-hidden="true"></i>
                        </div>
                        
                    </div>
                </div>
                
                <div class="zippy__content" [hidden]="!visible">
                    <div class="row">
                            <div class="col-sm-1">
                                <label><strong>Language</strong></label>
                            </div>
                            <div class="col-sm-3">
                                <label>{{language}}</label>
                            </div>
                     </div>       
                    <div class="row">
                            <div class="col-sm-1">    
                                <label><strong>programme:</strong></label>
                            </div>
                            <div class="col-sm-10">    
                                <label>{{programme}}</label>
                            </div>
                    </div>
                    <div class="row">
                            <div class="col-sm-1">    
                                <label><strong>purpose:</strong></label>
                            </div>
                            <div class="col-sm-4">    
                                <label>{{purpose}}</label>
                            </div>
                    </div>
                    <div class="row">
                            <div class="col-sm-1">
                                <label for="name"><strong>Comment:</strong></label>
                            </div>
                            <div class="col-sm-10">    
                                <label for="name">{{comments}}</label>
                            </div>
                    </div>
                    <div class="row">
                            <div class="col-sm-2">
                                <label><strong>Attached file:</strong></label>
                            </div>
                            <div class="col-sm-2">    
                                <label>file</label>
                            </div>
                    </div>
                    <form>
                        <button type="submit" class="btn btn-primary">Accept</button>
                    </form>
                                        <div class="row">
                            <div class="col-sm-2">    
                                <label  style="visibility: hidden;">{{id}}</label>
                            </div>
                    </div>
                </div>
        </div>
        <hr>`
})


export class requestDetailComponent{
    @Input() id;
    @Input() name;
    @Input() email;
    @Input() purpose;
    @Input() programme;
    @Input() language;
    @Input() comments;

    visible = false;
    toggle() {
    this.visible = !this.visible;
  }
}