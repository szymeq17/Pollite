export interface SurveyInfo {
  surveyId: number,
  description: string,
  isActive: boolean,
  owner: string,
  startDate?: Date,
  endDate?: Date
}
