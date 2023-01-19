import {Injectable} from "@angular/core";
import {Constants} from "../config/constants";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Survey} from "../model/Survey";
import {CompletedSurvey} from "../model/CompletedSurvey";

@Injectable({providedIn: 'root'})
export class SurveyService {
  private SURVEY_API_PATH: string = Constants.API_ENDPOINT + 'survey';

  constructor(private http: HttpClient) {}

  public createSurvey(survey: Survey): Observable<number> {
    const headers = new HttpHeaders({Authorization: 'Basic ' + btoa('admin:admin')});
    return this.http.post<number>(this.SURVEY_API_PATH, survey, {headers: headers});
  }

  public getSurvey(surveyId: number): Observable<Survey> {
    //const headers = new HttpHeaders({Authorization: 'Basic ' + btoa('admin:admin')});
    return this.http.get<Survey>(this.SURVEY_API_PATH + `/${surveyId}`);
  }

  public submitCompletedSurvey(completedSurvey: CompletedSurvey): Observable<any> {
    return this.http.post(this.SURVEY_API_PATH + "/submit", completedSurvey);
  }
}
