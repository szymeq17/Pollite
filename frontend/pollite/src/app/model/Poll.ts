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
