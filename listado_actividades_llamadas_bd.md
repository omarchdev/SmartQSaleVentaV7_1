# Listado de Actividades y Fragmentos con Llamadas a la Base de Datos Local

Este documento detalla qué clases en la aplicación Android (`SmartQSaleVentaV7_1`) aún realizan llamados directos o indirectos (mediante AsyncTasks heredados) a la base de datos local SQLite/SQL Server a través de la clase `BdConnectionSql`.

---

## 1. Llamados Directos (Activas)

Estas son las clases que importan y llaman activamente a métodos de `BdConnectionSql` en su código (sin estar comentados):

| Clase | Archivo / Ruta | Métodos / Propiedades Involucradas |
| :--- | :--- | :--- |
| ~~**AddEditProduct**~~ | ~~[AddEditProduct.java](file:///c:/Users/omarc/proyectos android/SmartQSaleVentaV7_1/app/src/main/java/com/omarchdev/smartqsale/smartqsaleventas/Activitys/AddEditProduct.java)~~ | ~~`bdConnectionSql.getCategorias(0, "")`~~ *(Migrado)* |
| **HistorialVentas** | [HistorialVentas.java](file:///c:/Users/omarc/proyectos android/SmartQSaleVentaV7_1/app/src/main/java/com/omarchdev/smartqsale/smartqsaleventas/Activitys/HistorialVentas.java) | `bdConnectionSql.cancelarVenta(...)` |
| **LoginActivity** | [LoginActivity.java](file:///c:/Users/omarc/proyectos android/SmartQSaleVentaV7_1/app/src/main/java/com/omarchdev/smartqsale/smartqsaleventas/Activitys/LoginActivity.java) | `bdConnectionSql.GetStringConnectionStart()`, `bdConnectionSql.SimboloMonedaPorDefecto()` |
| **PantallaPrincipal** | [PantallaPrincipal.java](file:///c:/Users/omarc/proyectos android/SmartQSaleVentaV7_1/app/src/main/java/com/omarchdev/smartqsale/smartqsaleventas/Activitys/PantallaPrincipal.java) | `BdConnectionSql.getSinglentonInstance().finalizar()` |
| **ActivityParent** | [ActivityParent.kt](file:///c:/Users/omarc/proyectos android/SmartQSaleVentaV7_1/app/src/main/java/com/omarchdev/smartqsale/smartqsaleventas/Activitys/ActivityParent.kt) | `nombreRed`, `ConectarInstanciaPrincipal()`, `isSeConectoVentas`, `setConnectionVentasFin()` |

---

## 2. Llamados Indirectos (Mediante Clases `AsyncTask`)

Estas clases no llaman a `BdConnectionSql` de forma directa, sino que invocan clases `AsyncTask` que internamente ejecutan consultas en la base de datos local (`BdConnectionSql`). 

### Actividades (Activities)
* **ActivityConfigPack**: Usa `AsyncProducto`
* **ActivityConfigProducto**: Usa `AsyncProducto`
* **ActivityEdiccionVariantes**: Usa `AsyncVariantes`
* **ActivityModificadorConfig**: Usa `AsyncModificadores`
* **ActivityRegistroCategorias**: Usa `AsyncCategoria`
* **AreasProduccionLista**: Usa `AsyncAreasProduccion`
* **CajaFlujoActivity**: Usa `AsyncCaja`, ~~`AsyncReporteCierreCaja`~~ *(Migrado)*
* **CategoriasActivity**: Usa `AsyncCategoria`
* **ConfigModificadorProducto**: Usa `AsyncModificadores`
* **DetallePedido**: Usa `AsyncPedidos`
* **DetalleVenta**: Usa `AsyncProcesoVenta`
* **HistorialCierresCaja**: Usa `AsyncUsers`
* **HistorialVentas**: Usa `AsyncUsers` (adicional a su llamada directa)
* **IngresoCompraKt**: Usa `AsyncProducto`
* **ListadoClientes**: Usa ~~`AsyncClientes`~~ *(Migrado)*
* **ListadoMedioPago**: Usa ~~AsyncMedioPago~~ *(Migrado)*
* **ListadoProductos**: Usa `AsyncProducto`
* **ListadoRoles**: Usa `AsyncRoles`
* **ListadoUsuarios**: Usa `AsyncUsers`
* **ListadoVendedores**: Usa `AsyncVendedores`
* **ListaImpresorasAreas**: Usa `AsyncImpresora`
* **LoginPrincipal12**: Usa `AsyncLogUser`, `AsyncUsers`
* **Main3Activity**: Usa `AsyncLogUser`, `AsyncVersion`
* **PantallaPrincipal**: Usa `AsyncCaja`, `AsyncUsers` (adicional a su llamada directa)
* **PedidosDespacho**: Usa `AsyncPedidos`
* **PedidosEnReserva**: Usa `AsyncPedido`
* **PedidosTienda**: Usa `AsyncPedidos`
* **PinLoginActivity**: Usa `AsyncLogUser`, `AsyncUsers`
* **PinProcesApp**: Usa `AsyncSoporte`
* **PinResetApp**: Usa `AsyncSoporte`
* **RegistroAreaProduccion**: Usa `AsyncAreasProduccion`
* **RegistroCliente**: Usa ~~`AsyncClientes`~~ *(Migrado)*
* **RegistroImpresora**: Usa `AsyncAreasProduccion`, `AsyncImpresora`
* **RegistroMedioPago**: Usa ~~AsyncMedioPago~~ *(Migrado)*
* **RegistroSubCategoria**: Usa ~~`AsyncSubCategorias`~~ *(Migrado)*
* **RegistroTienda**: Usa `AsyncTiendas`
* **RegistroUsuario**: Usa `AsyncLogUser`, `AsyncRegistroUsuario`, `AsyncTiendas`, `AsyncUsers`
* **RegistroVendedor**: Usa `AsyncVendedores`
* **Registro_Producto**: Usa `AsyncAreasProduccion`, `AsyncCategoria`, `AsyncProducto`
* **ReporteAlmacen**: Usa `AsyncReporte`
* **ReporteCierreCaja**: Usa `AsyncCaja`, `AsyncReporte`
* **ReporteIngresosRetirosPeriodo**: ~~`AsyncReporteIngresoRetiro`~~ *(Migrado)*
* **ReportePeriodoTienda**: Usa `AsyncReportePeriodo`
* **ReporteVendedor**: Usa `AsyncReporte`, ~~`AsyncReporteVendedor`~~ *(Migrado)*, `AsyncVendedor`, `AsyncVendedores`
* **ReporteVentasCaja**: Usa `AsyncCaja`, `AsyncReporte`, `AsyncVendedores`
* **ReporteVentasCierre**: Usa `AsyncCaja`, `AsyncReporte`, `AsyncVendedores`
* **ReporteVentasProductos**: ~~`AsyncReporteProductos`~~ *(Migrado)*
* **SelectTienda**: Usa `AsyncTiendas`
* **SoporteTecnico**: Usa `AsyncVersion`
* **ProductoDBasicos**: Usa `AsyncAreasProduccion`, `AsyncProductKt`, `AsyncSubCategorias`
* **ReporteVentasVendedor**: Usa `AsyncReporte`
* **SelectTienda**: Usa `AsyncTiendas`
* **VariantesProducto**: Usa `AsyncVariantes`

### Fragmentos (Fragments)
* **ConfigCategorias**: Usa `AsyncCategoria`
* **ConfiguracionRol**: Usa `AsyncRoles`
* **ConfiguracionUsuario**: Usa `AsyncUsers`
* **VentasFragment**: Usa `AsyncCabeceraVenta`, `AsyncCaja`, ~~`AsyncCategoria`~~ *(Migrado)*, `AsyncPedido`, `AsyncPedidos`, `AsyncProcesoVenta`, `AsyncProducto`, `AsyncZonaServicio`
