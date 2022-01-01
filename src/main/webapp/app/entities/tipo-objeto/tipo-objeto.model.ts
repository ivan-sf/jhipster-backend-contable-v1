export interface ITipoObjeto {
  id?: number;
  nombre?: string | null;
  fechaRegistro?: string | null;
  codigoDian?: string | null;
}

export class TipoObjeto implements ITipoObjeto {
  constructor(public id?: number, public nombre?: string | null, public fechaRegistro?: string | null, public codigoDian?: string | null) {}
}

export function getTipoObjetoIdentifier(tipoObjeto: ITipoObjeto): number | undefined {
  return tipoObjeto.id;
}
