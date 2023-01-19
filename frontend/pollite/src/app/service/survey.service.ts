import {Injectable} from "@angular/core";
import {Constants} from "../config/constants";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Survey} from "../model/Survey";
import {CompletedSurvey} from "../model/CompletedSurvey";

@Injectable({providedIn: 'root'})
export class SurveyService {
  private SURVEY_API_PATH: string = Constants.API_ENDPOINT + 'survey';

  constructor(private http: HttpClient) {}

  public createSurvey(survey: Survey): Observable<number> {
    return this.http.post<number>(this.SURVEY_API_PATH, survey);
  }

  public getSurvey(surveyId: number): Observable<Survey> {
    return this.http.get<Survey>(this.SURVEY_API_PATH + `/${surveyId}`);
  }

  public submitCompletedSurvey(completedSurvey: CompletedSurvey): Observable<any> {
    return this.http.post(this.SURVEY_API_PATH + "/submit", completedSurvey);
  }
}
