package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProductKt
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.ListaPrecioVenta
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoListaPrecioSeleccion
import com.omarchdev.smartqsale.smartqsaleventas.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DialogSeleccionListaPrecioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */




class DialogSeleccionListaPrecioFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    interface IDialogSeleccionListaPrecioPedido{
        fun SeleccionarListaPrecio(productoListaPrecioSeleccion: ProductoListaPrecioSeleccion)
    }
     var iDialogSeleccionListaPrecioPedido:IDialogSeleccionListaPrecioPedido?=null
    private var idProduct: Int? = null
    private var param2: String? = null
    private var rb_venta : RadioButton?=null
    private var rb_compra : RadioButton?=null
    private var rb_consumo : RadioButton?=null
    private var btnSalirLista :Button?=null
    private var btnConfirmarLista :Button?=null
    private var txtTituloDialogLista: TextView?=null
    private var btnPlusCantidad:Button?=null
    private var txtCantidadPedido: EditText?=null
    private var btnMinusCantidad:Button?=null
    private var asyncProducto: AsyncProductKt?=null
    private var listaPrecioSeleccionada: ListaPrecioVenta? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idProduct = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view=inflater.inflate(R.layout.fragment_dialog_seleccion_lista_precio, container, false)

        return view    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, // Ancho: Ocupa casi todo
            ViewGroup.LayoutParams.WRAP_CONTENT,  // Alto: Se ajusta al contenido
        )
    }

    fun salir(){
        dismiss()
    }


    fun aumentarCantidad(){
        var cantidad=txtCantidadPedido?.text.toString().toInt()

        if(cantidad==100){
            return
        }
        cantidad++

        txtCantidadPedido?.setText(cantidad.toString())
    }

    fun disminuirCantidad(){
        var cantidad=txtCantidadPedido?.text.toString().toInt()
        if(cantidad==1){
            return
        }
        cantidad--
        txtCantidadPedido?.setText(cantidad.toString())
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rb_venta = view.findViewById<View?>(R.id.rb_venta) as RadioButton
        rb_compra = view.findViewById<View?>(R.id.rb_compra) as RadioButton
        rb_consumo = view.findViewById<View?>(R.id.rb_consumo) as RadioButton
        btnSalirLista = view.findViewById<View?>(R.id.btnSalirLista) as Button
        btnConfirmarLista = view.findViewById<View?>(R.id.btnConfirmarLista) as Button
        txtTituloDialogLista= view.findViewById<View?>(R.id.txtTituloDialogLista) as TextView
        txtCantidadPedido=view.findViewById<View?>(R.id.txtCantidadPedido) as EditText
        btnMinusCantidad=view.findViewById<View?>(R.id.btnMinusCantidad) as Button
        btnPlusCantidad=view.findViewById<View?>(R.id.btnPlusCantidad) as Button
        btnSalirLista?.setOnClickListener {
            salir()
        }
        btnConfirmarLista?.setOnClickListener {
            capturarDatos()
        }

        btnPlusCantidad?.setOnClickListener {
            aumentarCantidad()
        }

        btnMinusCantidad?.setOnClickListener {
            disminuirCantidad()
        }

        txtCantidadPedido?.setText("1")

        asyncProducto= AsyncProductKt()
        asyncProducto!!.iListaPreciosVentaConsulta=object :AsyncProductKt.IListaPreciosVentaConsulta{
            override fun ResultListasPreciosVenta(listasPrecios: List<ListaPrecioVenta>) {

                if(listasPrecios.size==0){
                    salir()
                    Toast.makeText(context,"No se encontro al producto en listas de precios",Toast.LENGTH_LONG).show()
                    return
                }

                var lista=listasPrecios[0]
                listaPrecioSeleccionada = lista
                rb_venta?.text=lista.descripcionVenta +" - "+ Constantes.SimboloMoneda.moneda + lista.npreci_unitario
                rb_compra?.text=lista.descripcionCompra +" - "+ Constantes.SimboloMoneda.moneda + lista.nprecio_unitario_compra
                rb_consumo?.text=lista.descripcionConsumo+ " - "+ Constantes.SimboloMoneda.moneda + lista.nprecio_unitario_consumo
                rb_venta?.isChecked=true;
            }
        }
        txtTituloDialogLista?.text="Lista de precios"+"\n"+param2
        asyncProducto?.GetListasPreciosVenta(idProduct!!)
        // Aquí configuras los clics de botones de tu XML
    }


    fun capturarDatos(){
        var tipoUnidad=""
        var precio = 0.toBigDecimal()

        if (listaPrecioSeleccionada == null) return

        if(rb_venta!!.isChecked){
            tipoUnidad="V"
            precio = listaPrecioSeleccionada!!.npreci_unitario
        }else if(rb_compra!!.isChecked){
            tipoUnidad="C"
            precio = listaPrecioSeleccionada!!.nprecio_unitario_compra
        }else if(rb_consumo!!.isChecked){
            tipoUnidad="U"
            precio = listaPrecioSeleccionada!!.nprecio_unitario_consumo
        }

        if (precio.compareTo(0.toBigDecimal()) == 0) {
            AlertDialog.Builder(requireContext())
                .setTitle("Atención")
                .setMessage("No puede seleccionar un precio igual a cero")
                .setPositiveButton("Aceptar", null)
                .show()
            return
        }


        var dataProducto= ProductoListaPrecioSeleccion(
            idProduct!!,
            tipoUnidad,
            txtCantidadPedido?.text.toString().toBigDecimal(),
            "UND",
            0,
            0
        )

        iDialogSeleccionListaPrecioPedido?.SeleccionarListaPrecio(
            dataProducto
        )
        dismiss()
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DialogSeleccionListaPrecioFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            DialogSeleccionListaPrecioFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}