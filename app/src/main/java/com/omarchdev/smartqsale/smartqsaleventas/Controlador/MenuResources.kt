package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import android.content.Context
import android.view.View
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.github.zawadz88.materialpopupmenu.popupMenu

class MenuResources(context: Context){

    val context=context
    private lateinit var listenerAccionRegistro: ListenerAccionRegistro
    private lateinit var listenerListadoOpciones: ListenerListadoOpciones
    var listenerSubCategoria:ListenerSubCategoria?=null

    fun setListernerAccionRegistro(listenerAccionRegistro: ListenerAccionRegistro){
        this.listenerAccionRegistro=listenerAccionRegistro
    }

    public interface  ListenerAccionRegistro{
        fun AccionGuardar()
    }

    public fun setListenerListadoOpciones (listenerListadoOpciones: ListenerListadoOpciones){
        this.listenerListadoOpciones=listenerListadoOpciones
    }

    public interface ListenerListadoOpciones{
        fun AccionEditar()
        fun AccionAnular()
        fun AccionVisualizar()
        fun AgregarSubCategoria()

    }

    public interface ListenerSubCategoria{
        fun AgregarSubCategoria()
    }

    fun OpcionesListadoCategoria(view:View){
        val popupMenu = popupMenu {
            section {
                title = "Opciones"
                item {
                    label = "Editar"
                    icon = R.drawable.pencil_edit //optional
                    callback = {
                        //optional
                        if(listenerListadoOpciones!=null) {
                            listenerListadoOpciones.AccionEditar()
                        }
                    }
                }
                item {
                    label = "Visualizar"
                    icon = R.drawable.eye //optional
                    callback = {
                        //optional
                        if(listenerListadoOpciones!=null) {
                            listenerListadoOpciones.AccionVisualizar()
                        }
                    }
                }
                item{
                    label="Agregar subCategorias"
                    icon=R.drawable.ic_plus_grey600_36dp
                    callback={
                        listenerListadoOpciones?.AgregarSubCategoria()
                    }
                }
                item{

                    label="Eliminar"
                    icon=R.drawable.trash
                    callback={
                        listenerListadoOpciones?.AccionAnular()
                    }

                }
            }
        }
        popupMenu.show(context, view)
    }

    fun OpcionesListado(view:View){
        val popupMenu = popupMenu {
            section {
                title = "Opciones"
                item {
                    label = "Editar"
                    icon = R.drawable.pencil_edit //optional
                    callback = {
                        //optional
                        if(listenerListadoOpciones!=null) {
                            listenerListadoOpciones.AccionEditar()
                        }
                    }
                }
                item {
                    label = "Visualizar"
                    icon = R.drawable.eye //optional
                    callback = {
                        //optional
                        if(listenerListadoOpciones!=null) {
                            listenerListadoOpciones.AccionVisualizar()
                        }
                    }
                }
                item{

                    label="Eliminar"
                    icon=R.drawable.trash
                    callback={
                        listenerListadoOpciones?.AccionAnular()
                    }
                }
            }
        }
        popupMenu.show(context, view)
    }

    fun EditarMenu(view:View){
        val popupMenu = popupMenu {
            section {
                title = "Opciones"
                item {
                    label = "Editar"
                    icon = R.drawable.check //optional
                    callback = {
                        //optional
                        if(listenerAccionRegistro!=null) {
                            listenerAccionRegistro.AccionGuardar()
                        }
                    }
                }

            }
        }
        popupMenu.show(context, view)
    }

    fun RegistroMenu(view: View) {
        val popupMenu = popupMenu {
            section {
                title = "Opciones"
                item {
                    label = "Guardar"
                    icon = R.drawable.check //optional
                    callback = {
                        //optional
                        if(listenerAccionRegistro!=null) {
                            listenerAccionRegistro.AccionGuardar()
                        }
                    }
                }

            }
        }
        popupMenu.show(context, view)
    }


}