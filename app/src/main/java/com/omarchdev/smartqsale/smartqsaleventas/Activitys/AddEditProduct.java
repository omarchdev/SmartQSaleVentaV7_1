package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.CategoriaAdapter;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProductos;
import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImagenesController;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCategoriaProductos;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddEditProduct extends ActivityParent implements View.OnClickListener {

    final int CAMERA_CAPTURE = 1;
    final int SELECT_PICTURE = 200;
    final int CROP_PIC = 2;
    ImagenesController imagenesController;
    ControladorProductos controladorProductos;
    String campoUnidad="Seleccione una Unidad";
    String EstadoProducto;
    boolean estadoImagen=false;
    ImageButton btnAceptar;
    ImageButton btnCancelar;
    CircleImageView circleImageViewProducto;
    EditText edtCategoriaProduct;
    EditText edtKey;
    Button btnScan;
    ImageButton imgElegirFoto;
    EditText edtUnit;
    EditText edtAdditionalInformation;
    EditText edtProductName;
    EditText edtQuantity;
    LinearLayoutManager llm;
    EditText edtQuantityReserve;
    EditText edtPurcharsePrice;
    EditText edtSalesPrice;
    ImageButton imgBtnAddPrice;
    BdConnectionSql bdConnectionSql;
    ImageView imgProduct;
    Bitmap bmp;
    ImageButton btnTomarFoto;
    int codigoProducto=0;
    String option="";
    CategoriaAdapter categoriaAdapter;
    List<mCategoriaProductos> list;

    int idProducto;
    private Spinner spinnerCategoria;
    private Uri picUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imagenesController = new ImagenesController();

        VerificarExisteProducto(getIntent().getIntExtra("idProducto", 0));
        bdConnectionSql = BdConnectionSql.getSinglentonInstance();

        bmp=null;
        controladorProductos=new ControladorProductos(this);
        spinnerCategoria = (Spinner) findViewById(R.id.spinner_categoria);
        list = bdConnectionSql.getCategorias(0, "");
        categoriaAdapter = new CategoriaAdapter(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinnerCategoria.setAdapter(categoriaAdapter);
        EstadoProducto=getIntent().getStringExtra(Constantes.EstadoProducto.EstadoProducto);
        llm=new LinearLayoutManager(this);


        btnTomarFoto=(ImageButton)findViewById(R.id.btnCamaraImagen);
        //circleImageViewProducto=(CircleImageView)findViewById(R.id.CircleImageProducto);
        imgProduct = (ImageView) findViewById(R.id.imageProduct);

        btnScan=(Button)findViewById(R.id.btnScan);
        imgElegirFoto=(ImageButton)findViewById(R.id.btnBuscarImagen);

        imgElegirFoto.setOnClickListener(this);
        btnTomarFoto.setOnClickListener(this);

        edtCategoriaProduct.setOnClickListener(this);
        VerificarEstado();


    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.btnScan){
            option="ScanearCodigo";
            // ScanCode();


        }

        if(v.getId()==R.id.btnCamaraImagen) {
            option="TomarFoto";
            Toast.makeText(this, "seleccionado", Toast.LENGTH_LONG).show();
            takephoto();

        }
        if(v.getId()==R.id.btnBuscarImagen){
            option="ElegirFoto";
            Toast.makeText(this, "seleccionado", Toast.LENGTH_LONG).show();

            choisePhoto();
        }

    }

    private void ScanCode(){

        IntentIntegrator scanIntegrator=new IntentIntegrator(this);
        scanIntegrator.setPrompt("Escannear Codigo de barras");
        scanIntegrator.setBeepEnabled(true);
        scanIntegrator.setOrientationLocked(true);
        scanIntegrator.setBarcodeImageEnabled(true);
        scanIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent intent){

        super.onActivityResult(requestCode,resultCode,intent);
        IntentResult scanningResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);
        if (requestCode == CROP_PIC) {
            Bundle ext = intent.getExtras();
            Bitmap thePic = ext.getParcelable("data");
            imgProduct.setImageBitmap(thePic);
        }
        if(option=="TomarFoto"){
            if(resultCode== Activity.RESULT_OK){
                if (requestCode == CAMERA_CAPTURE) {


                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                        imageBitmap = imagenesController.cropToSquare(imageBitmap);
                        imageBitmap = imagenesController.cambiarResolucion(imageBitmap);
                        imgProduct.setImageBitmap(imageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    estadoImagen = true;
                }

            }
        } else if(option=="ScanearCodigo") {
            String scanContent = scanningResult.getContents().toString();
            edtKey.setText(scanContent);
            Toast.makeText(this, scanContent, Toast.LENGTH_LONG).show();
        } else if(option=="ElegirFoto"){
            if (resultCode == Activity.RESULT_OK) {


                Uri path=intent.getData();
                try {
                    bmp=MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                    bmp = imagenesController.cropToSquare(bmp);
                    bmp = imagenesController.cambiarResolucion(bmp);
                    imgProduct.setImageBitmap(bmp);

                } catch (IOException e) {
                    e.printStackTrace();
                }


                estadoImagen=true;
            }


        }
    }


    public boolean validarCampos(){

        boolean vacio=false;

        if(!edtProductName.getText().toString().isEmpty() &&
                !edtUnit.getText().toString().isEmpty() && !edtQuantity.getText().toString().isEmpty()
                && !edtQuantityReserve.getText().toString().isEmpty() && !edtPurcharsePrice.getText().toString().isEmpty()
                && !edtSalesPrice.getText().toString().isEmpty() && !edtKey.getText().toString().isEmpty()){

            vacio=true;

        }
        return vacio;
    }

    public Dialog onCreateDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        return builder.create();
    }

    public void CreateDialogInsertPrice(){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=this.getLayoutInflater();
        View v=inflater.inflate(R.layout.dialog_insert_price_additional,null);
        builder.setView(v);

        final EditText edtAddPrice=(EditText)v.findViewById(R.id.edtAddPrice);

        builder.setTitle("Ingresa un nuevo Precio");

        builder.setPositiveButton(
                "Ingresar",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String a=edtAddPrice.getText().toString();
                        a.length();
                        a.toString();

                        edtCategoriaProduct.setText(a);
                    }
                }
        );
        builder.setNegativeButton("Cancelar",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                edtCategoriaProduct.setText("");
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }

    public void takephoto(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            picUri = Uri.fromFile(
                    new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_d" + String.valueOf(System.currentTimeMillis())
                                    + ".jpg")

            );

            intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
            startActivityForResult(intent, CAMERA_CAPTURE);


        }

    }

    public void choisePhoto(){

        Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(getIntent().createChooser(i,"Selecciona una imagen"),SELECT_PICTURE);
    }

    private void EnviarDatosCampos(){
        codigoProducto=getIntent().getIntExtra("IdProducto",0);
        edtKey.setText(getIntent().getStringExtra("CodigoProducto"));
        edtProductName.setText(getIntent().getStringExtra("NombreProducto"));
        edtUnit.setText(getIntent().getStringExtra("Unidad"));
        edtQuantity.setText(String.valueOf(getIntent().getFloatExtra("Cantidad",0)));
        edtQuantityReserve.setText(String.valueOf(getIntent().getFloatExtra("CantidadReserva",0)));
        edtAdditionalInformation.setText(getIntent().getStringExtra("InformacionAdicional"));
        edtPurcharsePrice.setText(String.valueOf(getIntent().getFloatExtra("PrecioCompra",0)));
        edtSalesPrice.setText(String.valueOf(getIntent().getFloatExtra("PrecioVenta",0)));
        byte[]data=getIntent().getByteArrayExtra("DataImagen");
        if(data!=null) {
            bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            circleImageViewProducto.setImageBitmap(bmp);
        }
    }

    private void VaciarCampos(){
        edtKey.setText("");
        edtProductName.setText("");
        edtQuantity.setText("");
        edtQuantityReserve.setText("");
        edtSalesPrice.setText("");
        edtPurcharsePrice.setText("");
        edtAdditionalInformation.setText("");
    }

    private void VerificarEstado() {

        switch (EstadoProducto) {
            case Constantes.EstadoProducto.EditarProducto:
                EnviarDatosCampos();
                break;
            case Constantes.EstadoProducto.NuevoProducto:
                VaciarCampos();
                break;
        }
    }

    private boolean ValidarPrecioCompraMayorPrecioVenta(){
        boolean a;
        if(Float.parseFloat(edtPurcharsePrice.getText().toString())<Float.parseFloat(edtSalesPrice.getText().toString())){
            a=true;

        } else{
            Toast.makeText(this,"El precio de venta debe ser mayor al precio de Compra",Toast.LENGTH_SHORT).show();
            edtSalesPrice.setError("Debe ser mayor al precio de compra");
            a=false;
        }
        return a;
    }

    private boolean VerificarCamposCompletados(){

        boolean permitir;
        if(!edtKey.getText().toString().isEmpty() && !edtProductName.getText().toString().isEmpty()
                && !edtUnit.getText().toString().equals(campoUnidad) && !edtQuantity.getText().toString().isEmpty()
                && !edtQuantityReserve.getText().toString().isEmpty() && !edtSalesPrice.getText().toString().isEmpty()
                && !edtPurcharsePrice.getText().toString().isEmpty()){

            permitir=true;
        } else{

            permitir=false;
        }
        return permitir;

    }

    private mProduct ObtenerDatos(){
        mProduct product = new mProduct();
        product.setIdProduct(codigoProducto);
        product.setcKey(edtKey.getText().toString());
        product.setcProductName(edtProductName.getText().toString());
        product.setcUnit(edtUnit.getText().toString());
        product.setdQuantity(Float.parseFloat(edtQuantity.getText().toString()));
        product.setdQuantityReserve(Float.parseFloat(edtQuantityReserve.getText().toString()));
        product.setdPurcharsePrice(Float.parseFloat(edtPurcharsePrice.getText().toString()));
        product.setdSalesPrice(Float.parseFloat(edtSalesPrice.getText().toString()));
        product.setcAdditionalInformation(edtAdditionalInformation.getText().toString());
        if(bmp!=null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            product.setbImage(stream.toByteArray());
        } else{
            product.setbImage(null);
        }
        return product;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void VerificarExisteProducto(int idProducto) {

        if (idProducto == 0) {

            getSupportActionBar().setTitle("Agregar Producto");

        } else if (idProducto != 0) {
            getSupportActionBar().setTitle("Editar Producto");
        }

    }

    public void cropCaptureImage() {

        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", false);
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 2);
            cropIntent.putExtra("outputX", 250);
            cropIntent.putExtra("outputY", 250);

            cropIntent.putExtra("return-data", true);

            //iniciamos nuestra activity y pasamos un codigo de respuesta.
            startActivityForResult(cropIntent, CROP_PIC);
        } catch (ActivityNotFoundException anfe) {

            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();

        }

    }


}

