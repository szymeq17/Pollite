export interface SurveyResults {
  questionsResults: SurveyQuestionResults[]
}

export interface SurveyQuestionResults {
  questionId: number,
  questionText: string,
  answersResults: SurveyQuestionAnswerResults[]
  total: number
}

export interface SurveyQuestionAnswerResults {
  answerId: number,
  answerText: string,
  total: number
  percentage: number
  selected: boolean;
}
