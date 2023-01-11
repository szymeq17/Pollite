import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PollsComponent} from "./poll/polls/polls/polls.component";
import {PollFormDialogComponent} from "./poll/poll-form-dialog/poll-form-dialog.component";
import {SurveyFormComponent} from "./survey/survey-form/survey-form.component";

const routes: Routes = [
  { path: 'polls', component: PollsComponent },
  { path: 'survey', component: SurveyFormComponent },
  { path: '', component: PollFormDialogComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
