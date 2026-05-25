# Global Toolbar Standardization Plan

Apply the modern toolbar style from `ActivityConfigProducto` to all activities in the project. This includes a white toolbar, specific back icon, and inline `SearchView` for activities with search functionality.

## Activities to Update

### Group 1: Activities with Search
These activities will get the `Modern` toolbar with an inline `searchViewInline`.
- `ListadoProductos.kt` (Verify/Finalize)
- `ListadoClientes.java` (Verify/Finalize)
- `ListadoProductosStock.kt`
- `IngresoCompraKt.kt`
- `CtaClientesActivity.java`
- `ActivityEdiccionVariantes.java`

### Group 2: General Activities (No Search)
These activities will get the `Modern` toolbar style but without the inline search box.
- `Registro_Producto.java`
- `ActivityRegistroProducto.java`
- `ActivityModificadorConfig.java`
- `ActivityRegistroCategoria.java`
- `CategoriasActivity.java`
- `ListadoVendedores.java`
- `ListadoTiendas.kt`
- `HistorialVentas.java`
- `DetallePedido.java`
- (And others using `Toolbar`)

## Proposed Changes

### Layouts (XML)
- Standardize the `androidx.appcompat.widget.Toolbar` structure.
- Use `style="@style/AppTheme.Toolbar.Modern"`.
- Add `app:contentInsetStartWithNavigation="0dp"`.
- If search exists: Insert `androidx.appcompat.widget.SearchView` with `id/searchViewInline`.

### Android Manifest
- Ensure all affected activities have `android:theme="@style/AppTheme.NoActionBar"`.

### Activity Code
- Initialize `Toolbar` and `setSupportActionBar`.
- Set navigation click listener to call `onBackPressed()`.
- If search exists:
    - Initialize `searchViewInline`.
    - Implement/Keep `OnQueryTextListener` logic.
    - Hide old `searchToolbar1` in `onCreateOptionsMenu`.

## Verification Plan
- Deploy and verify each activity.
- Check that the toolbar style is consistent.
- Check that the back navigation works.
- Check that search functionality (if any) is now inline and working.
- Check that other menu buttons (like "Add" or "Scan") are still present and functional.
