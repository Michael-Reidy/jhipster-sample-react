export interface ISearchResult {
  id?: number;
  source?: string;
  text?: string;
  url?: string;
}

export const defaultValue: Readonly<ISearchResult> = {};
