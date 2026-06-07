# Seguimiento de Migración de Base de Datos Local a Web API (Retrofit)

Este documento sirve para realizar el seguimiento del estado de la migración de las operaciones de base de datos local (SQLite / SQL Server a través de `BdConnectionSql` y `DbHelper`) hacia la Web API centralizada utilizando Retrofit.

---

## 📊 Resumen de Estado

- **Total de Módulos (AsyncTask):** 36
- **Migrados:** 11 (~30.6%)
- **Pendientes:** 25 (~69.4%)

---

## 1. Módulos de Operación (AsyncTask)

A continuación se detalla la lista de clases `AsyncTask` que gestionan el acceso a los datos, marcando aquellas que ya fueron migradas por completo a Retrofit y aquellas que continúan ejecutando lógica local.

### 1.1. ✅ Completados / Migrados
Estos componentes ya no tienen dependencias directas con `BdConnectionSql` y realizan todas sus operaciones a través de la API REST:

- [x] **AsyncAlmacenes** (Gestión de almacenes, stock y transferencias)
- [x] **AsyncCategoria** (Categorías de productos — *Migrado Hoy*)
- [x] **AsyncClientes** (Gestión y registro de clientes — *Migrado Hoy*)
- [x] **AsyncMedioPago** (Gestión de medios de pago — *Migrado Hoy*)
- [x] **AsyncOperarios** (Listado y consulta de operarios de producción)
- [x] **AsyncReporteCierreCaja** (Reportes y balances financieros de cierres — *Migrado Hoy*)
- [x] **AsyncReporteIngresoRetiro** (Flujos de ingreso/retiro de dinero en caja — *Migrado Hoy*)
- [x] **AsyncReporteProductos** (Estadísticas y reportes de salida de productos — *Migrado Hoy*)
- [x] **AsyncReporteVendedor** (Reportes de rendimiento de ventas por vendedor — *Migrado Hoy*)
- [x] **AsyncStockProductos** (Consulta de stock y detalle de productos en almacén)
- [x] **AsyncSubCategorias** (Subcategorías de productos — *Migrado Hoy*)

---

### 1.2. ⏳ Pendientes de Migración
Estos componentes aún realizan llamados SQL locales en SQLite o mediante conexiones JDBC en `BdConnectionSql`:

- [ ] **AsyncAreasProduccion** (Áreas de producción de artículos)
- [ ] **AsyncCabeceraVenta** (Asignación de clientes, vendedores y descuentos a ventas)
- [ ] **AsyncCaja** (Apertura y control de flujo de caja)
- [ ] **AsyncFacturacion** (Lógica de facturación electrónica y CPE)
- [ ] **AsyncImpresora** (Configuración de impresoras locales por área)
- [ ] **AsyncLogUser** (Inicio de sesión por PIN)
- [ ] **AsyncModificadores** (Configuración de modificadores/adicionales de productos)
- [ ] **AsyncPedido** (Guardado y modificación de detalles de pedidos)
- [ ] **AsyncPedidos** (Listado y filtros de pedidos en reserva/tienda)
- [ ] **AsyncProcesoVenta** (Carga de packs, combos, adición de productos a pedidos, eliminación de detalles)
- [ ] **AsyncProductKt / AsyncProducto** (Carga de productos para la venta y configuración)
- [ ] **AsyncRegistroUsuario** (Registro y guardado de nuevos usuarios)
- [ ] **AsyncReporte** (Lógica general de reportes locales de almacén y ventas)
- [ ] **AsyncReportePeriodo** (Histórico y reportes de ventas por rangos de tiempo)
- [ ] **AsyncRoles** (Roles y permisos de usuarios)
- [ ] **AsyncSelectTienda / AsyncTiendas** (Listado y configuración de sucursales)
- [ ] **AsyncSoporte** (Validación de tokens de soporte técnico y PINs)
- [ ] **AsyncTiposDocumento** (Tipos de documento de identidad)
- [ ] **AsyncUsers** (Autenticación, roles y registro de usuarios)
- [ ] **AsyncVariantes** (Configuración de atributos, opciones y variantes de producto)
- [ ] **AsyncVendedor / AsyncVendedores** (Asignación y listados de vendedores)
- [ ] **AsyncVersion** (Lógica de verificación y descarga de actualizaciones de la app)
- [ ] **AsyncZonaServicio** (Asignación de mesas, zonas de atención y espacios)

---

## 2. Llamados Directos en Actividades / Fragmentos

Además de los `AsyncTask`, existen algunas pantallas que realizan consultas directamente en su código principal. Estas deberán ser refactorizadas para canalizar sus llamadas a través de una API:

- [x] **AddEditProduct.java** (Llama a `getCategorias` localmente — *Migrado*)
- [ ] **HistorialVentas.java** (Llama a `cancelarVenta` localmente)
- [ ] **LoginActivity.java** (Llama a `GetStringConnectionStart` y `SimboloMonedaPorDefecto` localmente)
- [ ] **PantallaPrincipal.java** (Llama a `finalizar` conexión localmente)
