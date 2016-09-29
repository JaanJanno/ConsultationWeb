import {Component} from '@angular/core';

@Component({
    selector:'request',
    template:`
        <div class="container">
        <h2>Submit Consultation Request</h2>
            <form>
                <div class="form-group">               
                    <div class="row">
                        <div class="col-sm-1">
                            <label for="name">Name:</label>
                        </div>
                        <div class="col-sm-6">    
                            <input type="text" class="form-control" id="name" placeholder="Enter name">
                        </div>
                    </div>    
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-sm-1">
                            <label for="email">Email:</label>
                        </div>    
                        <div class="col-sm-6">
                            <input type="email" class="form-control" id="email" placeholder="Enter email">
                        </div>    
                    </div>
                </div>

                <div class="form-group">
                <div class="row">
                        <div class="col-sm-1">
                            <label for="name">Department:</label>
                        </div>
                        <div class=col-sm-3>
                            <div class="dropdown">
                                <button class="btn btn-info dropdown-toggle" type="button" data-toggle="dropdown">Department
                                <span class="caret"></span></button>
                                <ul class="dropdown-menu">
                                <li><a href="#">Faculty of Art and Humanities</a></li>
                                <li><a href="#">Faculty of Social Sciences</a></li>
                                <li><a href="#">Faculty of Medicine</a></li>
                                <li><a href="#">Faculty of Science and Technology</a></li>
                                <li><a href="#">Office of Academic Affairs</a></li>
                                <li><a href="#">Estonian Genom Center, Univercity of Tartu</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-sm-1">
                            <label for="name"> Degree:</label>
                        </div>
                        <div class=col-sm-3>
                                <div class="dropdown">
                                    <button class="btn btn-info dropdown-toggle" type="button" data-toggle="dropdown"> Degree
                                    <span class="caret"></span></button>
                                    <ul class="dropdown-menu">
                                    <li><a href="#">Bachelor</a></li>
                                    <li><a href="#">Master</a></li>
                                    <li><a href="#">Phd</a></li>
                                    </ul>
                                </div>
                        </div>
                      </div>  
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-sm-1">
                            <label for="name">Type:</label>
                        </div> 
                        <div class=col-sm-2>
                                <div class="dropdown">
                                    <button class="btn btn-info dropdown-toggle" type="button" data-toggle="dropdown">Type
                                    <span class="caret"></span></button>
                                    <ul class="dropdown-menu">
                                    <li><a href="#">Thesis</a></li>
                                    <li><a href="#">Article</a></li>
                                    <li><a href="#">Essay</a></li>
                                    </ul>
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
                            <textarea class="form-control" rows="3"></textarea>
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
        </div>`,
        styleUrls:['app/app.component.css']
})

export class ConsultationRequest{}
