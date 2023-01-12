import {Injectable} from "@angular/core";
import {Constants} from "../../config/constants";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Survey} from "../../model/Survey";

@Injectable({providedIn: 'root'})
export class SurveyService {
  private SURVEY_API_PATH: string = Constants.API_ENDPOINT + 'survey';

  constructor(private http: HttpClient) {}

  public createSurvey(survey: Survey): Observable<any> {
    return this.http.post(this.SURVEY_API_PATH, survey);
  }
}
