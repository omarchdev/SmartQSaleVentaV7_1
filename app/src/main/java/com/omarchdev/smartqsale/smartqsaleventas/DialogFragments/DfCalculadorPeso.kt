package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.*
import com.omarchdev.smartqsale.smartqsaleventas.ConsultaHttp.bg
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.df_control_peso.*
import java.math.BigDecimal

class DfCalculadorPeso():DialogFragment(), View.OnClickListener {

    private var peso=0.bg
    internal var empezoEscribir: Boolean = false
    private var p= mProduct()
    private var obtenerPeso:ObtenerPeso?=null
    interface ObtenerPeso{
        fun PesoIngresadoProducto(peso:BigDecimal,product:mProduct)
    }
    fun newInstance(p:mProduct,obtenerPeso:ObtenerPeso):DfCalculadorPeso{

        var f=DfCalculadorPeso()
        f.p=p
        f.obtenerPeso=obtenerPeso
        return f
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.df_control_peso, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnNumber0.setOnClickListener(this)
        btnNumber1.setOnClickListener(this)
        btnNumber2.setOnClickListener(this)
        btnNumber3.setOnClickListener(this)
        btnNumber4.setOnClickListener(this)
        btnNumber5.setOnClickListener(this)
        btnNumber6.setOnClickListener(this)
        btnNumber7.setOnClickListener(this)
        btnNumber8.setOnClickListener(this)
        btnNumber9.setOnClickListener(this)
        btnNumberDelete.setOnClickListener(this)
        btnNumberCancel.setOnClickListener(this)
        btnCancelar.setOnClickListener(this)
        btnGuardar.setOnClickListener(this)
        peso=0.bg
        empezoEscribir=false
        EnviarDatoTexto()
        txtTipodePago.text="Cantidad de\n"+p.getcProductName()

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog=super.onCreateDialog(savedInstanceState)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    fun imp(a:Int,b:String,c:Int){
       var cb = "$b "
    }

    private fun ModificarValorPago(valorIngresado: BigDecimal) {
        if (empezoEscribir == true) {
            peso = BigDecimal(0.00).setScale(2, BigDecimal.ROUND_FLOOR)
            EnviarDatoTexto()
            empezoEscribir = false
        }
        var a="String"
        peso = (peso.multiply(BigDecimal.valueOf(10)).
                add(valorIngresado.divide(BigDecimal(100)))).setScale(2, BigDecimal.ROUND_FLOOR)
        EnviarDatoTexto()


    }
    private fun EliminarCifras() {

        var nuevaCadena = ""
        nuevaCadena = txtValorOriginal.getText().toString()
        peso = BigDecimal(nuevaCadena).setScale(2, BigDecimal.ROUND_FLOOR).divide(BigDecimal(10))


        EnviarDatoTexto()

    }

    private fun EnviarDatoTexto() {

        txtValorOriginal.setText(String.format("%.2f", peso.setScale(2, BigDecimal.ROUND_FLOOR)))
        if(String.format("%.2f", peso.setScale(2, BigDecimal.ROUND_FLOOR)).equals("0.00")){
            peso=0.bg
        }
    }


    private fun VaciarCantidadPago() {
        peso = BigDecimal(0)
        txtValorOriginal.setText(String.format("%.2f", peso.setScale(2, BigDecimal.ROUND_FLOOR)))

    }
    override fun onClick(v: View?) {
        when(v?.id){

            R.id.btnNumber0->{
                ModificarValorPago(BigDecimal(0))
            }
            R.id.btnNumber1->{
                ModificarValorPago(BigDecimal(1))
            }
            R.id.btnNumber2->{
                ModificarValorPago(BigDecimal(2))}
            R.id.btnNumber3->{
                ModificarValorPago(BigDecimal(3))}
            R.id.btnNumber4->{
                ModificarValorPago(BigDecimal(4))}
            R.id.btnNumber5->{
                ModificarValorPago(BigDecimal(5))}
            R.id.btnNumber6->{
                ModificarValorPago(BigDecimal(6))}
            R.id.btnNumber7->{
                ModificarValorPago(BigDecimal(7))}
            R.id.btnNumber8->{
                ModificarValorPago(BigDecimal(8))}
            R.id.btnNumber9->{
                ModificarValorPago(BigDecimal(9))}
            R.id.btnNumberDelete->{
                EliminarCifras()
                EnviarDatoTexto()
            }
            R.id.btnNumberCancel->{
                VaciarCantidadPago()
            }
            R.id.btnCancelar->{
                dialog?.dismiss()
            }
            R.id.btnGuardar->{
                //GuardarMetodoPagoConCantidad()
                if(peso.compareTo(BigDecimal(0))==1) {
                    obtenerPeso?.PesoIngresadoProducto(peso, p)
                    dialog?.dismiss()
                }

            }

        }
    }

}

private val Double.bg: BigDecimal
    get() {

        return BigDecimal(this)
    }
