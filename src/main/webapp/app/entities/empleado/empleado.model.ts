import { ITrabajo } from 'app/entities/trabajo/trabajo.model';

export interface IEmpleado {
  id?: number;
  nombres?: string | null;
  apellidos?: string | null;
  tipoPersona?: string | null;
  tipoDocumento?: string | null;
  documento?: string | null;
  dv?: string | null;
  estado?: number | null;
  trabajo?: ITrabajo | null;
}

export class Empleado implements IEmpleado {
  constructor(
    public id?: number,
    public nombres?: string | null,
    public apellidos?: string | null,
    public tipoPersona?: string | null,
    public tipoDocumento?: string | null,
    public documento?: string | null,
    public dv?: string | null,
    public estado?: number | null,
    public trabajo?: ITrabajo | null
  ) {}
}

export function getEmpleadoIdentifier(empleado: IEmpleado): number | undefined {
  return empleado.id;
}
