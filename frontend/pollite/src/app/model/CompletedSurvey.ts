export interface CompletedSurvey {
  surveyId: number,
  completedQuestions: CompletedSurveyQuestion[]
}

export interface CompletedSurveyQuestion {
  questionId: number,
  questionAnswerIds: number[]
}
