package com.omarchdev.smartqsale.smartqsaleventas.Fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.Controlador.RadioGroupedButton;
import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImagenesController;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class RepresentacionProducto extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    ImagenesController imgController;
    RadioGroup rgShape;
    Bitmap bmpImg;
    ImageView imageProduct;
    ImageButton imgChoisePhoto, imgTakePhoto;
    boolean modificoImagen;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;

    final byte REPRESENTACION_COLOR = 1; // 1 Cuando se seleccione radio button color
    final byte REPRESENTACION_IMAGEN = 2;// 2 Cuando se seleccione radio button imagen
    byte RepresentacionSeleccionada;    // Aqui se guarda la representacion selecionada
    final int CAMERA_CAPTURE = 1337;
    final int SELECT_PICTURE = 200;
    File file;
    Resources res;
    String[] colores;
    String[] formas;
    RadioGroupedButton rb1, rb2, rb3, rb4, rb5, rb6, rb7, rb8;
    RadioGroup rgImagen;
    RadioButton rbColor, rbImagen;
    RelativeLayout rlayoutColor;
    ConstraintLayout rlayoutImagen;
    String ColorSeleccionado;
    String FormaSeleccionada;
    ListenerRepresentacionProducto listenerRepresentacionProducto;

    public interface ListenerRepresentacionProducto {

        void obtenerRepresentacionProducto(byte TipoRepresentacion, Bitmap bmpProduct, String ColorSeleccionado, String FormaSeleccionada);


    }

    public void setListenerRepresentacionProducto(ListenerRepresentacionProducto listenerRepresentacionProducto) {

        this.listenerRepresentacionProducto = listenerRepresentacionProducto;
    }

    public RepresentacionProducto() {
        // Required empty public constructor
        bmpImg = null;
        ColorSeleccionado = "";
        RepresentacionSeleccionada = REPRESENTACION_COLOR;
        FormaSeleccionada = "";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_representacion_producto, container, false);

        //---Asignar colores--//
        res = getResources();
        colores = res.getStringArray(R.array.colores_seleccion_producto);
        formas = res.getStringArray(R.array.shape_form);
        //Inicializar UI
        rgImagen = (RadioGroup) v.findViewById(R.id.rgImagen);
        rgShape = (RadioGroup) v.findViewById(R.id.rgShape);

        rbImagen = (RadioButton) v.findViewById(R.id.rbImagen);
        rbColor = (RadioButton) v.findViewById(R.id.rbColor);

        rlayoutColor = (RelativeLayout) v.findViewById(R.id.rlayoutColor);
        rlayoutImagen = v.findViewById(R.id.rlayoutImagen);

        imgChoisePhoto = (ImageButton) v.findViewById(R.id.btnBuscarImagen);
        imgTakePhoto = (ImageButton) v.findViewById(R.id.btnCamaraImagen);
        imageProduct = (ImageView) v.findViewById(R.id.imageProduct);


        imgController = new ImagenesController();


        rlayoutColor.setVisibility(View.GONE);
        rlayoutImagen.setVisibility(View.GONE);

        rb1 = v.findViewById(R.id.group1);
        rb2 = v.findViewById(R.id.group2);
        rb3 = v.findViewById(R.id.group3);
        rb4 = v.findViewById(R.id.group4);
        rb5 = v.findViewById(R.id.group5);
        rb6 = v.findViewById(R.id.group6);
        rb7 = v.findViewById(R.id.group7);
        rb8 = v.findViewById(R.id.group8);

        imgChoisePhoto.setOnClickListener(this);
        imgTakePhoto.setOnClickListener(this);
        modificoImagen = false;

        rb1.setOnCheckedChangeListener(this);
        rb2.setOnCheckedChangeListener(this);
        rb3.setOnCheckedChangeListener(this);
        rb4.setOnCheckedChangeListener(this);
        rb5.setOnCheckedChangeListener(this);
        rb6.setOnCheckedChangeListener(this);
        rb7.setOnCheckedChangeListener(this);
        rb8.setOnCheckedChangeListener(this);
        rgImagen.setOnCheckedChangeListener(this);
        rgShape.setOnCheckedChangeListener(listenerForm);

        //ColorRecibido();

        ValoresPorDefecto();


        return v;
    }

    public void ColorRecibido() {
        String colorrecibido = "#42ff52";
        int a = 0;
        for (int i = 0; i < 8; i++) {

            if (colores[i].equals(colorrecibido)) {

                a = i;

            }

        }
        Toast.makeText(getActivity(), String.valueOf(a), Toast.LENGTH_SHORT).show();
        switch (a) {

            case 0:
                rb1.setChecked(true);
                break;
            case 1:
                rb2.setChecked(true);
                break;
            case 2:
                rb3.setChecked(true);
                break;
            case 3:
                rb4.setChecked(true);
                break;
            case 4:
                rb5.setChecked(true);
                break;
            case 5:
                rb6.setChecked(true);
                break;
            case 6:
                rb7.setChecked(true);
                break;
            case 7:
                rb8.setChecked(true);
                break;

        }
    }

    public void ValoresPorDefecto() {

        rb1.setChecked(true);
        rb2.setChecked(false);
        rb3.setChecked(false);
        rb4.setChecked(false);
        rb5.setChecked(false);
        rb6.setChecked(false);
        rb7.setChecked(false);
        rb8.setChecked(false);
        rbColor.setChecked(true);
        FormaSeleccionada = formas[0];
        ColorSeleccionado = colores[0];
        rgShape.check(R.id.rbShape1Rhombus);
    }

    RadioGroup.OnCheckedChangeListener listenerForm = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.rbShape1Rhombus) {
                FormaSeleccionada = formas[0];
                listenerRepresentacionProducto.obtenerRepresentacionProducto(RepresentacionSeleccionada, bmpImg, ColorSeleccionado, FormaSeleccionada);
            } else if (checkedId == R.id.rbShape1Square) {
                FormaSeleccionada = formas[1];
                listenerRepresentacionProducto.obtenerRepresentacionProducto(RepresentacionSeleccionada, bmpImg, ColorSeleccionado, FormaSeleccionada);
            } else if (checkedId == R.id.rbShape1Circle) {
                FormaSeleccionada = formas[2];
                listenerRepresentacionProducto.obtenerRepresentacionProducto(RepresentacionSeleccionada, bmpImg, ColorSeleccionado, FormaSeleccionada);
            } else if (checkedId == R.id.rbShape1Octagon) {
                FormaSeleccionada = formas[3];
                listenerRepresentacionProducto.obtenerRepresentacionProducto(RepresentacionSeleccionada, bmpImg, ColorSeleccionado, FormaSeleccionada);
            }
        }
    };

    public void rgPorDefecto() {


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnCamaraImagen:
                SolicitarPermiso();
                break;
            case R.id.btnBuscarImagen:
                choisePhoto();
                break;
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.group1 && isChecked == true) {
            rb2.setChecked(false);
            rb3.setChecked(false);
            rb4.setChecked(false);
            rb5.setChecked(false);
            rb6.setChecked(false);
            rb7.setChecked(false);
            rb8.setChecked(false);
            ColorSeleccionado = colores[0];
            listenerRepresentacionProducto.obtenerRepresentacionProducto(RepresentacionSeleccionada, bmpImg, ColorSeleccionado, FormaSeleccionada);
        } else if (buttonView.getId() == R.id.group2 && isChecked == true) {
            rb1.setChecked(false);
            rb3.setChecked(false);
            rb4.setChecked(false);
            rb5.setChecked(false);
            rb6.setChecked(false);
            rb7.setChecked(false);
            rb8.setChecked(false);
            ColorSeleccionado = colores[1];
            listenerRepresentacionProducto.obtenerRepresentacionProducto(RepresentacionSeleccionada, bmpImg, ColorSeleccionado, FormaSeleccionada);

        } else if (buttonView.getId() == R.id.group3 && isChecked == true) {

            rb1.setChecked(false);
            rb2.setChecked(false);
            rb4.setChecked(false);
            rb5.setChecked(false);
            rb6.setChecked(false);
            rb7.setChecked(false);
            rb8.setChecked(false);
            ColorSeleccionado = colores[2];
            listenerRepresentacionProducto.obtenerRepresentacionProducto(RepresentacionSeleccionada, bmpImg, ColorSeleccionado, FormaSeleccionada);
        } else if (buttonView.getId() == R.id.group4 && isChecked == true) {
            rb1.setChecked(false);
            rb2.setChecked(false);
            rb3.setChecked(false);
            rb5.setChecked(false);
            rb6.setChecked(false);
            rb7.setChecked(false);
            rb8.setChecked(false);
            ColorSeleccionado = colores[3];
            listenerRepresentacionProducto.obtenerRepresentacionProducto(RepresentacionSeleccionada, bmpImg, ColorSeleccionado, FormaSeleccionada);
        } else if (buttonView.getId() == R.id.group5 && isChecked == true) {

            rb1.setChecked(false);
            rb2.setChecked(false);
            rb3.setChecked(false);
            rb4.setChecked(false);
            rb6.setChecked(false);
            rb7.setChecked(false);
            rb8.setChecked(false);
            ColorSeleccionado = colores[4];
            listenerRepresentacionProducto.obtenerRepresentacionProducto(RepresentacionSeleccionada, bmpImg, ColorSeleccionado, FormaSeleccionada);

        } else if (buttonView.getId() == R.id.group6 && isChecked == true) {

            rb1.setChecked(false);
            rb2.setChecked(false);
            rb3.setChecked(false);
            rb4.setChecked(false);
            rb5.setChecked(false);
            rb7.setChecked(false);
            rb8.setChecked(false);
            ColorSeleccionado = colores[5];
            listenerRepresentacionProducto.obtenerRepresentacionProducto(RepresentacionSeleccionada, bmpImg, ColorSeleccionado, FormaSeleccionada);
        } else if (buttonView.getId() == R.id.group7 && isChecked == true) {
            rb1.setChecked(false);
            rb2.setChecked(false);
            rb3.setChecked(false);
            rb4.setChecked(false);
            rb5.setChecked(false);
            rb6.setChecked(false);
            rb8.setChecked(false);
            ColorSeleccionado = colores[6];
            listenerRepresentacionProducto.obtenerRepresentacionProducto(RepresentacionSeleccionada, bmpImg, ColorSeleccionado, FormaSeleccionada);
        } else if (buttonView.getId() == R.id.group8 && isChecked == true) {
            rb1.setChecked(false);
            rb2.setChecked(false);
            rb3.setChecked(false);
            rb4.setChecked(false);
            rb5.setChecked(false);
            rb6.setChecked(false);
            rb7.setChecked(false);
            ColorSeleccionado = colores[7];
            listenerRepresentacionProducto.obtenerRepresentacionProducto(RepresentacionSeleccionada, bmpImg, ColorSeleccionado, FormaSeleccionada);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rbColor) {
            rlayoutImagen.setVisibility(View.GONE);
            rlayoutColor.setVisibility(View.VISIBLE);
            RepresentacionSeleccionada = REPRESENTACION_COLOR;
            listenerRepresentacionProducto.obtenerRepresentacionProducto(RepresentacionSeleccionada, bmpImg, ColorSeleccionado, FormaSeleccionada);
        } else if (checkedId == R.id.rbImagen) {
            rlayoutColor.setVisibility(View.GONE);
            rlayoutImagen.setVisibility(View.VISIBLE);
            RepresentacionSeleccionada = REPRESENTACION_IMAGEN;
            listenerRepresentacionProducto.obtenerRepresentacionProducto(RepresentacionSeleccionada, bmpImg, ColorSeleccionado, FormaSeleccionada);
        }
    }

    public void choisePhoto() {

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(getActivity().getIntent().createChooser(i, "Selecciona una imagen"), SELECT_PICTURE);
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "SmartSale");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    public void takephoto() {
        try {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, CAMERA_CAPTURE);
            }

        } catch (Exception e) {

            e.toString();
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    public Bitmap GetImageBitmap() {

        return bmpImg;

    }

    public void setProductRepresentacion(mProduct product) {

        if (product.getbImage() != null) {
            bmpImg = imgController.ConvertByteArrayToBitmap(product.getbImage());
            imageProduct.setImageBitmap(bmpImg);

        }

        switch (product.getTipoRepresentacionImagen()) {

            case REPRESENTACION_COLOR:
                rgImagen.check(R.id.rbColor);
                break;

            case REPRESENTACION_IMAGEN:
                rgImagen.check(R.id.rbImagen);
                break;


        }
        for (int i = 0; i < rgImagen.getChildCount(); i++) {
            ((RadioButton) rgImagen.getChildAt(i)).setEnabled(false);
        }
        rgImagen.setEnabled(false);
        imgTakePhoto.setEnabled(false);
        imgChoisePhoto.setEnabled(false);


    }

    public void habilitarEdicion() {
        for (int i = 0; i < rgImagen.getChildCount(); i++) {
            ((RadioButton) rgImagen.getChildAt(i)).setEnabled(true);
        }
        rgImagen.setEnabled(true);
        imgTakePhoto.setEnabled(true);
        imgChoisePhoto.setEnabled(true);

    }

    private void SolicitarPermiso() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            Toast.makeText(getContext(), "Debe activar la camara para usar el scanner", Toast.LENGTH_LONG).show();

        } else {
            takephoto();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Si el permiso está concedido prosigue con el flujo normal
                takephoto();
            } else {
                //Si el permiso no fue concedido no se puede continuar
            }
        }
    }

    public boolean GetModificoImagen() {
        return modificoImagen;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(data!=null){

                if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
                    modificoImagen = true;
                    Uri uri = data.getData();

                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(uri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    Bitmap imageBitmap = imgController.ProcesarImagen(selectedImage);
                    imageProduct.setImageBitmap(imageBitmap);
                    bmpImg = imageBitmap;
                    listenerRepresentacionProducto.obtenerRepresentacionProducto(RepresentacionSeleccionada, imageBitmap, ColorSeleccionado, FormaSeleccionada);

                } else if (requestCode == CAMERA_CAPTURE && resultCode == RESULT_OK) {
                    modificoImagen = true;
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageBitmap = imgController.ProcesarImagen(imageBitmap);
                    imageProduct.setImageBitmap(imageBitmap);
                    bmpImg = imageBitmap;
                    listenerRepresentacionProducto.obtenerRepresentacionProducto(RepresentacionSeleccionada, imageBitmap, ColorSeleccionado, FormaSeleccionada);

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (Exception ex){
            Log.e("error1",ex.toString()    );
        }
    }

}

