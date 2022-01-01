import { IPrecio } from 'app/entities/precio/precio.model';

export interface IPaquete {
  id?: number;
  cantidad?: number | null;
  precios?: IPrecio[] | null;
}

export class Paquete implements IPaquete {
  constructor(public id?: number, public cantidad?: number | null, public precios?: IPrecio[] | null) {}
}

export function getPaqueteIdentifier(paquete: IPaquete): number | undefined {
  return paquete.id;
}
