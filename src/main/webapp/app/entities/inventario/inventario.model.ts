import * as dayjs from 'dayjs';
import { ISector } from 'app/entities/sector/sector.model';

export interface IInventario {
  id?: number;
  nombre?: string | null;
  valor?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  sectors?: ISector[] | null;
}

export class Inventario implements IInventario {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public valor?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public sectors?: ISector[] | null
  ) {}
}

export function getInventarioIdentifier(inventario: IInventario): number | undefined {
  return inventario.id;
}
