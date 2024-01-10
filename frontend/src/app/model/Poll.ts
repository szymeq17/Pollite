export interface Poll {
  id: number,
  ownerUsername: string,
  text: string,
  pollAnswers: PollAnswer[],
  startDateTime?: Date,
  endDateTime?: Date
}

export interface PollAnswer {
  id: number,
  text: string
}

export interface PollResults {
  results: PollAnswerResult[],
  votesTotal: number
}

export interface PollAnswerResult {
  pollAnswerText: string,
  votes: number;
  percentage: number;
}
