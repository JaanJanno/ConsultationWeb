import {ConsultationRequest} from './request.component';
import {consultantHomeComponent} from './consultant-home.component';
import {provideRouter} from '@angular/router';


const Routes=[
    {path:'',component:consultantHomeComponent },
    {path:'request',component:ConsultationRequest},
    {path:'**',component:consultantHomeComponent}
]


export const App_Routes= [provideRouter(Routes)]