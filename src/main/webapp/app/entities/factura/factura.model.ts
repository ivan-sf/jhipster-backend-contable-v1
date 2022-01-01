import * as dayjs from 'dayjs';
import { ISucursal } from 'app/entities/sucursal/sucursal.model';
import { ITipoFactura } from 'app/entities/tipo-factura/tipo-factura.model';
import { INotaContable } from 'app/entities/nota-contable/nota-contable.model';
import { IDetalleFactura } from 'app/entities/detalle-factura/detalle-factura.model';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { IEmpleado } from 'app/entities/empleado/empleado.model';

export interface IFactura {
  id?: number;
  numero?: number | null;
  caja?: string | null;
  estado?: number | null;
  iva19?: number | null;
  baseIva19?: number | null;
  iva5?: number | null;
  baseIva5?: number | null;
  iva0?: number | null;
  baseIva0?: number | null;
  total?: number | null;
  pago?: number | null;
  saldo?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  fechaActualizacion?: dayjs.Dayjs | null;
  sucursal?: ISucursal | null;
  tipoFactura?: ITipoFactura | null;
  notaContable?: INotaContable | null;
  detalleFacturas?: IDetalleFactura[] | null;
  cliente?: ICliente | null;
  empleadp?: IEmpleado | null;
}

export class Factura implements IFactura {
  constructor(
    public id?: number,
    public numero?: number | null,
    public caja?: string | null,
    public estado?: number | null,
    public iva19?: number | null,
    public baseIva19?: number | null,
    public iva5?: number | null,
    public baseIva5?: number | null,
    public iva0?: number | null,
    public baseIva0?: number | null,
    public total?: number | null,
    public pago?: number | null,
    public saldo?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public fechaActualizacion?: dayjs.Dayjs | null,
    public sucursal?: ISucursal | null,
    public tipoFactura?: ITipoFactura | null,
    public notaContable?: INotaContable | null,
    public detalleFacturas?: IDetalleFactura[] | null,
    public cliente?: ICliente | null,
    public empleadp?: IEmpleado | null
  ) {}
}

export function getFacturaIdentifier(factura: IFactura): number | undefined {
  return factura.id;
}
