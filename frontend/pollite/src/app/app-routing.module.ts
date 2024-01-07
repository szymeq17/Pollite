import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PollsComponent} from "./poll/polls/polls/polls.component";
import {SurveyFormComponent} from "./survey/survey-form/survey-form.component";
import {CompleteSurveyFormComponent} from "./survey/complete-survey-form/complete-survey-form.component";
import {LoginComponent} from "./auth/login/login.component";
import {AuthGuard} from "./auth/auth.guard";
import {SurveyResultsComponent} from "./survey/survey-results/survey-results.component";
import {UserProfileComponent} from "./user/user-profile/user-profile.component";
import {PollFormComponent} from "./poll/poll-form/poll-form.component";
import {RegisterComponent} from "./auth/register/register.component";
import {MainComponent} from "./main/main.component";

const routes: Routes = [
  { path: '', component: MainComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'polls', component: PollsComponent },
  { path: 'poll', component: PollFormComponent, canActivate: [AuthGuard]},
  { path: 'users/:username', component: UserProfileComponent },
  { path: 'survey', component: SurveyFormComponent, canActivate: [AuthGuard] },
  { path: 'survey/:surveyId', component: CompleteSurveyFormComponent},
  { path: 'survey/:surveyId/results', component: SurveyResultsComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
