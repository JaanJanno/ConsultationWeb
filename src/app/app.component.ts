import { Component } from '@angular/core';
import {ConsultationRequest} from './con-request.component';

@Component({

  selector: 'app-root',
  template: `<request></request>`,
  directives:[ConsultationRequest]
})
export class AppComponent {
  title = 'app works!';
}
