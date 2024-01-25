import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Constants} from "../config/constants";
import {Observable} from "rxjs";
import {Poll, PollResults} from "../model/Poll";

@Injectable({providedIn: 'root'})
export class PollService {
  private POLL_API_PATH: string = Constants.API_ENDPOINT + 'polls';

  constructor(private http: HttpClient) {}

  public getPolls(page: number, pageSize: number): Observable<any> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', pageSize);

    return this.http.get(this.POLL_API_PATH, {'params': params});
  }

  public getPoll(pollId: number): Observable<Poll> {
    return this.http.get<Poll>(`${this.POLL_API_PATH}/${pollId}`);
  }

  public createPoll(poll: Poll) {
    return this.http.post(this.POLL_API_PATH, poll);
  }

  public vote(pollId: number | undefined, answerId: number | undefined): Observable<PollResults> {
    return this.http.post<PollResults>(`${this.POLL_API_PATH}/${pollId}/vote/${answerId}`, {})
  }

  public getUsersPollInfos(username: string, page: number, pageSize: number): Observable<any> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', pageSize);

    return this.http.get(this.POLL_API_PATH + `/users/${username}`, {'params': params});
  }

  public getPollResults(pollId: number): Observable<PollResults> {
    return this.http.get<PollResults>(this.POLL_API_PATH + `/${pollId}/results`);
  }

  public deletePoll(pollId: number): Observable<any> {
    return this.http.delete(this.POLL_API_PATH + `/${pollId}`);
  }
}
