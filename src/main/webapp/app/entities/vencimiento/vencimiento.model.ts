import * as dayjs from 'dayjs';

export interface IVencimiento {
  id?: number;
  fecha?: dayjs.Dayjs | null;
}

export class Vencimiento implements IVencimiento {
  constructor(public id?: number, public fecha?: dayjs.Dayjs | null) {}
}

export function getVencimientoIdentifier(vencimiento: IVencimiento): number | undefined {
  return vencimiento.id;
}
