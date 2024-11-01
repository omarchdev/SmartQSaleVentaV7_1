package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.*
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.fortMoneda
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.ConsultaHttp.bg
import com.omarchdev.smartqsale.smartqsaleventas.Control.MethodsNumber.ReplaceCommaToDot
import com.omarchdev.smartqsale.smartqsaleventas.Control.NumberTextWatcher
import com.omarchdev.smartqsale.smartqsaleventas.Control.NumberTextWatcher.INumberTextWatcher
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ClickEditText
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ClickTextInputLayout
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.dialog_edit_price.*
import java.math.BigDecimal

class DfEditProductCarSale():DialogFragment(), View.OnClickListener{

    private var cantidadEnVenta=0f
    private var productoEnVenta:ProductoEnVenta?=null
    private val montoDescuento=BigDecimal(0)
    private var montoTotal=BigDecimal(0)
    private var mQ=0.bg
    private var mD=0.bg
    private var porcentajeDescuento=0.bg
    private var precioVentaUnitario=0.bg
    private var listenerEdiccionProductoEnVenta:ListenerEdiccionProductoEnVenta?=null
    var permitir=false
    interface ListenerEdiccionProductoEnVenta{

        fun EditarProductoEnVenta(productoEnVenta: ProductoEnVenta)

    }

    fun newInstance(productoEnVenta: ProductoEnVenta,listenerEdiccionProductoEnVenta: ListenerEdiccionProductoEnVenta?):DfEditProductCarSale{

        var dfEditProductCarSale=DfEditProductCarSale()
        dfEditProductCarSale.productoEnVenta=productoEnVenta
        dfEditProductCarSale.listenerEdiccionProductoEnVenta=listenerEdiccionProductoEnVenta
        dfEditProductCarSale.precioVentaUnitario=productoEnVenta.precioOriginal
        return dfEditProductCarSale
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_edit_price, container, false)
    }


    fun montoTotal(cantidad:BigDecimal,precioOriginal:BigDecimal,montoDescuento:BigDecimal):BigDecimal{
        return cantidad.multiply(precioOriginal).subtract(montoDescuento)
    }
    fun descripcionInfoTotal(cantidad:BigDecimal,precioOriginal:BigDecimal,montoDescuento:BigDecimal):String=
            "${Constantes.DivisaPorDefecto.SimboloDivisa}${String.format("%.2f",cantidad.multiply(precioOriginal))}-" +
                    "${String.format("%.2f",montoDescuento)}=" +
                    "${Constantes.DivisaPorDefecto.SimboloDivisa}${String.format("%.2f",montoTotal(cantidad,precioOriginal,montoDescuento))}"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try{
            btnMinusb.setOnClickListener(this)
            btnPlus.setOnClickListener(this)
            btnGuardarCantidadProducto.setOnClickListener(this)
            btnSalirDialog.setOnClickListener(this)
            edtDescuento.visibility=View.GONE
            edtDescuento.hint="Porcentaje a descontar (%)"
            edtMontoDescuento.hint="Cantidad a descontar "+Constantes.DivisaPorDefecto.SimboloDivisa
            edtQuantityProduct.setText(String.format("%.2f",productoEnVenta?.cantidad))
            edtMontoDescuento.editText?.setText(String.format("%.2f",productoEnVenta?.montoDescuento))
            txtNombreProducto.text="${productoEnVenta?.productName}"
            txtObservacionProducto.editText?.setText(productoEnVenta?.observacion)
            if(!productoEnVenta!!.isbPrecioVariable()){
                edtPrecioUnitario?.visibility=View.GONE
            }else{
                edtPrecioUnitario?.visibility=View.VISIBLE
            }
            edtPrecioUnitario.editText?.setText(precioVentaUnitario.fortMoneda)
            mQ=productoEnVenta?.cantidad!!.toBigDecimal()
            mD=productoEnVenta?.montoDescuento!!
            /* if(!productoEnVenta?.estadoGuardado.equals("")){
                     btnPlus.isEnabled=false
                     btnMinusb.isEnabled=false
                     edtQuantityProduct.isFocusable=false
                     edtQuantityProduct.isEnabled=false
             }*/
            val watcherEdtPrecio=NumberTextWatcher( edtPrecioUnitario.editText!!)
            watcherEdtPrecio.iNumberTextWatcher=object:INumberTextWatcher{
                override fun cantidadBigDecimal(number: BigDecimal) {

                    precioVentaUnitario=number
                    txtInfoTotal.text = descripcionInfoTotal(mQ,
                        precioVentaUnitario,
                        mD)
                }
            }
            edtPrecioUnitario.editText?.addTextChangedListener(watcherEdtPrecio)
            ClickTextInputLayout(edtPrecioUnitario)

            /*  edtPrecioUnitario.editText?.addTextChangedListener(object:TextWatcher{
                  override fun afterTextChanged(s: Editable?) {
                  }
                  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                  }
                  override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                      if(s.toString().replace(",",".").equals(".")){
                          precioVentaUnitario=0.bg
                      }else if(s.toString().replace(",",".").equals("")){
                          precioVentaUnitario=0.bg
                      }else{
                          precioVentaUnitario=s.toString().replace(",",".").bgS
                      }
                      txtInfoTotal.text = descripcionInfoTotal(mQ,
                              precioVentaUnitario,
                              mD)
                  }
              })*/
            cbDescuento.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    edtMontoDescuento.isEnabled=true
                }else{
                    edtMontoDescuento.isEnabled=false
                    edtMontoDescuento.editText?.setText("0.00")

                }
            }
            edtMontoDescuento.isEnabled=productoEnVenta!!.isUsaDescuento
            edtDescuento.isEnabled=productoEnVenta!!.isUsaDescuento
            cbDescuento.isChecked=productoEnVenta!!.isUsaDescuento
           txtInfoTotal.text = descripcionInfoTotal(productoEnVenta!! .cantidad.toBigDecimal(),
                productoEnVenta!!.precioOriginal,
               productoEnVenta!!.montoDescuento)
            ListenerEdt()

            cbDescuento.isChecked=productoEnVenta!!.isUsaDescuento
            edtMontoDescuento.isEnabled=productoEnVenta!!.isUsaDescuento

        }catch (ex:Exception){
            ex.toString()
        }

    }

    private fun ListenerEdt(){
        val listenerCantidad=NumberTextWatcher(edtQuantityProduct)
        listenerCantidad.iNumberTextWatcher=object:INumberTextWatcher{
            override fun cantidadBigDecimal(number: BigDecimal) {
                mQ=number
                txtInfoTotal.text = descripcionInfoTotal(mQ,
                        precioVentaUnitario,
                        mD)
            }

        }
        ClickEditText(edtQuantityProduct)
        edtQuantityProduct.addTextChangedListener(listenerCantidad)
       /* edtQuantityProduct.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                if(s.toString().equals("")){
                    mQ=0.bg
                }else if(s.toString().equals(".")){
                    mQ=0.bg
                }
                else{
                    mQ=s.toString().toBigDecimal()
                }
                txtInfoTotal.text = descripcionInfoTotal(mQ,
                        precioVentaUnitario,
                        mD)
              /*  edtDescuento.editText?.setText(ObtenerDescuentoPorcentaje
                (mD,montoTotal(mQ,
                        precioVentaUnitario, mD)))*/
            }
        })*/
        val listenerDescuento=NumberTextWatcher(edtMontoDescuento.editText!!)
        listenerDescuento.iNumberTextWatcher=object:INumberTextWatcher{
            override fun cantidadBigDecimal(number: BigDecimal) {
                mD=number
                txtInfoTotal.text = descripcionInfoTotal(mQ,
                        precioVentaUnitario, mD)
            }

        }
        edtMontoDescuento.editText?.addTextChangedListener(listenerDescuento)
        /*edtMontoDescuento.editText?.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //    edtDescuento.editText?.addTextChangedListener(null)
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s
                if(s.toString().equals("")){
                    mD=0.bg
                }else if(s.toString().replace(",",".").equals(".")){
                    mD=0.bg
                }else{
                    mD=s.toString().replace(",",".").toBigDecimal()
                }



                txtInfoTotal.text = descripcionInfoTotal(mQ,
                        precioVentaUnitario, mD)

               /* edtDescuento.editText?.setText(ObtenerDescuentoPorcentaje
                (mD,montoTotal(mQ,
                        precioVentaUnitario, mD)))*/
            }

        })*/
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog=super.onCreateDialog(savedInstanceState)

        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return super.onCreateDialog(savedInstanceState)
    }

    fun ObtenerDescuentoPorcentaje(monto:BigDecimal,montoTotal:BigDecimal):String{
            return ((monto.multiply(100.bg)).divide(montoTotal)).fortMoneda
    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.btnMinusb->{
                if(edtQuantityProduct.text.toString().toFloat()-1>0) {
                    cantidadEnVenta = ReplaceCommaToDot(edtQuantityProduct.text.toString()).toFloat()
                    cantidadEnVenta--
                    edtQuantityProduct.setText(String.format("%.2f",cantidadEnVenta))
                }
            }
            R.id.btnPlus->{
                cantidadEnVenta = ReplaceCommaToDot(edtQuantityProduct.text.toString()).toFloat()
                cantidadEnVenta++
                edtQuantityProduct.setText(String.format("%.2f",cantidadEnVenta))

            }
            R.id.btnGuardarCantidadProducto->{

                montoTotal=precioVentaUnitario.multiply(ReplaceCommaToDot(edtQuantityProduct.text.toString()).toBigDecimal())

                if(montoTotal.compareTo(ReplaceCommaToDot(edtMontoDescuento.editText?.text.toString()).toBigDecimal())==1 && montoTotal.compareTo(0.bg)==1){

                    var p=ProductoEnVenta()
                    try {
                        p.precioOriginal=precioVentaUnitario
                        p.idDetallePedido = productoEnVenta!!.idDetallePedido
                        p.montoDescuento = ReplaceCommaToDot(edtMontoDescuento.editText?.text.toString()).toBigDecimal()
                        p.cantidad = ReplaceCommaToDot(edtQuantityProduct.text.toString()).toFloat()
                        p.observacion = txtObservacionProducto.editText?.text.toString()
                        p.isUsaDescuento=cbDescuento.isChecked
                    }catch (e:Exception){
                        e.toString()
                    }
                    this.dismiss()
                    listenerEdiccionProductoEnVenta?.EditarProductoEnVenta(p)
                }

            }
            R.id.btnSalirDialog->{
                 this.dismiss()
            }

        }

    }


}