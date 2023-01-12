import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Constants} from "../../config/constants";
import {Observable} from "rxjs";
import {PollResults} from "../../model/Poll";

@Injectable({providedIn: 'root'})
export class PollService {
  private POLL_API_PATH: string = Constants.API_ENDPOINT + 'poll';

  constructor(private http: HttpClient) {}

  public getPolls(page: number, pageSize: number): Observable<any> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', pageSize);

    return this.http.get(this.POLL_API_PATH, {'params': params});
  }

  public vote(pollId: number | undefined, answerId: number | undefined): Observable<PollResults> {
    return this.http.post<PollResults>(`${this.POLL_API_PATH}/${pollId}/vote/${answerId}`, {})
  }
}
