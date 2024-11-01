

package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Html
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncAlmacenes
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProducto
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.*
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAlmacen
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMovAlmacen
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.github.zawadz88.materialpopupmenu.popupMenu
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ObtenerNombreTienda
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterProductoCompra
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterSelectCompraProduct
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_ingreso_compra_kt.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class IngresoCompraKt : ActivityParent(), SlidingUpPanelLayout.PanelSlideListener, AsyncProducto.ListenerComprasProducto, RvAdapterSelectCompraProduct.ListenerPosition, DialogRegistroIngresoProductoCompra.ListenerProductCompra, RvAdapterProductoCompra.ListenerPositionProductoParaCompra, DialogGuardarProcesoAlmacen.ListenerAccionGuardarCompra, AsyncAlmacenes.ListenerRecuperarMov, SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        if(EsSalida()){
            adapterProductoCompra.LimpiarLista()
            pbProductosAlmacen.visibility=View.VISIBLE

            asyncProducto.ObtenerProductosEnAlmacen(idAlmacenOrigen,newText)
            asyncProducto.setListenerProductosAlmacen(object:AsyncProducto.ListenerProductosAlmacen{
                override fun ListadoProductosAlmacen(productList: MutableList<mProduct>?) {
                    pbProductosAlmacen.visibility=View.GONE

                    adapterProductoCompra.LimpiarLista()
                    listProductAlmacen.clear()
                    listProductAlmacen.addAll(productList!!)
                    listPr.clear()
                    listPr.addAll(listProductAlmacen)
                    adapterProductosCompras.setEsSalida(true)
                    adapterProductosCompras.AgregarProductos(ArrayList(listPr))
                    if(listPr.size==0){

                        txtMensajePAlmacen.visibility=View.VISIBLE
                        if(EsSalida()) {
                            txtMensajePAlmacen.text = "No tiene productos en el almacén seleccionado"
                        }else {
                            txtMensajePAlmacen.text = "No tiene productos con control de stock activado"

                        }
                    }
                    else if(listPr.size>0){
                        txtMensajePAlmacen.visibility=View.GONE
                    }
                }

            })
        }
        else{
            asyncProducto.ObtenerPorductosAlmacen(newText)
        }

        return false
    }
    val calendar:Calendar= Calendar.getInstance()
    val fm=this@IngresoCompraKt.fragmentManager
    val selectFecha:DialogDatePickerSelect=DialogDatePickerSelect()
    val dialogScan:DialogScannerCam= DialogScannerCam()
    var dia:Int=0
    var mes:Int=0
    var anio:Int=0
    var origen:Byte=0
    val dialogSelectAlmacen=DialogSelectAlmacen()
    val listPr= mutableListOf<mProduct>()
    var idAlmacenOrigen:Int=0
    var idAlmacenD:Int=0
    val ft=this@IngresoCompraKt.supportFragmentManager
    val asyncProducto=AsyncProducto()
    val dialogRegistroIngresoProductoCompra=DialogRegistroIngresoProductoCompra()
    private lateinit var listaAlmacen:ArrayList <mAlmacen>
    private lateinit var listaProductosEliminados:ArrayList<mProduct>
    val adapterProductoCompra= RvAdapterProductoCompra()
    val listProductAlmacen= mutableListOf<mProduct>()
    private lateinit var adapterProductosCompras: RvAdapterSelectCompraProduct
    private lateinit var menuSaveItem: MenuItem
    private lateinit var menuSearch: MenuItem
    private lateinit var menuScan: MenuItem
    var dialogGuardarProcesoAlmacen= DialogGuardarProcesoAlmacen()
    val asyncAlmacen= AsyncAlmacenes()
    var fechaGuia:String?=""
    var fechaMov=""
    var fechaCompra:String?=""
    var codigoBarra=""
    val context: Context =this
    var idMovAlmacen:Int=0
    var tipoMov=""
    var tipoAccion=0;
    var tituloPantalla=""
    var estadoEditable:String=""
    var count=0
    val controladorProcesoCargar=ControladorProcesoCargar(this)
    var permitirGuardar=true


    override fun ObtenerProductosAlmacen(productList: MutableList<mProduct>?) {
        pbProductosAlmacen.visibility=View.GONE
        listProductAlmacen.clear()
        listProductAlmacen.addAll(productList!!)
        listPr.clear()
        listPr.addAll(listProductAlmacen)
        adapterProductosCompras.AgregarProductos(ArrayList(listProductAlmacen))
        if(listPr.size==0) {

            txtMensajePAlmacen.visibility = View.VISIBLE
            txtMensajePAlmacen.text = "No tiene productos con control de stock activado"

        }else{
            txtMensajePAlmacen.visibility = View.GONE
        }
    }


    override fun getMenuInflater(): MenuInflater {
       return super.getMenuInflater()
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_ingreso_compra,menu)
        menuSaveItem=menu!!.findItem(R.id.actionCheck)
        menuSearch=menu!!.findItem(R.id.searchToolbar1)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.searchToolbar1).actionView as SearchView

        val searchableInfo = searchManager.getSearchableInfo(componentName)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        val searchimgId = resources.getIdentifier("android:id/search_button", null, null)
        val imageId = resources.getIdentifier("android:id/search_close_btn", null, null)
        val searchTextId = resources.getIdentifier("android:id/search_src_text", null, null)
        val searchBox = searchView.findViewById<View>(searchTextId) as EditText
        val searchClose = searchView.findViewById<View>(imageId) as ImageView
        val imgSearch = searchView.findViewById<View>(searchimgId) as ImageView
        searchView.queryHint = "Busqueda de producto"
        searchView.setSearchableInfo(searchableInfo)
        searchView.setOnQueryTextListener(this)
        imgSearch.setColorFilter(resources.getColor(R.color.colorAccent))
        searchClose.setColorFilter(resources.getColor(R.color.colorAccent))
        searchBox.setHintTextColor(resources.getColor(R.color.colorAccent))
        searchBox.setTextColor(resources.getColor(R.color.colorAccent))
        searchBox.highlightColor = resources.getColor(R.color.colorAccent)
        searchBox.drawingCacheBackgroundColor = resources.getColor(R.color.colorAccent)
         menuScan=menu!!.findItem(R.id.actionScanCode)
        dialogScan.setScannerResult {
        panel.panelState=SlidingUpPanelLayout.PanelState.EXPANDED
        menuScan.setVisible(true)
        searchView.onActionViewExpanded()
        searchBox.setText(it)
        }
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item!!.itemId== R.id.actionCheck){
            if(adapterProductoCompra.itemCount==0){
                Toast.makeText(this,"No permitir",Toast.LENGTH_SHORT).show()
            }else if(adapterProductoCompra.itemCount>0){

                if(!EsSalida()){
                    try {

                        if(tipoMov.equals(Constantes.MovAlmacen.IngresoTransferencia)){
                            PopUpMenuIngresoTrasnferencia(checkNotNull(findViewById(R.id.actionCheck)))
                        }else {
                            onSingleSectionWithIconsClicked(checkNotNull(findViewById(R.id.actionCheck)))
                        }
                    }catch(e:Exception){
                        Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
                    }
                }else{
                    if(tipoMov.equals(Constantes.MovAlmacen.SalidaTransferencia)){
                        if(idAlmacenD==0){
                            MensajeAdvertencia("Debe seleccionar un almacén de destino para guardar la trasacción","Adevertencia")
                        }else{
                            try {
                                if(tipoMov.equals(Constantes.MovAlmacen.IngresoTransferencia)){
                                    PopUpMenuIngresoTrasnferencia(checkNotNull(findViewById(R.id.actionCheck)))
                                }else {
                                    onSingleSectionWithIconsClicked(checkNotNull(findViewById(R.id.actionCheck)))
                                }
                            }catch(e:Exception){
                                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else{
                        try {
                           if(tipoMov.equals(Constantes.MovAlmacen.IngresoTransferencia)){
                                PopUpMenuIngresoTrasnferencia(checkNotNull(findViewById(R.id.actionCheck)))
                            }else {
                                onSingleSectionWithIconsClicked(checkNotNull(findViewById(R.id.actionCheck)))
                            }
                           }catch(e:Exception){
                            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
                           }
                    }
                }
            }

        }
        else if(item!!.itemId==R.id.actionScanCode){
            Scannear()
        }

        return super.onOptionsItemSelected(item)
    }


    private fun DialogGuardarCompra(titulo:String){
        var dialogf=dialogGuardarProcesoAlmacen
        dialogGuardarProcesoAlmacen.titulo="$titulo"
        dialogf?.show(ft, "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingreso_compra_kt)
        try {

              declareOnClickListener()
            recibirResultadoScan()
            FechaPorDefecto()
            GetFecha()
            panel.addPanelSlideListener(this)
            listaAlmacen=arrayListOf()
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
            rvSeleccionProductos.layoutManager = LinearLayoutManager(this)

            adapterProductosCompras = RvAdapterSelectCompraProduct( )
            rvSeleccionProductos.adapter=adapterProductosCompras
            panel.panelHeight = 0
            asyncProducto.setListenerComprasProducto(this)
            asyncProducto.context=this
            adapterProductosCompras.setListenerPosition(this)
            if(this.orientationScreen==0){

                rvProductoParaCompra.layoutManager=LinearLayoutManager(this)
            }else{
                rvProductoParaCompra.layoutManager = GridLayoutManager(this,2)

            }


            rvProductoParaCompra.adapter=adapterProductoCompra
            dialogRegistroIngresoProductoCompra.setListenerProductCompra(this)
            adapterProductoCompra.setListenerPositionProductoParaCompra(this)
            dialogGuardarProcesoAlmacen.setListenerAccionGuardarCompra(this)
            panel.panelHeight=0
            asyncAlmacen.setListenerRegistroCompra(listenerRegistroCompra)
            idMovAlmacen=intent.getIntExtra("idMov",0)
            tipoAccion=intent.getIntExtra("tipoAccion",resources.getInteger(R.integer.NuevoMovAlmacen))
            tipoMov= intent.getStringExtra("tipoMov").toString()
            estadoEditable= intent.getStringExtra("estadoEdt").toString()
            txtMensajePAlmacen.visibility=View.GONE

            pbProductosAlmacen.visibility=View.GONE
            VerificarMovAlmacen()

        }catch(e:Exception){
      Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()

        }
    }

    fun VerificarMovAlmacen(){
        if(idMovAlmacen>0 && tipoAccion==resources.getInteger(R.integer.EditarMovAlmacen)){
            asyncAlmacen.ObtenerMovAlmacen(idMovAlmacen)
            asyncAlmacen.setListenerRecuperarMov(this)
            contentPantalla.visibility=View.INVISIBLE
            pbIndicator.show()

        }else if(idMovAlmacen==0 && tipoAccion==resources.getInteger(R.integer.NuevoMovAlmacen)){

            contentPantalla.visibility=View.VISIBLE
            pbIndicator.hide()
            ajustarPantallaTipoMov()
            if(!EsSalida()) {
                asyncProducto.ObtenerPorductosAlmacen("")
            }
            else{
                adapterProductoCompra.esSalida=true
            }

        }
    }
    fun ajustarPantallaTipoMov(){
        when(tipoMov){
           Constantes.MovAlmacen.IngresoCompra->{
               PantallaIngresoCompra()
            }
            Constantes.MovAlmacen.IngresoIniInventario->{
                 PantallaIngresoInicialInventario()
            }
            Constantes.MovAlmacen.IngresoAjusInventario->{
                PantallaIngresoAjusteInventario()
            }
            Constantes.MovAlmacen.SalidaTransferencia->{
                PantallaSalidaTransferencia()
            }
            Constantes.MovAlmacen.SalidaCaducidad->{
                PantallaSalidaPorCaducidad()
            }
            Constantes.MovAlmacen.IngresoTransferencia->{
                PantallaIngresoTransferencia()
            }
            Constantes.MovAlmacen.SalidadDevolucionPorveedor->{
                PantallaSalidaDevolucionProveedor()
            }
            Constantes.MovAlmacen.SalidaAjusteInventario->{
                PantallaSalidaAjusteInventario()
            }
            Constantes.MovAlmacen.IngresoCancelacionVenta->{
                PantallaIngresoVentaCancelada()
            }
            Constantes.MovAlmacen.SalidaVentas->{
                PantallaSalidaVentas()
            }

        }
        contentPantalla.visibility=View.VISIBLE
        pbIndicator.hide()
    }

    fun PantallaIngresoTransferencia(){
        tituloPantalla="Ingreso por Transferencia"
        supportActionBar?.setTitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        supportActionBar?.setSubtitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        txtMensaje.text="Productos en transferencia"
        btnAlmacen.setText("Seleccion almacén")
        textAlmacen.setText("Almacén de recepción")
        textAlmacenDestino.setText("Documentos de Salida")
        btnAlmacenDestino.setText("Seleccion documento")
        btnAlmacen.visibility=View.VISIBLE
        btnAlmacenDestino.visibility=View.VISIBLE
        textAlmacenDestino.visibility=View.VISIBLE
        textAlmacen.visibility=View.VISIBLE
        btnFechaCompra.setOnClickListener(null)
        btnFechaIngreso.setOnClickListener(null)
        btnScan.visibility=View.GONE
        btnselectProduct.visibility=View.GONE
        edtGuiaProveedor.visibility=View.GONE
        btnFechaGuia.visibility=View.GONE
        edtNombreProveedor.setHint("Descripcion")
        btnFechaCompra.setText("Fecha Transferencia")

        btnFechaIngreso.setText(TextoFecha("Fecha Recepcion",dia,mes,anio))
        fechaMov= GenerarFechaTexto(dia,mes,anio)
        btnFechaIngreso.setOnClickListener(null)
        btnFechaCompra.setOnClickListener(null)
        btnAlmacenDestino.setOnClickListener{

            if(idAlmacenOrigen!=0) {
                val dialogSelectTransferencia = DialogSelectTransferencia()

                pbProductosAlmacen.visibility=View.VISIBLE
                dialogSelectTransferencia.setData(idAlmacenOrigen)
                dialogSelectTransferencia.show(ft, "")
                dialogSelectTransferencia.setListenerMovSeleccionado(object:DialogSelectTransferencia.ListenerMovSeleccionado{
                    override fun MovSeleccionado(movAlmacen: mMovAlmacen) {
                        pbProductosAlmacen.visibility=View.GONE
                        idMovAlmacen=movAlmacen.idMovAlmacen
                        btnAlmacenDestino.setText("${movAlmacen.descAlmacenI} ")
                        btnFechaCompra.setText("Fecha Transferencia\n ${movAlmacen.fechaMov}")
                        fechaCompra=movAlmacen.fechaMov.replace("/","-",false)
                        asyncAlmacen.ObtenerProductosMovimiento(movAlmacen.idMovOrigenTransf)
                        asyncAlmacen.setListenerProductosMovimiento {
                            for(i in it.indices){
                                it.get(i).setdQuantity(it.get(i).getdQuantity()*(-1))
                            }
                            adapterProductoCompra.esSalida=true
                            adapterProductoCompra.visible=false
                            adapterProductoCompra.addProductList(ArrayList(it))

                            if(it!!.size>0){
                                txtMensaje.visibility=View.INVISIBLE

                            }

                        }
                    }

                })
            }else{
                MensajeAdvertencia("Debe seleccionar un almacen de recepcion para ver las transferencias disponibles","Advertencia")
            }


        }
    }

    fun PantallaIngresoCompra(){

        tituloPantalla="Ingreso por compra"
        supportActionBar?.setTitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        supportActionBar?.setSubtitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        textAlmacenDestino.visibility=View.INVISIBLE
        btnAlmacenDestino.visibility=View.INVISIBLE
        pbProductosAlmacen.visibility=View.VISIBLE
        txtInfoProduct.text="Precio Compra"
    }
    fun PantallaIngresoVentaCancelada(){

        edtNombreProveedor.hint="Observación"
        edtNombreProveedor.maxLines=3
        textAlmacenDestino.visibility=View.GONE
        textAlmacen.visibility=View.GONE
        val params = LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        edtNombreProveedor.layoutParams=params
        btnScan.visibility=View.GONE
        btnselectProduct.visibility=View.GONE
        edtNombreProveedor.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.comentario_libro,0)
        btnFechaIngreso.visibility=View.GONE
        edtGuiaProveedor.visibility=View.GONE
        btnFechaGuia.visibility=View.GONE
        btnAlmacen.visibility=View.GONE
        btnAlmacenDestino.visibility=View.GONE
        tituloPantalla="Ingreso cancelación venta"
        supportActionBar?.setTitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        supportActionBar?.setSubtitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        btnAlmacenDestino.visibility=View.GONE
        btnAlmacen.visibility=View.GONE

    }

    fun PantallaIngresoAjusteInventario(){
        btnFechaCompra.visibility=View.GONE
        edtNombreProveedor.hint="Observación"
        edtGuiaProveedor.visibility=View.GONE
        btnFechaGuia.visibility=View.GONE
        edtNombreProveedor.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.comentario_libro,0)
        tituloPantalla="Ingreso Ajuste Inventario"
        supportActionBar?.setTitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        supportActionBar?.setSubtitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        textAlmacenDestino.visibility=View.INVISIBLE
        btnAlmacenDestino.visibility=View.INVISIBLE
        pbProductosAlmacen.visibility=View.VISIBLE
        txtInfoProduct.text="Precio Compra"
    }

    fun PantallaSalidaTransferencia(){

        btnFechaCompra.visibility=View.GONE
        edtNombreProveedor.hint="Observación"
        edtGuiaProveedor.visibility=View.GONE
        btnFechaGuia.visibility=View.GONE
        edtNombreProveedor.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.comentario_libro,0)
        tituloPantalla="Salida Transferencia"
        textAlmacenDestino.visibility=View.VISIBLE
        btnAlmacenDestino.visibility=View.VISIBLE
        textAlmacen.setText("Almacén de Origen")
        txtInfoProduct.text="Cantidad disponible"
        textAlmacenDestino.setText("Almacén de Destino ")
        supportActionBar?.setTitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        supportActionBar?.setSubtitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        btnAlmacenDestino.setText("Seleccione almacén")

    }

    fun PantallaIngresoInicialInventario(){
        txtInfoProduct.text="Precio Compra"
        btnFechaCompra.visibility=View.GONE
        edtNombreProveedor.hint="Observación"
        edtGuiaProveedor.visibility=View.GONE
        btnFechaGuia.visibility=View.GONE
        edtNombreProveedor.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.comentario_libro,0)
        tituloPantalla="Ingreso Inventario"
        supportActionBar?.setTitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        textAlmacenDestino.visibility=View.INVISIBLE
        btnAlmacenDestino.visibility=View.INVISIBLE
        pbProductosAlmacen.visibility=View.VISIBLE

    }
    fun PantallaSalidaPorCaducidad(){
        txtInfoProduct.text="Cantidad disponible"
        btnFechaCompra.visibility=View.GONE
        edtNombreProveedor.hint="Observación"
        edtGuiaProveedor.visibility=View.GONE
        btnFechaGuia.visibility=View.GONE
        edtNombreProveedor.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.comentario_libro,0)
        tituloPantalla="Salida por Caducidad"
        textAlmacenDestino.visibility=View.INVISIBLE
        btnAlmacenDestino.visibility=View.INVISIBLE
        supportActionBar?.setTitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        supportActionBar?.setSubtitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        btnAlmacen.text="Nombre Almacén"
        textAlmacen.text="Almacén Origen"

    }
    fun PantallaSalidaVentas(){
        btnFechaCompra.visibility=View.GONE
        edtNombreProveedor.hint="Observación"
        edtGuiaProveedor.visibility=View.GONE
        btnFechaGuia.visibility=View.GONE
        edtNombreProveedor.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.comentario_libro,0)
        tituloPantalla="Salida por ventas"
        textAlmacenDestino.visibility=View.INVISIBLE
        btnAlmacenDestino.visibility=View.INVISIBLE
        supportActionBar?.setTitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        supportActionBar?.setSubtitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        btnAlmacen.text="Nombre Almacén"
        textAlmacen.text="Almacén Origen"
        txtInfoProduct.text="Cantidad disponible"


    }

    fun PantallaSalidaAjusteInventario(){
        btnFechaCompra.visibility=View.GONE
        edtNombreProveedor.hint="Observación"
        edtGuiaProveedor.visibility=View.GONE
        btnFechaGuia.visibility=View.GONE
        edtNombreProveedor.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.comentario_libro,0)
        tituloPantalla="Salida Ajuste Inventario"
        textAlmacenDestino.visibility=View.INVISIBLE
        btnAlmacenDestino.visibility=View.INVISIBLE
        supportActionBar?.setTitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        supportActionBar?.setSubtitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        btnAlmacen.text="Nombre Almacén"
        textAlmacen.text="Almacén Origen"
        txtInfoProduct.text="Cantidad disponible"


    }

    fun PantallaSalidaDevolucionProveedor(){
        txtInfoProduct.text="Cantidad disponible"
        btnFechaCompra.visibility=View.GONE
        edtNombreProveedor.hint="Proveedor"
        edtGuiaProveedor.visibility=View.GONE
        btnFechaGuia.visibility=View.GONE
        edtNombreProveedor.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.truck,0)
        tituloPantalla="Salida Devolución Proveedor"
        textAlmacenDestino.visibility=View.INVISIBLE
        btnAlmacenDestino.visibility=View.INVISIBLE
        supportActionBar?.setTitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        supportActionBar?.setSubtitle(Html.fromHtml(
                "<font color=\"#757575\">" + tituloPantalla +
                        "</font>"))
        btnAlmacen.text="Nombre Almacén"
        textAlmacen.text="Almacén Origen"

    }

    val listenerRegistroCompra=object:AsyncAlmacenes.ListenerRegistroCompra{
        override fun FaltaStock() {
            Toast.makeText(context,"Stock insuficiente",Toast.LENGTH_SHORT).show()
            MensajeAdvertencia("Stock insuficiente para realizar el movimiento","Advertencia")
            controladorProcesoCargar.FinalizarDialogCarga()
        }

        override fun RegistroProcesarExito() {
            controladorProcesoCargar.FinalizarDialogCarga()
            MensajeTransaccionCompleta("Confirmación","Movimiento realizado con éxito")
            menuSearch.setVisible(false)
            menuSaveItem.setVisible(false)
            btnAlmacen.isEnabled=false
            btnAlmacenDestino.isEnabled=false
            btnScan.isEnabled=false
            btnFechaIngreso.isEnabled=false
            btnFechaGuia.isEnabled=false
            btnFechaCompra.isEnabled=false
            btnselectProduct.isEnabled=false
            adapterProductoCompra.editableList(false)
        }

        override fun RegistrogGuardarCompletar(){
        controladorProcesoCargar.FinalizarDialogCarga()
            MensajeTransaccionCompleta("Confirmación","Se guardo el movimiento con éxito")
            menuSearch.setVisible(false)
            menuSaveItem.setVisible(false)
            btnAlmacen.isEnabled=false
            btnAlmacenDestino.isEnabled=false
            btnScan.isEnabled=false
            btnFechaIngreso.isEnabled=false
            btnFechaGuia.isEnabled=false
            btnFechaCompra.isEnabled=false
            btnselectProduct.isEnabled=false
            adapterProductoCompra.editableList(false)
        }

        override fun RegistroProcesarIncompleto() {
            try {
                controladorProcesoCargar.FinalizarDialogCarga()
                MensajeTransaccionCompleta("Error", "Error al procesar el movimiento")
                menuSearch.setVisible(false)
                menuSaveItem.setVisible(false)
                btnAlmacen.isEnabled=false
                btnAlmacenDestino.isEnabled=false
                btnScan.isEnabled=false
                btnFechaIngreso.isEnabled=false
                btnFechaGuia.isEnabled=false
                btnFechaCompra.isEnabled=false
                btnselectProduct.isEnabled=false
                adapterProductoCompra.editableList(false)
            }
            catch (e:Exception){
                e.toString()
            }
        }

        override fun ErrorProcedimiento() {
            controladorProcesoCargar.FinalizarDialogCarga()
            MensajeTransaccionCompleta("Error","Error al procesar el movimiento")
            menuSearch.setVisible(false)
            menuSaveItem.setVisible(false)
            btnAlmacen.isEnabled=false
            btnAlmacenDestino.isEnabled=false
            btnScan.isEnabled=false
            btnFechaIngreso.isEnabled=false
            btnFechaGuia.isEnabled=false
            btnFechaCompra.isEnabled=false
            btnselectProduct.isEnabled=false
            adapterProductoCompra.editableList(false)
        }

        override fun ErrorConnection() {
            controladorProcesoCargar.FinalizarDialogCarga()
            MensajeTransaccionCompleta("Error al procesar el ingreso","Error")
            menuSearch.setVisible(false)
            menuSaveItem.setVisible(false)
            btnAlmacen.isEnabled=false
            btnAlmacenDestino.isEnabled=false
            btnScan.isEnabled=false
            btnFechaIngreso.isEnabled=false
            btnFechaGuia.isEnabled=false
            btnFechaCompra.isEnabled=false
            btnselectProduct.isEnabled=false
            adapterProductoCompra.editableList(false)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    override fun onResume() {
        super.onResume()

    }

    private fun FechaPorDefecto(){
        dia=calendar.get(Calendar.DAY_OF_MONTH)
        mes=calendar.get(Calendar.MONTH)+1
        anio=calendar.get(Calendar.YEAR)
        fechaGuia=GenerarFechaTexto(dia,mes,anio)
        fechaMov=GenerarFechaTexto(dia,mes,anio)
        fechaCompra=GenerarFechaTexto(dia,mes,anio)
        btnFechaCompra.setText(TextoFecha(resources.getString(R.string.FechaCompra),dia,mes,anio))
        btnFechaIngreso.setText(TextoFecha(resources.getString(R.string.FechaMovimiento),dia,mes,anio))
        btnFechaGuia.setText(TextoFecha(resources.getString(R.string.FechaGuia),dia,mes,anio))
    }
    //Eventos click fechas
    private fun declareOnClickListener(){
        btnAlmacen.setOnClickListener{

            dialogSelectAlmacen?.show(ft, "")
            dialogSelectAlmacen.setListenerAlmacen(object:DialogSelectAlmacen.AlmacenSeleccionListener{
                override fun obtenerAlmacen(idAlmacen: Int, descripcion: String) {
                    idAlmacenOrigen=idAlmacen
                    btnAlmacen.setText(descripcion)
                    //Eventos cambio almacen origen en la salida
                    if(EsSalida()){
                        adapterProductoCompra.LimpiarLista()
                        pbProductosAlmacen.visibility=View.VISIBLE

                        asyncProducto.ObtenerProductosEnAlmacen(idAlmacenOrigen,"")
                        asyncProducto.setListenerProductosAlmacen(object:AsyncProducto.ListenerProductosAlmacen{
                            override fun ListadoProductosAlmacen(productList: MutableList<mProduct>?) {
                                pbProductosAlmacen.visibility=View.GONE

                                adapterProductoCompra.LimpiarLista()
                                listProductAlmacen.clear()
                                listProductAlmacen.addAll(productList!!)
                                listPr.clear()
                                listPr.addAll(listProductAlmacen)
                                adapterProductosCompras.setEsSalida(true)
                                adapterProductosCompras.AgregarProductos(ArrayList(listPr))
                                if(listPr.size==0){

                                    txtMensajePAlmacen.visibility=View.VISIBLE
                                    if(EsSalida()) {
                                        txtMensajePAlmacen.text = "No tiene productos en el almacén seleccionado"
                                    }else {
                                        txtMensajePAlmacen.text = "No tiene productos con control de stock activado"

                                    }
                                }
                                else if(listPr.size>0){
                                    txtMensajePAlmacen.visibility=View.GONE
                                }
                   }

                        })
                    }
                }
            })

        }
        btnAlmacenDestino.setOnClickListener{
            dialogSelectAlmacen?.show(ft, "")
            dialogSelectAlmacen.setListenerAlmacen(object:DialogSelectAlmacen.AlmacenSeleccionListener{
                override fun obtenerAlmacen(idAlmacen: Int, descripcion: String) {
                        idAlmacenD=idAlmacen
                        btnAlmacenDestino.setText(descripcion)
                }
            })
        }

        btnselectProduct.setOnClickListener{
            if(!EsSalida()) {
                panel.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
            }else{
                if(idAlmacenOrigen==0){
                    MensajeAdvertencia("Debe selecciona un almacén de origen para visualizar sus productos","Advertencia");
                }
                else if(idAlmacenOrigen>0){
                    panel.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                }

            }
        }

        btnFechaCompra.setOnClickListener{

            MostrarSeleccionarFecha(1)
        }
       btnFechaIngreso.setOnClickListener{
            MostrarSeleccionarFecha(2)
        }
        btnFechaGuia.setOnClickListener {
           MostrarSeleccionarFecha(3)
       }
        btnScan.setOnClickListener {
            Scannear()
        }

    }

    private fun MostrarSeleccionarFecha(origen:Byte){
        selectFecha.setOrigen(origen,calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.DAY_OF_MONTH))
        selectFecha.show(fm,"Fecha")
    }

    private fun GetFecha(){
        selectFecha.setFechaListener {
            day: Int, month: Int, year: Int, origen: Byte ->
            dia=day
            mes=month
            anio=year
            this.origen=origen
            when(origen){
                1.toByte()->{
                    fechaCompra=GenerarFechaTexto(dia,mes,anio)
                    btnFechaCompra.setText(TextoFecha(resources.getString(R.string.FechaCompra),dia,mes,anio))}
                2.toByte()->{
                    fechaMov=GenerarFechaTexto(dia,mes,anio)
                    btnFechaIngreso.setText(TextoFecha(resources.getString(R.string.FechaMovimiento),dia,mes,anio))}
                3.toByte()->{
                    fechaGuia=GenerarFechaTexto(dia,mes,anio)
                    btnFechaGuia.setText(TextoFecha(resources.getString(R.string.FechaGuia),dia,mes,anio))}

            }
        }
    }

    override fun onBackPressed() {
        when (panel.panelState){
            SlidingUpPanelLayout.PanelState.EXPANDED->panel.panelState=SlidingUpPanelLayout.PanelState.COLLAPSED
            else->super.onBackPressed()
        }
    }

    private fun GenerarFechaTexto(dia:Int, mes:Int, anio:Int):String="$dia-$mes-$anio"

    private  fun TextoFecha(titulo:String, dia:Int, mes:Int, anio:Int):String{
        return "$titulo \n $dia/$mes/$anio"
    }

    private fun Scannear(){
            dialogScan.show(parent.fragmentManager,"ScanBarCode")
    }
    private fun recibirResultadoScan(){
        dialogScan.setScannerResult {

            codigoBarra=it
            panel.panelState=SlidingUpPanelLayout.PanelState.EXPANDED
            adapterProductosCompras.AgregarProductos(ArrayList(listPr.filter { it.codigoBarra.equals(codigoBarra)}))
        }
    }

    override fun onPanelSlide(panel: View?, slideOffset: Float) {
   }



    override fun positionSelectProductAlmacen(position: Int) {
        try {
            if (!EsSalida()) {
                dialogRegistroIngresoProductoCompra.setInfoProducto(listProductAlmacen.get(position).getcProductName(),
                        0f,
                        listProductAlmacen.get(position).idProduct
                )
                dialogRegistroIngresoProductoCompra?.show(fm, "")
            } else {
                when (CantidadDisponible(position)) {
                    true -> {
                        val dialogSelectCantidad=DialogSeleccionarCantidad()
                        dialogSelectCantidad?.ObtenerInfoProduct(listProductAlmacen.get(position).getcProductName(),
                                listProductAlmacen.get(position).idProduct,
                        listProductAlmacen.get(position).getdQuantity())
                        dialogSelectCantidad?.show(fm, "")
                        dialogSelectCantidad?.setObtenerCantidadSeleccionada(object : DialogSeleccionarCantidad.ObtenerCantidadSeleccionada {
                            override fun ObtenerDatosProducto(product: mProduct) {
                                if(adapterProductoCompra.getProductList().filter { it->it.idProduct==product.idProduct }.size==1) {
                                    count=0
                                    while(count<adapterProductoCompra.getProductList().size){
                                        if(adapterProductoCompra.getProductList().get(count).idProduct==product.idProduct)
                                        adapterProductoCompra.getProductList().get(count).setdQuantity(product.getdQuantity())
                                        adapterProductoCompra.actualizarLista()
                                        count++
                                    }
                                }else {
                                       adapterProductoCompra.AgregarProductosSeleccionados(product)
                                }
                                txtMensaje.visibility = View.INVISIBLE
                                panel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                            }
                        })
                    }
                    false -> MensajeAdvertencia("El artículo ${listProductAlmacen.get(position).getcProductName()} no tiene stock disponible", "Advertencia")
                }
            }
        }catch (e:Exception){
            Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
        }

    }

    fun CantidadDisponible(position:Int):Boolean{

        when(listProductAlmacen.get(position).getdQuantity()){

            0f->return false
            else->return true

        }
    }
    override fun productoSeleccionadoParaCompra(product: mProduct) {
        txtMensaje.visibility=View.INVISIBLE
        adapterProductoCompra.AgregarProductosSeleccionados(product)
        panel.panelState=SlidingUpPanelLayout.PanelState.COLLAPSED
    }

    override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
        when(newState){
            SlidingUpPanelLayout.PanelState.EXPANDED->{
        //        toolbarCompras?.setTitle("Seleccion producto")
                supportActionBar!!.setTitle(Html.fromHtml("<font color=\"#757575\">" + "Seleccion producto" + "</font>"))
                menuSaveItem.setVisible(false)
                menuSearch.setVisible(true)
                menuScan.setVisible(true)
            }
            SlidingUpPanelLayout.PanelState.COLLAPSED->{
      //          toolbarCompras?.setTitle("Ingreso por Compra")
                supportActionBar?.setTitle(Html.fromHtml(
                        "<font color=\"#757575\">" + tituloPantalla +
                                "</font>"))

                menuSaveItem.setVisible(true)
                menuSearch.setVisible(false)
                menuScan.setVisible(false)
            }
        }
    }

    override fun GetPositionProPos(position: Int, producto: mProduct) {
        MensajeAlerta(TextoMensajeEliminar(producto.getcProductName()),position)

    }


    private fun TextoMensajeEliminar(nombre:String):String="¿Desea eliminar el producto '$nombre' de la lista?"

    internal fun  MensajeAdvertencia(mensaje:String,titulo:String){
        AlertDialog.Builder(this)
                .setMessage(mensaje).setNegativeButton("Salir",null).setTitle(titulo).create().show()
    }

    internal fun MensajeAlerta(mensaje:String,position:Int){

        AlertDialog.Builder(this)
                .setMessage(mensaje).setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
                    adapterProductoCompra.eliminarProductoPosition(position)
                    if(adapterProductoCompra.itemCount==0){
                        txtMensaje.visibility=View.VISIBLE
                    }
                }).setNegativeButton("Cancelar",null).create().show()
    }

    override fun GuardarSinProcesar() {

        when(tipoAccion) {

            resources.getInteger(R.integer.NuevoMovAlmacen)->
            asyncAlmacen.RegistrarMovAlmacen(0,fechaMov, fechaGuia, fechaCompra, edtGuiaProveedor.text.toString(),
                    edtNombreProveedor.text.toString(), idAlmacenOrigen,idAlmacenD, adapterProductoCompra.getProductList(), "A", tipoMov,"N",EsSalida())

            resources.getInteger(R.integer.EditarMovAlmacen)->
                asyncAlmacen.RegistrarMovAlmacen(idMovAlmacen,fechaMov, fechaGuia, fechaCompra, edtGuiaProveedor.text.toString(),
                        edtNombreProveedor.text.toString(), idAlmacenOrigen,idAlmacenD, adapterProductoCompra.getProductList(), "A", tipoMov,"E",EsSalida())

        }
        controladorProcesoCargar.IniciarDialogCarga("Guardando movimiento de almacén")
    }

    override fun ProcesarGuardar() {
        if (EsSalida()) {
            fechaGuia=null

        }
        permitirGuardar=true
        if(tipoMov.equals(Constantes.MovAlmacen.SalidaTransferencia)){
            if(idAlmacenOrigen==idAlmacenD){
                permitirGuardar=false
                MensajeAdvertencia("No puede realizar transferencias al mismo almacén","Advertencia")
            }
        }

        if(!EsSalida()){
            if(idAlmacenOrigen==0){
                 permitirGuardar=false
                 btnAlmacen.setText("Elija un almacén")
                 btnAlmacen.setTextColor(Color.RED)
                 MensajeAdvertencia("Debe elegir un almacén para realizar ","Advertencia")
            }
        }

        if(permitirGuardar) {
            when (tipoAccion) {
                resources.getInteger(R.integer.NuevoMovAlmacen) -> {
                    controladorProcesoCargar.IniciarDialogCarga("Procesando movimiento")
                    asyncAlmacen.RegistrarMovAlmacen(0, fechaMov, fechaGuia, fechaCompra, edtGuiaProveedor.text.toString(),
                            edtNombreProveedor.text.toString(), idAlmacenOrigen, idAlmacenD, adapterProductoCompra.getProductList(), "P", tipoMov, "N", EsSalida())
                }
                resources.getInteger(R.integer.EditarMovAlmacen) -> {
                    controladorProcesoCargar.IniciarDialogCarga("Procesando movimiento")

                    asyncAlmacen.RegistrarMovAlmacen(idMovAlmacen, fechaMov, fechaGuia, fechaCompra, edtGuiaProveedor.text.toString(),
                            edtNombreProveedor.text.toString(), idAlmacenOrigen, idAlmacenD, adapterProductoCompra.getProductList(), "P", tipoMov, "E", EsSalida())
                }
            }
        }
    }


    //Mostrar datos de movimiento consultado por id
    override fun DatosMovimiento(movAlmacen: mMovAlmacen?, productList: ArrayList<mProduct>?) {
       try {
           btnAlmacen.setText(movAlmacen?.descAlmacenI)
           idMovAlmacen = movAlmacen!!.idMovAlmacen
           idAlmacenOrigen = movAlmacen!!.idAlmacenI
           idAlmacenD = movAlmacen!!.idAlmacenD
           fechaGuia = movAlmacen!!.fechaGuia.replace("/", "-", false)
           fechaMov = movAlmacen!!.fechaMov.replace("/", "-", false)
           fechaCompra = movAlmacen!!.fechaFactura.replace("/", "-", false)
           btnFechaCompra.setText("${resources.getString(R.string.FechaCompra)} \n ${fechaCompra?.replace("-", "/", false)}")
           btnFechaGuia.setText("${resources.getString(R.string.FechaGuia)} \n ${fechaGuia?.replace("-", "/", false)}")
           btnFechaIngreso.setText("${resources.getString(R.string.FechaMovimiento)} \n ${fechaMov?.replace("-", "/", false)}")
           edtNombreProveedor.setText("${movAlmacen!!.descripcionMov}")
           edtGuiaProveedor.setText("${movAlmacen!!.nroGuia}")
           tipoMov = movAlmacen.codTransaccion
           if (productList!!.size > 0) {
               txtMensaje.visibility = View.INVISIBLE
           }
           if (idAlmacenOrigen == 0) {
               btnAlmacen.text = "Nombre almacén"
           }
           if (estadoEditable.equals(Constantes.EstadoEditar.noEditable)) {
                btnselectProduct.visibility = View.GONE
                btnScan.visibility = View.GONE
                btnAlmacen.isEnabled = false
                btnFechaCompra.isEnabled = false
                btnFechaIngreso.isEnabled = false
                btnFechaGuia.isEnabled = false
                btnScan.isEnabled = false
                edtNombreProveedor.isEnabled = false
                edtGuiaProveedor.isEnabled = false
                btnselectProduct.isEnabled = false
                menuSaveItem.setVisible(false)
                menuSearch.setVisible(false)
                menuScan.setVisible(false)
                adapterProductoCompra.editableList(false)
            }
           if (!EsSalida()) {
                if (!tipoMov.equals(Constantes.MovAlmacen.IngresoCancelacionVenta)) {
           //         asyncProducto.ObtenerPorductosAlmacen("")
                }
            }
            else {
                adapterProductoCompra.esSalida = true
                /*if (!tipoMov.equals(Constantes.MovAlmacen.IngresoTransferencia))
                    productList.forEach {
                        it.setdQuantity(it.getdQuantity()*(-1))
                    }*/
              /*     for (i in productList.indices) {
                         productList.get(i).setdQuantity(productList.get(i).getdQuantity() * (-1))
                    }*/
            }
           if (tipoMov.equals(Constantes.MovAlmacen.IngresoTransferencia)) {

           }

           if(tipoMov.equals(Constantes.MovAlmacen.IngresoCancelacionVenta)){

           }

            GlobalScope.launch {
               adapterProductoCompra.addProductListV2(productList)
             launch(Dispatchers.Main){
                  try{
                      adapterProductoCompra.notifyDataSetChanged()
                  }catch (f:Exception){
                    Log.e("Error",f.toString())
                  }
              }
           }

          ajustarPantallaTipoMov()

           if (estadoEditable.equals(Constantes.EstadoEditar.noEditable)) {
               btnselectProduct.visibility = View.GONE
               btnScan.visibility = View.GONE
               btnAlmacen.isEnabled = false
               btnFechaCompra.isEnabled = false
               btnFechaIngreso.isEnabled = false
               btnFechaGuia.isEnabled = false
               btnScan.isEnabled = false
               edtNombreProveedor.isEnabled = false
               btnAlmacenDestino.isEnabled = false
               edtGuiaProveedor.isEnabled = false
               btnselectProduct.isEnabled = false
               menuSaveItem.setVisible(false)
               menuSearch.setVisible(false)
               menuScan.setVisible(false)
               adapterProductoCompra.editableList(false)
               btnAlmacen.setText("${movAlmacen?.descAlmacenI} ${ObtenerNombreTienda(movAlmacen.idTiendaOrigen)}")
               btnFechaCompra.setText("Fecha Transferencia \n ${fechaCompra?.replace("-", "/", false)}")
           }
       }catch (e:Exception){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()
        }
    }

    override fun ErrorProcedimient() {
        Toast.makeText(this,"Error pro",Toast.LENGTH_SHORT).show()
    }

    override fun ErrorConnection() {
        Toast.makeText(this,"Error con" +
                "",Toast.LENGTH_SHORT).show()
    }

    fun EsSalida():Boolean{
        var respuesta=false
        when(tipoMov){

            Constantes.MovAlmacen.SalidaAjusteInventario->respuesta=true
            Constantes.MovAlmacen.SalidaCaducidad->respuesta=true
            Constantes.MovAlmacen.SalidaTransferencia->respuesta=true
            Constantes.MovAlmacen.SalidadDevolucionPorveedor->respuesta=true
            Constantes.MovAlmacen.IngresoTransferencia->respuesta=true

        }
        return respuesta
    }

    fun PopUpMenuIngresoTrasnferencia(view:View){
        val popupMenu = popupMenu {
            section {
                title="Opciones"
                item {
                    label = "Procesar transferencia"
                    icon = R.drawable.check //optional
                    callback = { //optional
                        procesoTransferirMovimiento()
                       }
                }

            }
        }

        popupMenu.show(this@IngresoCompraKt, view)

    }

    fun MensajeTransaccionCompleta(titulo: String,mensaje:String){
        AlertDialog.Builder(this)
                .setMessage(mensaje).setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
                    finish()

                }).setTitle(titulo).create().show()
    }
    fun procesoTransferirMovimiento(){
        asyncAlmacen.CompletarMovTransferencia(idMovAlmacen,fechaMov,edtNombreProveedor.text.toString(),fechaCompra)
        controladorProcesoCargar.IniciarDialogCarga("Procesando transferencia")
        menuSaveItem.setVisible(false)
        edtNombreProveedor.inputType=InputType.TYPE_NULL
        asyncAlmacen.setListenerCompletarMovTrans(object:AsyncAlmacenes.ListenerCompletarMovTrans{
            override fun ExitoTransferencia() {
                controladorProcesoCargar.FinalizarDialogCarga()
                menuSaveItem.setVisible(false)
                MensajeTransaccionCompleta("Confirmación","Ingreso por transferencia realizado con éxito")
                btnAlmacen.isEnabled=false
                btnAlmacenDestino.isEnabled=false
                btnScan.isEnabled=false
                btnFechaIngreso.isEnabled=false
                btnFechaGuia.isEnabled=false
                btnFechaCompra.isEnabled=false
                btnselectProduct.isEnabled=false
                adapterProductoCompra.editableList(false)
            }
            override fun ErrorTransferencia() {
                controladorProcesoCargar.FinalizarDialogCarga()
                menuSaveItem.setVisible(false)
                MensajeAdvertencia("Error en la transferencia.Verifique su conexión a internet","Error")
                btnAlmacen.isEnabled=false
                btnAlmacenDestino.isEnabled=false
                btnScan.isEnabled=false
                btnFechaIngreso.isEnabled=false
                btnFechaGuia.isEnabled=false
                btnFechaCompra.isEnabled=false
                btnselectProduct.isEnabled=false
                adapterProductoCompra.editableList(false)
            }

            override fun ErrorConnection() {

                controladorProcesoCargar.FinalizarDialogCarga()
                menuSaveItem.setVisible(false)
                MensajeAdvertencia("Error al momento de completar a transferencia.Verifique su conexión a internet","Error")
                btnAlmacen.isEnabled=false
                btnAlmacenDestino.isEnabled=false
                btnScan.isEnabled=false
                btnFechaIngreso.isEnabled=false
                btnFechaGuia.isEnabled=false
                btnFechaCompra.isEnabled=false
                btnselectProduct.isEnabled=false
                adapterProductoCompra.editableList(false)
            }

            override fun AnotherError() {

                controladorProcesoCargar.FinalizarDialogCarga()
                menuSaveItem.setVisible(false)
                MensajeAdvertencia("Error en la transferencia.Reinicie la aplicación","Error")
                btnAlmacen.isEnabled=false
                btnAlmacenDestino.isEnabled=false
                btnScan.isEnabled=false
                btnFechaIngreso.isEnabled=false
                btnFechaGuia.isEnabled=false
                btnFechaCompra.isEnabled=false
                btnselectProduct.isEnabled=false
                adapterProductoCompra.editableList(false)

            }

        })
    }
    fun onSingleSectionWithIconsClicked(view: View) {
        val popupMenu = popupMenu {
            section {

                title="Opciones"
                item {
                    label = "Procesar movimiento"
                    icon = R.drawable.check //optional
                    callback = { //optional
                        ProcesarGuardar()
                    }
                }
                item {
                    label = "Guardar sin procesar"
                    iconDrawable = ContextCompat.getDrawable(this@IngresoCompraKt, R.drawable.abc_ic_menu_paste_mtrl_am_alpha) //optional
                    callback = { //optional
                        if(idAlmacenOrigen!=0) {
                            GuardarSinProcesar()
                        }else{
                            MensajeAdvertencia(titulo="Advertencia",mensaje ="Debe seleccionar un almacén" )
                        }
                    }
                }
                item {
                    label = "Cancelar"
                    icon = R.drawable.abc_ic_menu_selectall_mtrl_alpha //optional
               }
            }
        }

        popupMenu.show(this@IngresoCompraKt, view)
    }


}
