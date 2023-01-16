import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PollsComponent} from "./poll/polls/polls/polls.component";
import {PollFormDialogComponent} from "./poll/poll-form-dialog/poll-form-dialog.component";
import {SurveyFormComponent} from "./survey/survey-form/survey-form.component";
import {CompleteSurveyFormComponent} from "./survey/complete-survey-form/complete-survey-form.component";

const routes: Routes = [
  { path: 'polls', component: PollsComponent },
  { path: 'survey', component: SurveyFormComponent },
  { path: 'survey/:surveyId', component: CompleteSurveyFormComponent},
  { path: '', component: PollFormDialogComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
