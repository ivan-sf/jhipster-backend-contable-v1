export interface ITipoFactura {
  id?: number;
  nombre?: string | null;
  prefijoInicial?: number | null;
  prefijoFinal?: number | null;
  prefijoActual?: number | null;
}

export class TipoFactura implements ITipoFactura {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public prefijoInicial?: number | null,
    public prefijoFinal?: number | null,
    public prefijoActual?: number | null
  ) {}
}

export function getTipoFacturaIdentifier(tipoFactura: ITipoFactura): number | undefined {
  return tipoFactura.id;
}
