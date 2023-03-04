import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NavbarComponent } from './navbar/navbar.component';
import { MdbAccordionModule } from 'mdb-angular-ui-kit/accordion';
import { MdbCarouselModule } from 'mdb-angular-ui-kit/carousel';
import { MdbCheckboxModule } from 'mdb-angular-ui-kit/checkbox';
import { MdbCollapseModule } from 'mdb-angular-ui-kit/collapse';
import { MdbDropdownModule } from 'mdb-angular-ui-kit/dropdown';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { MdbModalModule } from 'mdb-angular-ui-kit/modal';
import { MdbPopoverModule } from 'mdb-angular-ui-kit/popover';
import { MdbRadioModule } from 'mdb-angular-ui-kit/radio';
import { MdbRangeModule } from 'mdb-angular-ui-kit/range';
import { MdbRippleModule } from 'mdb-angular-ui-kit/ripple';
import { MdbScrollspyModule } from 'mdb-angular-ui-kit/scrollspy';
import { MdbTabsModule } from 'mdb-angular-ui-kit/tabs';
import { MdbTooltipModule } from 'mdb-angular-ui-kit/tooltip';
import { MdbValidationModule } from 'mdb-angular-ui-kit/validation';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PaginationComponent } from './pagination/pagination.component';
import { PollItemComponent } from './poll/poll-item/poll-item.component';
import { PollsComponent } from './poll/polls/polls/polls.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatPaginatorModule} from "@angular/material/paginator";
import { PollFormDialogComponent } from './poll/poll-form-dialog/poll-form-dialog.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import { SurveyFormComponent } from './survey/survey-form/survey-form.component';
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import {ToastrModule} from "ngx-toastr";
import { CompleteSurveyFormComponent } from './survey/complete-survey-form/complete-survey-form.component';
import {MatCardModule} from "@angular/material/card";
import {MatRadioModule} from "@angular/material/radio";
import {AuthInterceptor} from "./auth/auth.interceptor";
import { LoginComponent } from './login/login/login.component';
import { SurveyResultsComponent } from './survey/survey-results/survey-results.component';
import { UserProfileComponent } from './user/user-profile/user-profile.component';
import {MatTabsModule} from "@angular/material/tabs";
import { SurveyInfoComponent } from './survey/survey-info/survey-info.component';
import { SurveysViewComponent } from './survey/surveys-view/surveys-view.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    PaginationComponent,
    PollItemComponent,
    PollsComponent,
    PollFormDialogComponent,
    SurveyFormComponent,
    CompleteSurveyFormComponent,
    LoginComponent,
    SurveyResultsComponent,
    UserProfileComponent,
    SurveyInfoComponent,
    SurveysViewComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        NgbModule,
        MdbAccordionModule,
        MdbCarouselModule,
        MdbCheckboxModule,
        MdbCollapseModule,
        MdbDropdownModule,
        MdbFormsModule,
        MdbModalModule,
        MdbPopoverModule,
        MdbRadioModule,
        MdbRangeModule,
        MdbRippleModule,
        MdbScrollspyModule,
        MdbTabsModule,
        MdbTooltipModule,
        MdbValidationModule,
        BrowserAnimationsModule,
        HttpClientModule,
        FormsModule,
        MatPaginatorModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatButtonModule,
        MatIconModule,
        MatCheckboxModule,
        MatDatepickerModule,
        MatNativeDateModule,
        BrowserAnimationsModule,
        ToastrModule.forRoot(),
        MatCardModule,
        MatRadioModule,
        MatTabsModule
    ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
