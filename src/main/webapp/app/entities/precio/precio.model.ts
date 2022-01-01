import { IPaquete } from 'app/entities/paquete/paquete.model';

export interface IPrecio {
  id?: number;
  valorVenta?: number | null;
  valorCompra?: number | null;
  paquete?: IPaquete | null;
}

export class Precio implements IPrecio {
  constructor(
    public id?: number,
    public valorVenta?: number | null,
    public valorCompra?: number | null,
    public paquete?: IPaquete | null
  ) {}
}

export function getPrecioIdentifier(precio: IPrecio): number | undefined {
  return precio.id;
}
