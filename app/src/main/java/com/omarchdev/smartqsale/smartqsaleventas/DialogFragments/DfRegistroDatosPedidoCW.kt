package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncPedidos
import com.omarchdev.smartqsale.smartqsaleventas.ClickListener
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterOperarioPedido
import kotlinx.android.synthetic.main.df_registro_datos_pedido_cw.*
import java.text.SimpleDateFormat
import java.util.*


class DfRegistroDatosPedidoCW():DialogFragment(), AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        }
    private val listadoModelos=ArrayList<String>()
    private val listaModelos=ArrayList<cModelo>()
    private lateinit var arrayAdapterModelo:ArrayAdapter<String>
    private var permitir=true
    private var idCabeceraPedido:Int=0
    private var asyncPedidos:AsyncPedidos?=null
    private var fechaI=""
    private var fechaF=""
    private val operarios1=ArrayList<mOperario>()
    private val adapterOperarioPedido= RvAdapterOperarioPedido(operarios1)
    private val listaMarcas=ArrayList<mMarca>()
    private var idZona=0
    private var listadoMarcas=ArrayList<String>()
    lateinit var  arrayAdapter:ArrayAdapter<String>
    fun newInstance(idCabeceraPedido: Int):DfRegistroDatosPedidoCW{
        var f=DfRegistroDatosPedidoCW()
        f.idCabeceraPedido=idCabeceraPedido
        return f
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.df_registro_datos_pedido_cw, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pb.visibility=View.VISIBLE
        content.visibility = View.INVISIBLE
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, listadoMarcas)
        spnMarca.adapter = arrayAdapter
        arrayAdapterModelo = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, listadoModelos)
        spnModelos.adapter = arrayAdapterModelo
        rvOperariosPedido.layoutManager = LinearLayoutManager(context)
        rvOperariosPedido.adapter = adapterOperarioPedido


        adapterOperarioPedido.clickListener = object : ClickListener {
            override fun clickPositionOption(position: Int, accion: Byte) {
                operarios1.removeAt(position)
                adapterOperarioPedido.notifyDataSetChanged()
            }
        }
        asyncPedidos = AsyncPedidos(requireContext())
        asyncPedidos?.ObtenerDatosPedidoCW(idCabeceraPedido,
                object : AsyncPedidos.ListenerObtenerDatosPedidoCW {
                    override fun DatosPedidoCW(listaMarca: ArrayList<mMarca>,
                                               cabeceraPedido: mCabeceraPedido,
                                               listaOperarios: ArrayList<mOperario>, listaModelo: ArrayList<cModelo>) {
                        idZona = cabeceraPedido.zonaServicio.idZona
                        pb.visibility = View.INVISIBLE
                        content.visibility = View.VISIBLE
                        edtObservacion.editText!!.setText(cabeceraPedido.observacionPedido)
                        edtDescripcionZona.editText?.setText(cabeceraPedido.zonaServicio.descripcion)
                        edtColor.editText?.setText(cabeceraPedido.zonaServicio.color)
                        edtNumeroEspacios.editText?.setText(cabeceraPedido.zonaServicio.numEspacios.toString())
                        operarios1.clear()
                        if (cabeceraPedido.zonaServicio.idZona == 0) {
                            spnMarca.isEnabled = false
                        }
                        operarios1.addAll(listaOperarios)
                        txtFInicial.setText("Hora de entrada \n" + cabeceraPedido.fechaInicial)
                        txtFFinal.setText("Hora de salida    \n" + cabeceraPedido.fechaFinal)
                        adapterOperarioPedido.notifyDataSetChanged()
                        if (!cabeceraPedido.horaInicio.equals("NN")) {

                            edtHInicio.setText(cabeceraPedido.horaInicio)
                        }
                        if (!cabeceraPedido.horaFinal.equals("NN")) {
                            edtHFinal.setText(cabeceraPedido.horaFinal)
                        }
                        listaMarcas.clear()
                        if (listaModelo.size > 0) {
                            listaModelos.addAll(listaModelo)
                            listaModelos.forEach {
                                listadoModelos.add(it.cDescripcion)
                            }
                        }
                        spnModelos.setSelection(0)
                        arrayAdapterModelo.notifyDataSetChanged()
                        if (listaMarca.size > 0) {
                            listaMarcas.addAll(listaMarca)
                            listaMarcas.forEach {
                                listadoMarcas.add(it.Descripcion)
                            }
                            spnMarca.setSelection(0)
                            spnMarca.setSelection(0)
                            spnMarca.setOnItemSelectedListener(this@DfRegistroDatosPedidoCW)
                            arrayAdapter.notifyDataSetChanged()
                            listaMarcas.forEachIndexed { i, it ->
                                if (it.idMarca == cabeceraPedido.zonaServicio.idMarca) {
                                    spnMarca.setSelection(i)
                                }
                            }
                        }
                        listaModelos.forEachIndexed { i, it ->
                            if (cabeceraPedido.zonaServicio.idModelo == it.idModelo) {
                                spnModelos.setSelection(i)
                            }
                        }
                    }
                })
        btnAddOperario.setOnClickListener {
            DfSelectOperarios().newInstance(operarios1, object : DfSelectOperarios.ListenerOperariosSeleccionados {
                override fun OperariosSelect(operarios: ArrayList<mOperario>) {
                    operarios1.clear()
                    if (operarios.size > 0) {
                        operarios1.addAll(operarios)
                    }
                    adapterOperarioPedido.notifyDataSetChanged()
                }
            }).show(requireFragmentManager(), "")
        }
        btnClock1.setOnClickListener {
           asyncPedidos!!.ObtenerHoraActual(
                   object : AsyncPedidos.ListenerHoraActual {
                       override fun HoraActualEncontrada(time: Tiempo) {
                           //   TimePickerDialogFragment().show(childFragmentManager, "dialog_select1")
                           var onStartTimeListener1 = OnTimeSetListener { view, hourOfDay, minute ->
                               var AM_PM: String
                               var am_pm: Int
                               val _24HourTime = "$hourOfDay:$minute "
                               val _24HourSDF = SimpleDateFormat("HH:mm")
                               val _12HourSDF = SimpleDateFormat("hh:mm a")
                               val _24HourDt: Date = _24HourSDF.parse(_24HourTime)
                               edtHInicio.setText(_12HourSDF.format(_24HourDt))
                               txtFInicial.setText("Hora de entrada \n ${time.fecha}")

                               fechaI=time.fecha+" "+_12HourSDF.format(_24HourDt)
                           }
                           TimePickerDialog(context, 2, onStartTimeListener1, time.tiempo.hours, time.tiempo.minutes, false).show()

                       }
                   })



        }
        btnClock2.setOnClickListener {

            asyncPedidos!!.ObtenerHoraActual(object : AsyncPedidos.ListenerHoraActual {

                override fun HoraActualEncontrada(time: Tiempo) {
                    var onStartTimeListener2 = OnTimeSetListener { view, hourOfDay, minute ->
                        var AM_PM: String
                        var am_pm: Int
                        val _24HourTime = "$hourOfDay:$minute "
                        val _24HourSDF = SimpleDateFormat("HH:mm")
                        val _12HourSDF = SimpleDateFormat("hh:mm a")
                        val _24HourDt: Date = _24HourSDF.parse(_24HourTime)
                        edtHFinal.setText(_12HourSDF.format(_24HourDt))
                        txtFFinal.setText("Hora de salida \n ${time.fecha}")
                        fechaF =time.fecha+ " " + _12HourSDF.format(_24HourDt)

                    }
                    TimePickerDialog(context, 2, onStartTimeListener2, time.tiempo.hours, time.tiempo.minutes, false).show()
                    /*
                    AlertDialog.Builder(context).setTitle("Confirmacion").setMessage("¿Está séguro de colocar las ${time.hora} como hora de salida?").setPositiveButton("Aceptar", { dialog, which ->
                        fechaF = time.fecha + " " + time.hora
                        edtHFinal.setText(time.hora)
                        txtFFinal.setText("Hora de salida   \n ${time.fecha}")
                    })
                            .setNegativeButton("Cancelar", null).create().show()

                    */
                }
            })



        }
    }

    var onStartTimeListener = OnTimeSetListener { view, hourOfDay, minute ->
        var AM_PM: String
        var am_pm: Int
        val _24HourTime = "$hourOfDay:$minute "
        val _24HourSDF = SimpleDateFormat("HH:mm")
        val _12HourSDF = SimpleDateFormat("hh:mm a")
        val _24HourDt: Date = _24HourSDF.parse(_24HourTime)
        Toast.makeText(context, _12HourSDF.format(_24HourDt), Toast.LENGTH_SHORT).show()

    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }


    fun VerificarDatosGuardar():Boolean{

        return permitir
    }

    fun GuardarDatosDetallePedido():mCabeceraPedido{

        var cabeceraPedido=mCabeceraPedido()
        cabeceraPedido.idCabecera=idCabeceraPedido
        cabeceraPedido.zonaServicio= mZonaServicio()
        cabeceraPedido.zonaServicio.idZona=idZona
        cabeceraPedido.horaInicio=fechaI
        cabeceraPedido.horaFinal=fechaF

        if(listaMarcas.size>0){

            cabeceraPedido.zonaServicio.idMarca=listaMarcas.get(spnMarca.selectedItemPosition).idMarca
        }else{
            cabeceraPedido.zonaServicio.idMarca=0
        }

        cabeceraPedido.zonaServicio.color=edtColor.editText!!.text.toString()
        cabeceraPedido.zonaServicio.idModelo=listaModelos.get(spnModelos.selectedItemPosition).idModelo
        cabeceraPedido.operarios=operarios1
        cabeceraPedido.observacionPedido=edtObservacion.editText!!.text.toString()

        val controladorProcesoCargar=ControladorProcesoCargar(context)
        controladorProcesoCargar.IniciarDialogCarga("Guardando datos")
        asyncPedidos?.GuardarDetallesPedidoCW(cabeceraPedido, object : AsyncPedidos.ListenerGuardadoDetallePedido {
            override fun ExitoGuardar() {
                AlertDialog.Builder(context).setMessage("Se guardó los datós con éxito")
                        .setPositiveButton("Aceptar", null).create().show()
                if (fechaI.length > 0) {
                    btnClock1.visibility = View.GONE
                }
                if (fechaF.length > 0) {
                    btnClock2.visibility = View.GONE
                }
                controladorProcesoCargar.FinalizarDialogCarga()
            }

            override fun ErrorGuardar() {
                controladorProcesoCargar.FinalizarDialogCarga()
                AlertDialog.Builder(context).setMessage("Existé un problema al guardar la información.Verifique su conexión a intenet").setPositiveButton("Aceptar", null).create().show()

            }

        })
        return cabeceraPedido
    }



}