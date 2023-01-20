export interface SurveyResults {
  questionsResults: SurveyQuestionResults[]
}

export interface SurveyQuestionResults {
  questionId: number,
  questionText: string,
  answersResults: SurveyQuestionAnswerResults[]
}

export interface SurveyQuestionAnswerResults {
  answerId: number,
  answerText: string,
  total: number
}
