import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PollsComponent} from "./poll/polls/polls/polls.component";
import {PollFormDialogComponent} from "./poll/poll-form-dialog/poll-form-dialog.component";
import {SurveyFormComponent} from "./survey/survey-form/survey-form.component";
import {CompleteSurveyFormComponent} from "./survey/complete-survey-form/complete-survey-form.component";
import {LoginComponent} from "./login/login/login.component";
import {AuthGuard} from "./auth/auth.guard";

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'polls', component: PollsComponent },
  { path: 'survey', component: SurveyFormComponent, canActivate: [AuthGuard] },
  { path: 'survey/:surveyId', component: CompleteSurveyFormComponent},
  { path: '', component: PollFormDialogComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
