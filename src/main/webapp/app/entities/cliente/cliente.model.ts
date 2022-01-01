import { IMunicipio } from 'app/entities/municipio/municipio.model';
import { ICiudad } from 'app/entities/ciudad/ciudad.model';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { IPais } from 'app/entities/pais/pais.model';

export interface ICliente {
  id?: number;
  nombres?: string | null;
  apellidos?: string | null;
  tipoPersona?: string | null;
  tipoDocumento?: string | null;
  documento?: string | null;
  dv?: string | null;
  estado?: number | null;
  municipio?: IMunicipio | null;
  ciudad?: ICiudad | null;
  departamento?: IDepartamento | null;
  pais?: IPais | null;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public nombres?: string | null,
    public apellidos?: string | null,
    public tipoPersona?: string | null,
    public tipoDocumento?: string | null,
    public documento?: string | null,
    public dv?: string | null,
    public estado?: number | null,
    public municipio?: IMunicipio | null,
    public ciudad?: ICiudad | null,
    public departamento?: IDepartamento | null,
    public pais?: IPais | null
  ) {}
}

export function getClienteIdentifier(cliente: ICliente): number | undefined {
  return cliente.id;
}
