export interface IOrgUnit {
  id?: number;
  orgUnitName?: string | null;
  dhisOrgUnitName?: string | null;
  dhisOrgUnitCode?: string | null;
}

export class OrgUnit implements IOrgUnit {
  constructor(
    public id?: number,
    public orgUnitName?: string | null,
    public dhisOrgUnitName?: string | null,
    public dhisOrgUnitCode?: string | null
  ) {}
}

export function getOrgUnitIdentifier(orgUnit: IOrgUnit): number | undefined {
  return orgUnit.id;
}
