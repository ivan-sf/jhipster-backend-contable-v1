import * as dayjs from 'dayjs';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { IMunicipio } from 'app/entities/municipio/municipio.model';
import { ICiudad } from 'app/entities/ciudad/ciudad.model';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { IPais } from 'app/entities/pais/pais.model';
import { IEmpresa } from 'app/entities/empresa/empresa.model';

export interface IUsuario {
  id?: number;
  nombres?: string | null;
  apellidos?: string | null;
  tipoPersona?: string | null;
  tipoDocumento?: string | null;
  documento?: string | null;
  dv?: string | null;
  estado?: number | null;
  rol?: string | null;
  rut?: string | null;
  nombreComercial?: string | null;
  nit?: string | null;
  direccion?: string | null;
  indicativo?: number | null;
  telefono?: number | null;
  email?: string | null;
  genero?: string | null;
  edad?: dayjs.Dayjs | null;
  foto?: string | null;
  descripcion?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  cliente?: ICliente | null;
  empleado?: IEmpleado | null;
  municipio?: IMunicipio | null;
  ciudad?: ICiudad | null;
  departamento?: IDepartamento | null;
  pais?: IPais | null;
  empresas?: IEmpresa[] | null;
}

export class Usuario implements IUsuario {
  constructor(
    public id?: number,
    public nombres?: string | null,
    public apellidos?: string | null,
    public tipoPersona?: string | null,
    public tipoDocumento?: string | null,
    public documento?: string | null,
    public dv?: string | null,
    public estado?: number | null,
    public rol?: string | null,
    public rut?: string | null,
    public nombreComercial?: string | null,
    public nit?: string | null,
    public direccion?: string | null,
    public indicativo?: number | null,
    public telefono?: number | null,
    public email?: string | null,
    public genero?: string | null,
    public edad?: dayjs.Dayjs | null,
    public foto?: string | null,
    public descripcion?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public cliente?: ICliente | null,
    public empleado?: IEmpleado | null,
    public municipio?: IMunicipio | null,
    public ciudad?: ICiudad | null,
    public departamento?: IDepartamento | null,
    public pais?: IPais | null,
    public empresas?: IEmpresa[] | null
  ) {}
}

export function getUsuarioIdentifier(usuario: IUsuario): number | undefined {
  return usuario.id;
}
