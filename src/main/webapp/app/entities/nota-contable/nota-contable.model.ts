export interface INotaContable {
  id?: number;
  numero?: number | null;
}

export class NotaContable implements INotaContable {
  constructor(public id?: number, public numero?: number | null) {}
}

export function getNotaContableIdentifier(notaContable: INotaContable): number | undefined {
  return notaContable.id;
}
