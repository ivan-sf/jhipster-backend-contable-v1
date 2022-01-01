import { IObjeto } from 'app/entities/objeto/objeto.model';
import { IFactura } from 'app/entities/factura/factura.model';

export interface IDetalleFactura {
  id?: number;
  precio?: number | null;
  cantidad?: number | null;
  total?: number | null;
  iva?: number | null;
  valorImpuesto?: number | null;
  estado?: number | null;
  objeto?: IObjeto | null;
  factura?: IFactura | null;
}

export class DetalleFactura implements IDetalleFactura {
  constructor(
    public id?: number,
    public precio?: number | null,
    public cantidad?: number | null,
    public total?: number | null,
    public iva?: number | null,
    public valorImpuesto?: number | null,
    public estado?: number | null,
    public objeto?: IObjeto | null,
    public factura?: IFactura | null
  ) {}
}

export function getDetalleFacturaIdentifier(detalleFactura: IDetalleFactura): number | undefined {
  return detalleFactura.id;
}
