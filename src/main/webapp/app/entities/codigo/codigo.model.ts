import * as dayjs from 'dayjs';
import { IPaquete } from 'app/entities/paquete/paquete.model';
import { ILote } from 'app/entities/lote/lote.model';
import { IVencimiento } from 'app/entities/vencimiento/vencimiento.model';
import { IObjeto } from 'app/entities/objeto/objeto.model';

export interface ICodigo {
  id?: number;
  barCode?: number | null;
  qrCode?: number | null;
  cantidad?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  paquete?: IPaquete | null;
  lote?: ILote | null;
  vencimiento?: IVencimiento | null;
  objeto?: IObjeto | null;
}

export class Codigo implements ICodigo {
  constructor(
    public id?: number,
    public barCode?: number | null,
    public qrCode?: number | null,
    public cantidad?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public paquete?: IPaquete | null,
    public lote?: ILote | null,
    public vencimiento?: IVencimiento | null,
    public objeto?: IObjeto | null
  ) {}
}

export function getCodigoIdentifier(codigo: ICodigo): number | undefined {
  return codigo.id;
}
