export interface IRegimen {
  id?: number;
  tipoRegimen?: string | null;
  nombreRegimen?: string | null;
  fechaRegistro?: string | null;
}

export class Regimen implements IRegimen {
  constructor(
    public id?: number,
    public tipoRegimen?: string | null,
    public nombreRegimen?: string | null,
    public fechaRegistro?: string | null
  ) {}
}

export function getRegimenIdentifier(regimen: IRegimen): number | undefined {
  return regimen.id;
}
