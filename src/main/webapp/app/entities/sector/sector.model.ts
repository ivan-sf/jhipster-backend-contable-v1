import * as dayjs from 'dayjs';
import { IObjeto } from 'app/entities/objeto/objeto.model';
import { IInventario } from 'app/entities/inventario/inventario.model';

export interface ISector {
  id?: number;
  nombre?: string | null;
  valor?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  objetos?: IObjeto[] | null;
  inventario?: IInventario | null;
}

export class Sector implements ISector {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public valor?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public objetos?: IObjeto[] | null,
    public inventario?: IInventario | null
  ) {}
}

export function getSectorIdentifier(sector: ISector): number | undefined {
  return sector.id;
}
