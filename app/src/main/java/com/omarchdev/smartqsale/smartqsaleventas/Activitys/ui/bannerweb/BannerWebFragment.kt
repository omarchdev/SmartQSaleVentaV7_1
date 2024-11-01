package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.bannerweb

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync
import com.omarchdev.smartqsale.smartqsaleventas.Model.MedioImagen
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterImageBannerWeb
import kotlinx.android.synthetic.main.fragment_banner_web.*
import java.io.FileNotFoundException

class BannerWebFragment : Fragment() {
    val SELECT_PICTURE = 200

    companion object {
        fun newInstance() = BannerWebFragment()
    }
    var permiterGuardar=false
    val rvAdapterImageBannerWeb = RvAdapterImageBannerWeb()

    private lateinit var viewModel: BannerWebVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_banner_web, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_simple_guardar, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.ic_guardar) {
            if(permiterGuardar==true){
                viewModel.GuardarDatosMedios()
            }

        }
        return false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BannerWebVM::class.java)
        viewModel.GetImagenes().observe(viewLifecycleOwner, Observer {
            if(it.size==0){
                imgBannerMain.visibility=View.VISIBLE
                txtMensajeBanner.visibility=View.VISIBLE
            }else{
                imgBannerMain.visibility=View.GONE
                txtMensajeBanner.visibility=View.GONE
            }
            rvAdapterImageBannerWeb.addList(ArrayList(it))
        })
        rvBannerImg.adapter = rvAdapterImageBannerWeb
        rvBannerImg.layoutManager = LinearLayoutManager(context)
        btnAgregaImagen.setOnClickListener {
            AgregaBanner()
        }

        val cargaDialog = DialogCargaAsync(context)
        val dialogAsync = cargaDialog.getDialogCarga("Espere un momento")
        swImagenWeb.setOnCheckedChangeListener { compoundButton, b ->
            viewModel.setVisibleWeb(b)
        }
        viewModel.activoWeb.observe(viewLifecycleOwner, Observer {
            swImagenWeb.isChecked=it
        })
        viewModel.loadingStateCarga.observe(viewLifecycleOwner, Observer {

            if(it.loading){
                permiterGuardar=false
                swImagenWeb.visibility=View.GONE
                btnAgregaImagen.visibility=View.GONE
                pbBannerConfig.visibility=View.VISIBLE
                imgBannerMain.visibility=View.GONE
                txtMensajeBanner.visibility=View.GONE

            }else{
                permiterGuardar=true
                swImagenWeb.visibility=View.VISIBLE
                btnAgregaImagen.visibility=View.VISIBLE
                pbBannerConfig.visibility=View.GONE
            }

        })
        viewModel.loadingStateGuardar.observe(viewLifecycleOwner, Observer {
            if (it.loading) {
                dialogAsync.show()
            } else {
                dialogAsync.hide()
            }
            if (it.terminate) {
                if (it.resultOk) {
                    AlertDialog.Builder(context).setTitle("Confirmación").setMessage(it.message)
                            .setPositiveButton("Aceptar") { dialogInterface, i ->
                                viewModel.CloseLoadingState()
                                activity?.finish()
                            }.create()
                            .show()
                } else {
                    AlertDialog.Builder(context).setTitle("Advertencia").setMessage(it.message)
                            .setPositiveButton("Salir"){ dialogInterface, i ->
                                viewModel.CloseLoadingState()
                                activity?.finish()
                            }.create()
                            .show()
                }

            }

        })
        rvAdapterImageBannerWeb.iRvAdapterImage = object : RvAdapterImageBannerWeb.IRvAdapterImage {
            override fun updateList(listMedios: ArrayList<MedioImagen>) {
                viewModel.UpdateList(listMedios)
            }
            override fun itemDelete(medioImagen: MedioImagen) {
                viewModel.AddItemDelete(medioImagen)
            }
        }
    }

    fun AgregaBanner() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        i.type = "image/*"
        startActivityForResult(Intent.createChooser(i, "Selecciona una imagen"), SELECT_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            try {
                val imageStream = requireActivity().contentResolver.openInputStream(uri!!)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                val imageBitmap: Bitmap = selectedImage
                var bmpImg = imageBitmap
                rvAdapterImageBannerWeb.AddImage(bmpImg)
            } catch (ex: Exception) {
                Log.i("no-media-c1", ex.toString())
            } catch (e: FileNotFoundException) {
                Log.i("no-media-c", e.toString())
            }
        }
    }
}

