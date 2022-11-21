import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PollItemComponent} from "./poll/poll-item/poll-item.component";

const routes: Routes = [
  { path: 'poll', component: PollItemComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
