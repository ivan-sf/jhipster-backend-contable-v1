import * as dayjs from 'dayjs';

export interface IDetallesObjeto {
  id?: number;
  nombre?: string | null;
  descripcion?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
}

export class DetallesObjeto implements IDetallesObjeto {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public descripcion?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null
  ) {}
}

export function getDetallesObjetoIdentifier(detallesObjeto: IDetallesObjeto): number | undefined {
  return detallesObjeto.id;
}
