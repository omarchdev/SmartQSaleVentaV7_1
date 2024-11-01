package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments


import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.omarchdev.smartqsale.smartqsaleventas.API.ApiConsultaDocumento
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncClientes
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncTiposDocumento
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.ConsultaHttp.HttpConsultas
import com.omarchdev.smartqsale.smartqsaleventas.Interface.ListenerResultadosGeneric
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer
import com.omarchdev.smartqsale.smartqsaleventas.Model.mTipoDocumento
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.layout_registro_cliente.*

class DFmRegistroCliente(): DialogFragment(), ListenerResultadosGeneric<ArrayList<mTipoDocumento>>, AdapterView.OnItemSelectedListener, AsyncClientes.ObtenerDatoCliente, AsyncClientes.RegistroClientes, View.OnClickListener, HttpConsultas.ListenerResultadoBusquedaCliente, TextWatcher{


    override fun ClienteNoHabilitado(nombre:String) {
        AlertDialog.Builder(requireContext()).
                setPositiveButton("Salir",null).
                setMessage("$nombre no habilitado").setTitle("Advertencia").create().show()
    }

    override fun ExisteCliente() {
        AlertDialog.Builder(requireContext()).
                setPositiveButton("Salir",null).
                setMessage("Existe un cliente con el numero de documento ${edtNumerDocumento.editText?.text.toString().trim()} .").setTitle("Advertencia").create().show()
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(s?.length==minLength){
            edtNumerDocumento.error=null
        }
        if(verificarRuc){
            if(seUsaRuc){
                edtDireccionCliente.editText?.setText("")
                edtDenominacionCliente.editText?.setText("")
            }
        }
    }
    val apiConsultaDocumento=ApiConsultaDocumento()
    var seUsaRuc=false
    var consultaHttp=HttpConsultas()
    var listenerNombreCliente:ListenerNombreCliente?=null
    private var esDialog=false
    val asyncTiposDocumento=AsyncTiposDocumento()
    var descripcionesDocumentos=ArrayList<String>()
    var listaDocumento:ArrayList<mTipoDocumento>?=null
    var listenerAddEditCustomer:DialogAddEditCustomer.ListenerAddCustomer?=null
    val cliente=mCustomer()
    var verificarDireccion=false
    var verificarNumDocumento=true
    var verificarDenominacion=true
    internal var asyncClientes= AsyncClientes()
    var idCliente:Int=0
    var permitir=false
    var minLength=8
    var tipoDoc=1
    var verificarRuc=false
    interface ListenerNombreCliente{
        fun ObtenerNombreCliente(nombre:String)
    }

    override fun onStart() {

        super.onStart()
        if (dialog != null) {
            dialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    fun newInstance(idCliente:Int):DFmRegistroCliente{
        val f=DFmRegistroCliente()
        var arg=Bundle()
        arg.putInt("idCliente",idCliente)
        f.arguments=arg
        return f
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtTitulo.visibility=View.INVISIBLE
        btnPositive.visibility=View.INVISIBLE
        btnNegative.visibility=View.INVISIBLE
        txtMensaje.visibility=View.INVISIBLE
        content.visibility=View.INVISIBLE
        asyncTiposDocumento.listenerBusquedaTipoDoc=this
        asyncClientes.setRegistroClientes(this)
        idCliente=requireArguments().getInt("idCliente")
        if(esDialog){
            txtTitulo.text=""
            btnPositive.text="Guardar"
            btnNegative.text="Salir"
            btnPositive.visibility=View.VISIBLE
            btnNegative.visibility=View.VISIBLE
        }
        btnPositive.setOnClickListener(this)
        btnNegative.setOnClickListener(this)
        btnConsulta.setOnClickListener(this)
        ObtenerResultadosBusqueda(ArrayList(Constantes.TiposDocumentoId.listadoDocumentos))
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnPositive->{
                GuardarCliente()
            }
            R.id.btnNegative->{
                this.dismiss()
               }
            R.id.btnConsulta->{
                if(minLength==edtNumerDocumento.editText?.text?.toString()?.trim()?.length) {

                    consultaHttp.ObtenerDatosClienteNumRuc(edtNumerDocumento.editText?.text.toString())
                    consultaHttp.listenerResultadoBusquedaCliente = this
                }else{
                    edtNumerDocumento.error="Cantidad de carácteres es incorrecta"
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_registro_cliente, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        esDialog=true
        val dialog=super.onCreateDialog(savedInstanceState)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }
    override fun ObtenerResultadosBusqueda(resultado: ArrayList<mTipoDocumento>) {

        listaDocumento=resultado
        pb.visibility=View.GONE
         if(esDialog){
            txtTitulo.visibility=View.VISIBLE
            btnPositive.visibility=View.VISIBLE
            btnNegative.visibility=View.VISIBLE

         }else{
            txtTitulo.visibility=View.GONE
            btnPositive.visibility=View.GONE
            btnNegative.visibility=View.GONE
        }
        content.visibility=View.VISIBLE
        txtMensaje.visibility=View.GONE
       val adapter=ArrayAdapter(requireActivity(),android.R.layout.simple_spinner_dropdown_item,descripcionesDocumentos)
       spnTipoDocumento.adapter=adapter
        spnTipoDocumento.onItemSelectedListener=this
        edtNumerDocumento.editText?.addTextChangedListener(this)
        resultado.forEach {
            descripcionesDocumentos.add(it.cDescripcionCorta)
        }
        adapter?.notifyDataSetChanged()
        if(idCliente>0){
            asyncClientes.ObtenerClienteId(idCliente)
            asyncClientes.setObtenerDatoCliente(this)
        }else{
            listenerNombreCliente?.ObtenerNombreCliente("Agregar Cliente")
            txtTitulo.text="Agregar Cliente"
        }

    }
    override fun ErrorBusqueda() {
        pb.visibility=View.GONE
        txtMensaje.visibility=View.VISIBLE
        txtMensaje.text="Error al descargar la información solicitada.Verifique su conexion a interner"
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
    fun GuardarCliente(){
        permitir=true
        if(verificarDireccion){
            if(edtDireccionCliente.editText?.text.toString().trim().length==0){
               permitir=false
               edtDireccionCliente?.error="Este campo es obligatorio"
            }
        }
       if(minLength!=1){
            if(edtNumerDocumento.editText?.text.toString().trim().length!=minLength){
                permitir=false
                if(edtNumerDocumento?.editText?.text.toString().length==0){
                    edtNumerDocumento?.error="Este campo es obligatorio"
                }else{
                    edtNumerDocumento?.error="Cantidad de carácteres es incorrecta"
                }
            }

        }

        if(edtDenominacionCliente?.editText?.text.toString().trim().length==0){
            permitir=false
            edtDenominacionCliente?.error="Este campo es obligatorio"
        }

        cliente.setiId(idCliente)
        cliente.tipoCliente=2
        cliente.razonSocial=edtDenominacionCliente.editText?.text.toString()
        cliente.numeroRuc=edtNumerDocumento.editText?.text.toString()
        cliente.setcDireccion(edtDireccionCliente.editText?.text.toString())
        cliente.setcEmail(edtNumeroEmail.editText?.text.toString())
        cliente.setcNumberPhone(edtNumeroTelefono.editText?.text.toString())
        cliente.idTipoDocumento=listaDocumento?.get(spnTipoDocumento.selectedItemPosition)!!.idTipoDocumento
        asyncClientes.setContext(activity)
        if(permitir){
            asyncClientes.GuardarCliente(cliente)
        }


    }
    fun AgregarDatosClienteInterfaz(cliente:mCustomer)
    {
        var pos:Int=0
        listaDocumento?.forEachIndexed { index, t ->
            if(t.idTipoDocumento==cliente.idTipoDocumento){
                pos=index
            }
        }
        spnTipoDocumento.setSelection(pos)
        if(cliente.tipoCliente==1) {
            edtDenominacionCliente.editText?.setText(("${cliente.getcName().trim()}" +
                " ${cliente.getcApellidoPaterno().trim()} " +
                 "${cliente.getcApellidoMaterno().trim()}").trim())
          }else if(cliente.tipoCliente==2){
            edtDenominacionCliente.editText?.setText("${cliente.razonSocial}".trim())
          }
        edtNumerDocumento.editText?.setText("${cliente.numeroRuc}")
        edtDireccionCliente.editText?.setText("${cliente.getcDireccion()}")
        edtNumeroEmail.editText?.setText("${cliente.getcEmail()}")
        edtNumeroTelefono.editText?.setText("${cliente.getcNumberPhone()}")

        txtTitulo.text="Editar Cliente"
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        edtDenominacionCliente.hint=Constantes.
                TiposDocumentoId.listadoDocumentos.get(position).denominacionCliente
        edtNumerDocumento.hint=Constantes.TiposDocumentoId.
                listadoDocumentos.get(position).denominacionNumero
        verificarDireccion=Constantes.
                TiposDocumentoId.listadoDocumentos.get(position).bVerificarDireccion
        minLength=Constantes.TiposDocumentoId.listadoDocumentos.get(position).longitudNumeroDoc
        edtNumerDocumento.counterMaxLength= minLength
        verificarRuc=Constantes.TiposDocumentoId.listadoDocumentos.get(position).verificaRuc
        if(verificarRuc){
            btnConsulta.visibility=View.VISIBLE
            edtDenominacionCliente.isEnabled=true
            edtDireccionCliente.isEnabled=true
        }else{
            btnConsulta.visibility=View.GONE
            edtDenominacionCliente.isEnabled=true
            edtDireccionCliente.isEnabled=true
        }

        if(minLength==1){
            edtNumerDocumento.visibility=View.GONE

        }else{
            btnConsulta.visibility=View.VISIBLE
            btnConsulta.setText("Consulta "+Constantes.
                    TiposDocumentoId.listadoDocumentos.get(position).cDescripcionCorta)
        }
   /*     edtNumerDocmento.editText?.setText("")
        edtDenominacionCliente.editText?.setText("")
        edtDireccionCliente.editText?.setText("")
    */
    }

    override fun ErrorConnection() {
        Toast.makeText(activity,"Error al guardar la información.Verifique su conexión a internet",Toast.LENGTH_SHORT).show()
        if(!esDialog){
            activity?.finish()
        }else{
        }
    }

    override fun ErrorRegistro() {
        if(!esDialog){
            activity?.finish()
        }else{

        }
        Toast.makeText(activity,"Error al guardar la información.Verifique su conexión a internet",Toast.LENGTH_SHORT).show()
    }

    override fun ActualizarExito() {
        Toast.makeText(activity,"Se actualizó al cliente con éxito",Toast.LENGTH_SHORT).show()
        if(!esDialog){
            activity?.finish()
        }else{
            listenerAddEditCustomer?.actualizar()
            this.dismiss()
        }
    }

    override fun RegistrarExito() {
        Toast.makeText(activity,"Se registró al cliente con éxito",Toast.LENGTH_SHORT).show()
        if(!esDialog){
        activity?.finish()
        }else{
            listenerAddEditCustomer?.actualizar()
            this.dismiss()
        }
    }
    override fun DatosClienteResultadoSunat(cliente: mCustomer?) {
        seUsaRuc=true
        edtDireccionCliente.editText?.setText(cliente?.getcDireccion())
        edtDenominacionCliente.editText?.setText(cliente?.razonSocial)
    }
    override fun ErrorConsultaCliente(mensaje:String) {
        seUsaRuc=false
        AlertDialog.Builder(requireActivity()).setTitle("Advertencia")
                .setMessage(mensaje).setPositiveButton("Salir",null).create().show()
        edtDenominacionCliente.editText?.setText("")
        edtDireccionCliente.editText?.setText("")
        edtNumeroEmail.editText?.setText("")
        edtNumeroTelefono.editText?.setText("")

    }

    override fun ClienteObtenido(customer: mCustomer?) {
        AgregarDatosClienteInterfaz(customer!!)
        listenerNombreCliente?.ObtenerNombreCliente("Editar Cliente")
    }

}