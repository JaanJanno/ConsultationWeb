import {ConsultationRequest} from './request.component';
import {homeComponent} from './consultant-home.component';
import {provideRouter} from '@angular/router';


const Routes=[
    {path:'',component:homeComponent },
    {path:'request',component:ConsultationRequest},
    {path:'**',component:homeComponent}
]


export const App_Routes= [provideRouter(Routes)]