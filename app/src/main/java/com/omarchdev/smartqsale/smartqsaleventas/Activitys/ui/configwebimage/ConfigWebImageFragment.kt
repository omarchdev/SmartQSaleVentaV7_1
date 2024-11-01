package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configwebimage


import android.app.Activity
import androidx.lifecycle.ViewModelProviders

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.config_web_image_fragment.*
import java.io.FileNotFoundException

class ConfigWebImageFragment : Fragment(), View.OnClickListener {
    val SELECT_PICTURE = 200

    var bmpImg:Bitmap?=null
    companion object {
        fun newInstance() = ConfigWebImageFragment()
    }

    private lateinit var viewModel: ConfigWebImageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_config_categoria, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.ic_guardar) {
            viewModel.UpdateImage()
        }
        return false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.config_web_image_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title ="Logo en web"
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)


        viewModel = ViewModelProviders.of(this).get(ConfigWebImageViewModel::class.java)
        viewModel.imgLog.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                imgLogo.setImageBitmap(it!!)
            }
        })
        val cargaAsyncInit=DialogCargaAsync(requireContext())
        val dialogInit=cargaAsyncInit.getDialogCarga("Espere un momento")
        viewModel.loadingProcessInit.observe(viewLifecycleOwner, Observer {
            if(it!!.loading){
                dialogInit.show()
            }else{
                dialogInit.hide()
            }
        })
        val cargaAsyncGuardar=DialogCargaAsync(requireContext())
        val dialogGuardar=cargaAsyncGuardar.getDialogCarga("Guardando logo")
        viewModel.loadingProcessSave.observe(viewLifecycleOwner, Observer {
            if(it!!.loading){
                dialogGuardar.show()
            }else{
                dialogGuardar.hide()

            }

            if(it!!.terminate){
                var message=""
                var title=""
                message=it!!.message

                if(it!!.resultOk){
                     title="Confirmación"
                }else{
                    title="Advertencia"
                }
                AlertDialog.Builder(requireContext()).setTitle(title).setMessage(message)
                        .setPositiveButton("Aceptar") { dialog, which ->
                            if(it!!.resultOk){
                                requireActivity().finish()
                            }
                        }.create().show()
            }
        })
        btnSelectImg.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            try {
                val imageStream = requireActivity().contentResolver.openInputStream(uri!!)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                val imageBitmap: Bitmap = selectedImage
                imgLogo.setImageBitmap(imageBitmap)
                bmpImg = imageBitmap
                viewModel.SetImage(bmpImg!!)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnSelectImg->{

                val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                i.type = "image/*"
                startActivityForResult(Intent.createChooser(i, "Selecciona una imagen"), SELECT_PICTURE)
            }
        }
    }

}
