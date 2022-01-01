export interface ITrabajo {
  id?: number;
  nombre?: string | null;
  cargo?: string | null;
  salario?: number | null;
  salud?: string | null;
  pension?: string | null;
  observacon?: string | null;
  fechaRegistro?: string | null;
}

export class Trabajo implements ITrabajo {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public cargo?: string | null,
    public salario?: number | null,
    public salud?: string | null,
    public pension?: string | null,
    public observacon?: string | null,
    public fechaRegistro?: string | null
  ) {}
}

export function getTrabajoIdentifier(trabajo: ITrabajo): number | undefined {
  return trabajo.id;
}
