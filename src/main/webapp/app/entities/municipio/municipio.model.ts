export interface IMunicipio {
  id?: number;
  nombre?: string | null;
  codigoDIAN?: string | null;
}

export class Municipio implements IMunicipio {
  constructor(public id?: number, public nombre?: string | null, public codigoDIAN?: string | null) {}
}

export function getMunicipioIdentifier(municipio: IMunicipio): number | undefined {
  return municipio.id;
}
