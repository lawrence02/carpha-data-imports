export interface IDesease {
  id?: number;
  country?: string | null;
  deseaseId?: string | null;
  caseInfo?: string | null;
  year?: string | null;
  week?: string | null;
  weekEnding?: string | null;
}

export class Desease implements IDesease {
  constructor(
    public id?: number,
    public country?: string | null,
    public deseaseId?: string | null,
    public caseInfo?: string | null,
    public year?: string | null,
    public week?: string | null,
    public weekEnding?: string | null
  ) {}
}

export function getDeseaseIdentifier(desease: IDesease): number | undefined {
  return desease.id;
}
