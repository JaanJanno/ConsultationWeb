import {Component} from '@angular/core';

@Component({
    selector:"request-detail",
    template:`
        <div class="container">
                <div class="zippy__title" (click)="toggle()">
                    <div class="row">
                        <div class="col-sm-1">
                            <label>Name:</label>
                        </div>
                        <div class="col-sm-5">    
                            <label>Salman</label>
                        </div>
                        <div class="col-sm-1">
                            <label>Email:</label>
                        </div>
                        <div class="col-sm-5">    
                            <label>salmanlashkarara@gmail.com</label>
                        </div>
                    </div>
                </div>
                
                <div class="zippy__content" [hidden]="!visible">
                    <div class="row">
                            <div class="col-sm-1">
                                <label for="name">Department:</label>
                            </div>
                            <div class="col-sm-5">
                                <label for="name">Mathematic and Computer Science</label>
                            </div>
                            <div class="col-sm-1">    
                                <label for="name">Degree:</label>
                            </div>
                            <div class="col-sm-1">    
                                <label for="name">Bachelor</label>
                            </div>
                            <div class="col-sm-1">    
                                <label for="name">Type:</label>
                            </div>
                            <div class="col-sm-2">    
                                <label for="name">Artice</label>
                            </div>
                    </div>
                    <div class="row">
                            <div class="col-sm-1">
                                <label for="name">Comment:</label>
                            </div>
                            <div class="col-sm-10">    
                                <label for="name">Comment:</label>
                            </div>
                    </div>
                    <div class="row">
                            <div class="col-sm-2">
                                <label>Attached file:</label>
                            </div>
                            <div class="col-sm-2">    
                                <label>file</label>
                            </div>
                    </div>
                    <form>
                        <button type="submit" class="btn btn-primary">Accept</button>
                    </form>
                </div>
        </div>`
})


export class requestDetailComponent{
    visible = true;
    toggle() {
    this.visible = !this.visible;
  }
}