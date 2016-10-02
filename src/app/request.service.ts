import {Http} from '@angular/http';
import { Headers, RequestOptions } from '@angular/http';
import {Injectable } from '@angular/core';

@Injectable()
export class requestService{

    constructor(private _http:Http){}

    postRequest(request:any){
       
        const body=JSON.stringify(request);
        let headers = new Headers({ 'Content-Type': 'application/json' });
        let options = new RequestOptions({ headers: headers });
      
       return  this._http.post('http://consultationwebserver.herokuapp.com/requests',body,options).subscribe(res=> res.status.toString);
    }

}