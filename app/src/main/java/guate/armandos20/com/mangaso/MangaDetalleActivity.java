package guate.armandos20.com.mangaso;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import guate.armandos20.com.mangaso.Entidades.Home;

public class MangaDetalleActivity extends AppCompatActivity {

    //Widgets
    private ImageView mImageView;
    private TextView mTextViewDescripcion;
    private TextView mTextViewTemprada;
    private TextView mTextViewRanking;
    private View mParentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_detalle);
        //Intent intent = getIntent();
        //final String cheeseName = intent.getStringExtra(EXTRA_NAME);

        mParentLayout = findViewById(android.R.id.content);
        mImageView = findViewById(R.id.backdrop);
        mTextViewDescripcion = findViewById(R.id.tv_descripcion);
        mTextViewTemprada = findViewById(R.id.tv_temporadas);
        mTextViewRanking = findViewById(R.id.tv_ranking);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Home mHome = null;
        Intent i = getIntent();

        mHome = i.getParcelableExtra("miLista");


        String nombre = mHome.getTitulo().toString();
        String imagen = mHome.getUrl_portada().toString();
        String descripcion = mHome.getDescripcion().toString();
        String tempradas = mHome.getTemporadas().toString();
        String ranking = mHome.getRanking().toString();

        //Cargar toolbar colaps
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(nombre);

        //Cargar widgets
        loadBackdrop(imagen);
        mTextViewDescripcion.setText(descripcion);

        String mensaje = "";
        if (tempradas.equals("1")){
            mensaje = "Primera temporada actualmente.";
            mTextViewTemprada.setText(mensaje);
        }else if (tempradas.equals("2")){
            mensaje = "Segunda temporada actualmente.";
            mTextViewTemprada.setText(mensaje);
        }else if (tempradas.equals("3")){
            mensaje = "Tercera temporada actualmente.";
            mTextViewTemprada.setText(mensaje);
        }else{
            mTextViewTemprada.setText(tempradas);
        }

        mTextViewRanking.setText(ranking);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                DocumentReference newNoteRef = db.collection("notes").document("9Z07ICTjas6453a9uvkg").collection("usuarios").document();

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


                Map<String, Object> data = new HashMap<>();
                data.put("usuarios", userId);

                newNoteRef.set(data, SetOptions.merge());
                //db.collection("notes").document().collection("usuarios").document().set(data, SetOptions.merge());

                newNoteRef.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            makeSnackBarMessage("Created a new note");
                        }else{
                            makeSnackBarMessage("Failed. Check Log");
                        }
                    }
                });

            }
        });

    }

    private void loadBackdrop(String url_imagen) {
        final ImageView imageView = findViewById(R.id.backdrop);
        Glide.with(this)
                .load(url_imagen)
                .apply(RequestOptions.centerCropTransform())
                .into(mImageView);
    }

    private void makeSnackBarMessage(String message){
        Snackbar.make(mParentLayout, message, Snackbar.LENGTH_SHORT).show();
    }
}
