export interface IPais {
  id?: number;
  nombre?: string | null;
  codigoDIAN?: string | null;
}

export class Pais implements IPais {
  constructor(public id?: number, public nombre?: string | null, public codigoDIAN?: string | null) {}
}

export function getPaisIdentifier(pais: IPais): number | undefined {
  return pais.id;
}
