export interface ILote {
  id?: number;
  numero?: number | null;
}

export class Lote implements ILote {
  constructor(public id?: number, public numero?: number | null) {}
}

export function getLoteIdentifier(lote: ILote): number | undefined {
  return lote.id;
}
