export interface ICiudad {
  id?: number;
  nombre?: string | null;
  codigoDIAN?: string | null;
}

export class Ciudad implements ICiudad {
  constructor(public id?: number, public nombre?: string | null, public codigoDIAN?: string | null) {}
}

export function getCiudadIdentifier(ciudad: ICiudad): number | undefined {
  return ciudad.id;
}
