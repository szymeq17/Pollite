import {Injectable} from "@angular/core";
import {Constants} from "../config/constants";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Survey} from "../model/Survey";
import {CompletedSurvey} from "../model/CompletedSurvey";
import {SurveyResults} from "../model/SurveyResults";

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

  public getSurveyResults(surveyId: number, filters: any): Observable<SurveyResults> {
    return this.http.post<SurveyResults>(this.SURVEY_API_PATH + `/${surveyId}/results`, filters);
  }

  public getUsersSurveyInfos(username: string, page: number, pageSize: number): Observable<any> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', pageSize);

    return this.http.get(this.SURVEY_API_PATH + `/user/${username}`, {'params': params});
  }

  public getAllSurveyInfos(page: number, pageSize: number): Observable<any> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', pageSize);

    return this.http.get(this.SURVEY_API_PATH, {'params': params});
  }

  public deleteSurvey(surveyId: number) {
    return this.http.delete(this.SURVEY_API_PATH + `/${surveyId}`);
  }
}
