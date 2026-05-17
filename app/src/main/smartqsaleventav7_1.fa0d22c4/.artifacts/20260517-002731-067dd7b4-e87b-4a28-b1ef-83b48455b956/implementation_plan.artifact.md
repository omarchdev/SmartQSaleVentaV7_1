# Implement Modern Toolbar with Inline SearchView in ListadoProductos

Apply the same toolbar and inline search style from `ActivityConfigProducto` to `ListadoProductos`.

## Proposed Changes

### Android Layouts

#### [activity_listado_productos.xml](file:///C:/Users/omarc/proyectos android/SmartQSaleVentaV7_1/app/src/main/res/layout/activity_listado_productos.xml)

- Change root to `LinearLayout` (vertical).
- Add `androidx.appcompat.widget.Toolbar` with `id/toolbar`.
- Add `androidx.appcompat.widget.SearchView` with `id/searchViewInline` inside the Toolbar.
- Wrap the existing `RelativeLayout` (containing `RecyclerView`, `ProgressBar`, `txtMensaje`, and `FAB`) in the `LinearLayout`.

### Android Activities

#### [ListadoProductos.kt](file:///C:/Users/omarc/proyectos android/SmartQSaleVentaV7_1/app/src/main/java/com/omarchdev/smartqsale/smartqsaleventas/Activitys/ListadoProductos.kt)

- Change `searchView` property type to `androidx.appcompat.widget.SearchView?`.
- In `onCreate`:
    - Initialize `Toolbar` and `setSupportActionBar(toolbar)`.
    - Initialize `searchView` from layout and set `OnQueryTextListener`.
    - Set navigation click listener on the toolbar to handle back navigation.
- In `onCreateOptionsMenu`:
    - Hide the `searchToolbar1` menu item.
    - Remove old search styling logic that was specific to the menu-based `android.widget.SearchView`.
- Update `ResultadoScanner` to use the new `searchView`.

## Verification Plan

### Manual Verification
- Deploy the app and navigate to "Listado productos".
- Verify that the toolbar shows an inline search box instead of a search icon in the menu.
- Verify that typing in the search box filters the products.
- Verify that the back arrow in the toolbar works.
- Verify that the scan icon is still available in the menu and works (it should fill the inline search box).
