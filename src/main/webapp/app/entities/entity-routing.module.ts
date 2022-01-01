import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'empresa',
        data: { pageTitle: 'contable4App.empresa.home.title' },
        loadChildren: () => import('./empresa/empresa.module').then(m => m.EmpresaModule),
      },
      {
        path: 'sucursal',
        data: { pageTitle: 'contable4App.sucursal.home.title' },
        loadChildren: () => import('./sucursal/sucursal.module').then(m => m.SucursalModule),
      },
      {
        path: 'regimen',
        data: { pageTitle: 'contable4App.regimen.home.title' },
        loadChildren: () => import('./regimen/regimen.module').then(m => m.RegimenModule),
      },
      {
        path: 'usuario',
        data: { pageTitle: 'contable4App.usuario.home.title' },
        loadChildren: () => import('./usuario/usuario.module').then(m => m.UsuarioModule),
      },
      {
        path: 'cliente',
        data: { pageTitle: 'contable4App.cliente.home.title' },
        loadChildren: () => import('./cliente/cliente.module').then(m => m.ClienteModule),
      },
      {
        path: 'empleado',
        data: { pageTitle: 'contable4App.empleado.home.title' },
        loadChildren: () => import('./empleado/empleado.module').then(m => m.EmpleadoModule),
      },
      {
        path: 'trabajo',
        data: { pageTitle: 'contable4App.trabajo.home.title' },
        loadChildren: () => import('./trabajo/trabajo.module').then(m => m.TrabajoModule),
      },
      {
        path: 'municipio',
        data: { pageTitle: 'contable4App.municipio.home.title' },
        loadChildren: () => import('./municipio/municipio.module').then(m => m.MunicipioModule),
      },
      {
        path: 'ciudad',
        data: { pageTitle: 'contable4App.ciudad.home.title' },
        loadChildren: () => import('./ciudad/ciudad.module').then(m => m.CiudadModule),
      },
      {
        path: 'departamento',
        data: { pageTitle: 'contable4App.departamento.home.title' },
        loadChildren: () => import('./departamento/departamento.module').then(m => m.DepartamentoModule),
      },
      {
        path: 'pais',
        data: { pageTitle: 'contable4App.pais.home.title' },
        loadChildren: () => import('./pais/pais.module').then(m => m.PaisModule),
      },
      {
        path: 'factura',
        data: { pageTitle: 'contable4App.factura.home.title' },
        loadChildren: () => import('./factura/factura.module').then(m => m.FacturaModule),
      },
      {
        path: 'tipo-factura',
        data: { pageTitle: 'contable4App.tipoFactura.home.title' },
        loadChildren: () => import('./tipo-factura/tipo-factura.module').then(m => m.TipoFacturaModule),
      },
      {
        path: 'nota-contable',
        data: { pageTitle: 'contable4App.notaContable.home.title' },
        loadChildren: () => import('./nota-contable/nota-contable.module').then(m => m.NotaContableModule),
      },
      {
        path: 'detalle-factura',
        data: { pageTitle: 'contable4App.detalleFactura.home.title' },
        loadChildren: () => import('./detalle-factura/detalle-factura.module').then(m => m.DetalleFacturaModule),
      },
      {
        path: 'detalles-objeto',
        data: { pageTitle: 'contable4App.detallesObjeto.home.title' },
        loadChildren: () => import('./detalles-objeto/detalles-objeto.module').then(m => m.DetallesObjetoModule),
      },
      {
        path: 'objeto',
        data: { pageTitle: 'contable4App.objeto.home.title' },
        loadChildren: () => import('./objeto/objeto.module').then(m => m.ObjetoModule),
      },
      {
        path: 'codigo',
        data: { pageTitle: 'contable4App.codigo.home.title' },
        loadChildren: () => import('./codigo/codigo.module').then(m => m.CodigoModule),
      },
      {
        path: 'precio',
        data: { pageTitle: 'contable4App.precio.home.title' },
        loadChildren: () => import('./precio/precio.module').then(m => m.PrecioModule),
      },
      {
        path: 'paquete',
        data: { pageTitle: 'contable4App.paquete.home.title' },
        loadChildren: () => import('./paquete/paquete.module').then(m => m.PaqueteModule),
      },
      {
        path: 'lote',
        data: { pageTitle: 'contable4App.lote.home.title' },
        loadChildren: () => import('./lote/lote.module').then(m => m.LoteModule),
      },
      {
        path: 'vencimiento',
        data: { pageTitle: 'contable4App.vencimiento.home.title' },
        loadChildren: () => import('./vencimiento/vencimiento.module').then(m => m.VencimientoModule),
      },
      {
        path: 'tipo-objeto',
        data: { pageTitle: 'contable4App.tipoObjeto.home.title' },
        loadChildren: () => import('./tipo-objeto/tipo-objeto.module').then(m => m.TipoObjetoModule),
      },
      {
        path: 'inventario',
        data: { pageTitle: 'contable4App.inventario.home.title' },
        loadChildren: () => import('./inventario/inventario.module').then(m => m.InventarioModule),
      },
      {
        path: 'sector',
        data: { pageTitle: 'contable4App.sector.home.title' },
        loadChildren: () => import('./sector/sector.module').then(m => m.SectorModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
