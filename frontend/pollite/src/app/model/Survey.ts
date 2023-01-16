export interface Survey {
  id?: number,
  ownerUsername?: string,
  questions: SurveyQuestion[]
  configuration?: SurveyConfiguration
  description?: string
}

export interface SurveyQuestion {
  id?: number,
  type: string,
  text: string,
  order: number,
  answers: SurveyQuestionAnswer[]
}

export interface SurveyQuestionAnswer {
  id?: number,
  text: string,
  order: number
}

export interface SurveyConfiguration {
  id?: number,
  isActive?: boolean,
  startDate?: Date,
  endDate?: Date,
  exclusions?: SurveyQuestionExclusion[]
}

export interface SurveyQuestionExclusion {
  id: number,
  questionOrder: number,
  answerOrder: number,
  excludedQuestionOrder: number
}
