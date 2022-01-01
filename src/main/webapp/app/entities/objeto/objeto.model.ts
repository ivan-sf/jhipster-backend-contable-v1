import * as dayjs from 'dayjs';
import { ITipoObjeto } from 'app/entities/tipo-objeto/tipo-objeto.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { ICodigo } from 'app/entities/codigo/codigo.model';
import { ISector } from 'app/entities/sector/sector.model';

export interface IObjeto {
  id?: number;
  nombre?: string | null;
  cantidad?: number | null;
  codigoDian?: string | null;
  detalleObjeto?: number | null;
  vencimiento?: dayjs.Dayjs | null;
  fechaRegistro?: dayjs.Dayjs | null;
  tipoObjeto?: ITipoObjeto | null;
  usuario?: IUsuario | null;
  codigos?: ICodigo[] | null;
  sector?: ISector | null;
}

export class Objeto implements IObjeto {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public cantidad?: number | null,
    public codigoDian?: string | null,
    public detalleObjeto?: number | null,
    public vencimiento?: dayjs.Dayjs | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public tipoObjeto?: ITipoObjeto | null,
    public usuario?: IUsuario | null,
    public codigos?: ICodigo[] | null,
    public sector?: ISector | null
  ) {}
}

export function getObjetoIdentifier(objeto: IObjeto): number | undefined {
  return objeto.id;
}
