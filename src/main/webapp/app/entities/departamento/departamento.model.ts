export interface IDepartamento {
  id?: number;
  nombre?: string | null;
  codigoDIAN?: string | null;
}

export class Departamento implements IDepartamento {
  constructor(public id?: number, public nombre?: string | null, public codigoDIAN?: string | null) {}
}

export function getDepartamentoIdentifier(departamento: IDepartamento): number | undefined {
  return departamento.id;
}
