import * as dayjs from 'dayjs';
import { IRegimen } from 'app/entities/regimen/regimen.model';
import { IMunicipio } from 'app/entities/municipio/municipio.model';
import { ICiudad } from 'app/entities/ciudad/ciudad.model';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { IPais } from 'app/entities/pais/pais.model';
import { IEmpresa } from 'app/entities/empresa/empresa.model';

export interface ISucursal {
  id?: number;
  nombre?: string | null;
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
  regimen?: IRegimen | null;
  municipio?: IMunicipio | null;
  ciudad?: ICiudad | null;
  departamento?: IDepartamento | null;
  pais?: IPais | null;
  empresa?: IEmpresa | null;
}

export class Sucursal implements ISucursal {
  constructor(
    public id?: number,
    public nombre?: string | null,
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
    public regimen?: IRegimen | null,
    public municipio?: IMunicipio | null,
    public ciudad?: ICiudad | null,
    public departamento?: IDepartamento | null,
    public pais?: IPais | null,
    public empresa?: IEmpresa | null
  ) {}
}

export function getSucursalIdentifier(sucursal: ISucursal): number | undefined {
  return sucursal.id;
}
