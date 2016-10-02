import {Component,OnInit} from '@angular/core';
import {requestService} from './request.service';

@Component({
    selector:'request',
    template:`
        <div class="container">
        <h2>Submit Consultation Request</h2>
            <form (ngSubmit)="onSubmit()" >
                <div class="form-group">               
                    <div class="row">
                        <div class="col-sm-1">
                            <label for="name">Name:</label>
                        </div>
                        <div class="col-sm-6">    
                            <input type="text" class="form-control" [(ngModel)]="name" placeholder="Enter name" required>
                        </div>
                    </div>    
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-sm-1">
                            <label for="email">Email:</label>
                        </div>    
                        <div class="col-sm-6">
                            <input type="email" class="form-control" [(ngModel)]="email"  placeholder="Enter email" required>
                        </div>    
                    </div>
                </div>
                                <div class="form-group">
                    <div class="row">
                        <div class="col-sm-1">
                            <label for="email">Programme:</label>
                        </div>    
                        <div class="col-sm-6">
                            <input type="text" class="form-control" [(ngModel)]="programme"  placeholder="Enter programme" required>
                        </div>    
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                            <div class="col-sm-1">
                                <label for="name">Department:</label>
                            </div>
                            <div class=col-sm-3>
                                    <div>
                                        <div>
                                            <select class="btn btn-info dropdown-toggle" [(ngModel)]="department">
                                                <option>Faculty of Art and Humanities</option>
                                                <option>Faculty of Social Sciences</option>
                                                <option>Faculty of Medicine</option>
                                                <option>Faculty of Science and Technology</option>
                                                <option>Office of Academic Affairs</option>
                                                <option>Estonian Genom Center, Univercity of Tartu</option>
                                            </select>
                                        </div>
                                    </div>    
                            </div>
                    </div>
                </div>    
                <div class="form-group">
                    <div class="row">
                            <div class="col-sm-1">
                                <label for="name">Language:</label>
                            </div>
                            <div class=col-sm-3>
                                    <div>
                                        <div>
                                            <select class="btn btn-info dropdown-toggle" [(ngModel)]="language">
                                                <option>English</option>
                                                <option>Estonian</option>
                                                <option>Russian</option>
                                                <option>German</option>
                                            </select>
                                        </div>
                                    </div>    
                            </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                            <div class="col-sm-1">
                                <label for="name">Type:</label>
                            </div>
                            <div class=col-sm-3>
                                    <div>
                                        <div>
                                            <select class="btn btn-info dropdown-toggle" [(ngModel)]="purpose">
                                                <option>Essay</option>
                                                <option>Article</option>
                                                <option>Thesis</option>
                                            </select>
                                        </div>
                                    </div>    
                            </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-sm-1">
                            <label for="name"> Comment:</label>
                        </div>
                        <div class="col-sm-6">
                            <textarea class="form-control" rows="3" [(ngModel)]="comments"></textarea>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                        <label for="email">Upload File:</label>
                        <span class="btn btn-default btn-file glyphicon glyphicon-folder-open">
                            Browse <input type="file">
                        </span>
                </div>
                <div class="row">
                        <div class="col-sm-7">
                            <button type="submit" class="btn btn-primary">Submit</button>
                        </div>
                </div>
            </form>
            <div class="form-group" *ngIf="result">
                <p>       </p>
                    <div class="alert alert-success">
                        <strong>Perfect!</strong> We received your message. Our colleague will contact you via Email.
                    </div>
                
            </div>
        </div>`,
        styleUrls:['app/app.component.css'],
        providers:[requestService ]
})

export class ConsultationRequest{
    name="";
    email="";
    department="Faculty of Art and Humanities";
    language="English";
    purpose="Essay";
    comments="";
    programme="";
    result;
  
    constructor(private http:requestService){}
   
    onSubmit(){
        console.log(this.name+" "+this.email+" "+this.department+" "+this.language+" "+this.purpose+" "+this.comments);
        let request={name:this.name,
                     email:this.email,
                     department:this.department,
                     language:this.language,
                     purpose:this.purpose,
                     programme:this.programme,
                     comments:this.comments}


        this.result=this.http.postRequest(request);
        
    }
    
}
