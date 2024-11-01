package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncReportePeriodoKt;
import com.omarchdev.smartqsale.smartqsaleventas.ConsultaHttp.HttpConsultasKt;
import com.omarchdev.smartqsale.smartqsaleventas.Model.CategoriaPack;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;

import kotlin.jvm.internal.Intrinsics;


public  class RvAdapterPrecioCategoria extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CategoriaPack> list = new ArrayList();
    private int positionMostrar = -1;

    /* compiled from: RvAdapterPrecioCategoria.kt */
    public static final class ItemVh extends RecyclerView.ViewHolder {
        private final ImageButton btnEdit;
        private final EditText edtPrecio;
        private final ImageButton imgCancelar;
        private final ImageButton imgSave;
        private final TextView txtCategoria;
        private final TextView txtPrecio;

        public ItemVh(@NotNull View item) {
             super(item);
            this.txtCategoria = (TextView) item.findViewById(R.id.txtCategoria);
            this.edtPrecio = (EditText) item.findViewById(R.id.edtPrecioCategoria);
            this.txtPrecio = (TextView) item.findViewById(R.id.txtPrecio);
            this.imgSave = (ImageButton) item.findViewById(R.id.imgSave);
            this.imgCancelar = (ImageButton) item.findViewById(R.id.imgCancelar);
            this.btnEdit = (ImageButton) item.findViewById(R.id.btnEdit);
        }

        public final TextView getTxtCategoria() {
            return this.txtCategoria;
        }

        public final EditText getEdtPrecio() {
            return this.edtPrecio;
        }

        public final TextView getTxtPrecio() {
            return this.txtPrecio;
        }

        public final ImageButton getImgSave() {
            return this.imgSave;
        }

        public final ImageButton getImgCancelar() {
            return this.imgCancelar;
        }

        public final ImageButton getBtnEdit() {
            return this.btnEdit;
        }
    }

    public final int getPositionMostrar() {
        return this.positionMostrar;
    }

    public final void setPositionMostrar(int pos) {
        this.positionMostrar = pos;
    }

    @NotNull
    public final ArrayList<CategoriaPack> getList() {
        return this.list;
    }

    @NotNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_item_categoria_precio, parent, false);
        return new ItemVh(v);
    }



    public int getItemCount() {
        return this.list.size();
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemVh h = (ItemVh) holder;
        TextView txtCategoria = h.getTxtCategoria();
        Object obj = this.list.get(position);
        txtCategoria.setText(((CategoriaPack) obj).getDescripcionCategoria());
        h.getTxtPrecio().setText(AsyncReportePeriodoKt.getFortMoneda(((CategoriaPack) this.list.get(position)).getPrecio()));
        h.getBtnEdit().setOnClickListener(new RvAdapterPrecioCategoria$onBindViewHolder$1(this, position, h));
        h.getImgSave().setOnClickListener(new RvAdapterPrecioCategoria$onBindViewHolder$2(this, h, position));
        h.getImgCancelar().setOnClickListener(new RvAdapterPrecioCategoria$onBindViewHolder$3(this));
        EditText edtPrecio;
        ImageButton imgCancelar;
        if (this.positionMostrar == position) {


            edtPrecio = h.getEdtPrecio();




            edtPrecio.setVisibility(View.VISIBLE);
            imgCancelar = h.getImgCancelar();

            imgCancelar.setVisibility(View.VISIBLE);
            imgCancelar = h.getImgSave();


            imgCancelar.setVisibility(View.VISIBLE);
            imgCancelar = h.getBtnEdit();


            imgCancelar.setVisibility(View.GONE);
        } else {
            edtPrecio = h.getEdtPrecio();


            edtPrecio.setVisibility(View.GONE);
            imgCancelar = h.getImgCancelar();

            imgCancelar.setVisibility(View.GONE);
            imgCancelar = h.getImgSave();
             imgCancelar.setVisibility(View.GONE);
            imgCancelar = h.getBtnEdit();
            imgCancelar.setVisibility(View.GONE);
        }
    }

    public final void AgregarElementos(@NotNull ArrayList<CategoriaPack> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    final class RvAdapterPrecioCategoria$onBindViewHolder$1 implements View.OnClickListener {
        final /* synthetic */ ItemVh $h;
        final /* synthetic */ int $position;
        final /* synthetic */ RvAdapterPrecioCategoria this$0;

        RvAdapterPrecioCategoria$onBindViewHolder$1(RvAdapterPrecioCategoria rvAdapterPrecioCategoria, int i, ItemVh itemVh) {
            this.this$0 = rvAdapterPrecioCategoria;
            this.$position = i;
            this.$h = itemVh;
        }

        public final void onClick(View it) {
            this.this$0.setPositionMostrar(this.$position);
            this.$h.getEdtPrecio().setText(AsyncReportePeriodoKt.getFortMoneda(((CategoriaPack) this.this$0.getList().get(this.$position)).getPrecio()));
            this.this$0.notifyDataSetChanged();
        }
    }

    final class RvAdapterPrecioCategoria$onBindViewHolder$2 implements View.OnClickListener {
        final /* synthetic */ ItemVh $h;
        final /* synthetic */ int $position;
        final /* synthetic */ RvAdapterPrecioCategoria this$0;

        RvAdapterPrecioCategoria$onBindViewHolder$2(RvAdapterPrecioCategoria rvAdapterPrecioCategoria, ItemVh itemVh, int i) {
            this.this$0 = rvAdapterPrecioCategoria;
            this.$h = itemVh;
            this.$position = i;
        }

        public final void onClick(View it) {
            this.this$0.setPositionMostrar(-1);
            EditText edtPrecio = this.$h.getEdtPrecio();
            Intrinsics.checkExpressionValueIsNotNull(edtPrecio, "h.edtPrecio");
            if (edtPrecio.getText().toString().equals(".")) {
                ((CategoriaPack) this.this$0.getList().get(this.$position)).setPrecio(HttpConsultasKt.getBg(0));
            } else {
                edtPrecio = this.$h.getEdtPrecio();
                Intrinsics.checkExpressionValueIsNotNull(edtPrecio, "h.edtPrecio");
                if (edtPrecio.getText().toString().equals("0.")) {
                    ((CategoriaPack) this.this$0.getList().get(this.$position)).setPrecio(HttpConsultasKt.getBg(0));
                } else {
                    edtPrecio = this.$h.getEdtPrecio();
                    Intrinsics.checkExpressionValueIsNotNull(edtPrecio, "h.edtPrecio");
                    if (edtPrecio.getText().toString().equals(".0")) {
                        ((CategoriaPack) this.this$0.getList().get(this.$position)).setPrecio(HttpConsultasKt.getBg(0));
                    } else {
                        edtPrecio = this.$h.getEdtPrecio();
                        Intrinsics.checkExpressionValueIsNotNull(edtPrecio, "h.edtPrecio");
                        if (edtPrecio.getText().toString().equals("")) {
                            ((CategoriaPack) this.this$0.getList().get(this.$position)).setPrecio(HttpConsultasKt.getBg(0));
                        } else {
                            CategoriaPack categoriaPack = (CategoriaPack) this.this$0.getList().get(this.$position);
                            EditText edtPrecio2 = this.$h.getEdtPrecio();
                            Intrinsics.checkExpressionValueIsNotNull(edtPrecio2, "h.edtPrecio");
                            categoriaPack.setPrecio(new BigDecimal(edtPrecio2.getText().toString()));
                        }
                    }
                }
            }
            this.this$0.notifyDataSetChanged();
        }
    }

    final class RvAdapterPrecioCategoria$onBindViewHolder$3 implements View.OnClickListener {
        final /* synthetic */ RvAdapterPrecioCategoria this$0;

        RvAdapterPrecioCategoria$onBindViewHolder$3(RvAdapterPrecioCategoria rvAdapterPrecioCategoria) {
            this.this$0 = rvAdapterPrecioCategoria;
        }

        public final void onClick(View it) {
            this.this$0.setPositionMostrar(-1);
            this.this$0.notifyDataSetChanged();
        }
    }
}
