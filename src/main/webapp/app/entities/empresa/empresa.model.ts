import * as dayjs from 'dayjs';
import { ISucursal } from 'app/entities/sucursal/sucursal.model';
import { IRegimen } from 'app/entities/regimen/regimen.model';
import { IMunicipio } from 'app/entities/municipio/municipio.model';
import { ICiudad } from 'app/entities/ciudad/ciudad.model';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { IPais } from 'app/entities/pais/pais.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IEmpresa {
  id?: number;
  razonSocial?: string | null;
  nombreComercial?: string | null;
  tipoDocumento?: string | null;
  documento?: string | null;
  dv?: string | null;
  direccion?: string | null;
  indicativo?: number | null;
  telefono?: number | null;
  email?: string | null;
  logo?: string | null;
  resolucionFacturas?: string | null;
  prefijoInicial?: string | null;
  prefijoFinal?: string | null;
  prefijoActual?: string | null;
  descripcion?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  estado?: number | null;
  sucursals?: ISucursal[] | null;
  regimen?: IRegimen | null;
  municipio?: IMunicipio | null;
  ciudad?: ICiudad | null;
  departamento?: IDepartamento | null;
  pais?: IPais | null;
  usuarios?: IUsuario[] | null;
}

export class Empresa implements IEmpresa {
  constructor(
    public id?: number,
    public razonSocial?: string | null,
    public nombreComercial?: string | null,
    public tipoDocumento?: string | null,
    public documento?: string | null,
    public dv?: string | null,
    public direccion?: string | null,
    public indicativo?: number | null,
    public telefono?: number | null,
    public email?: string | null,
    public logo?: string | null,
    public resolucionFacturas?: string | null,
    public prefijoInicial?: string | null,
    public prefijoFinal?: string | null,
    public prefijoActual?: string | null,
    public descripcion?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public estado?: number | null,
    public sucursals?: ISucursal[] | null,
    public regimen?: IRegimen | null,
    public municipio?: IMunicipio | null,
    public ciudad?: ICiudad | null,
    public departamento?: IDepartamento | null,
    public pais?: IPais | null,
    public usuarios?: IUsuario[] | null
  ) {}
}

export function getEmpresaIdentifier(empresa: IEmpresa): number | undefined {
  return empresa.id;
}
