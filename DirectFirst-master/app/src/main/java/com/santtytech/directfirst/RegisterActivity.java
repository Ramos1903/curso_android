package com.santtytech.directfirst;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;


public class RegisterActivity extends AppCompatActivity {
    private EditText mEditNome;
    private EditText mEditEmail;
    private EditText mEditSenha;
    private Button mBtnCadastro;
    private Button mBtnPhoto;
    private ImageView mImgPerfil;

    private Uri mSelectedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //localizando e atribuindo parametros para as variaveis
        mEditNome = findViewById(R.id.textNome);
        mEditEmail = findViewById(R.id.textEmail);
        mEditSenha = findViewById(R.id.textPassword);
        mBtnCadastro = findViewById(R.id.btnSend);
        mBtnPhoto = findViewById(R.id.btnSelectPhoto);
        mImgPerfil = findViewById(R.id.imageBtnPerfil);

        //selecionar photo
        mBtnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });

        //evento ao botão cadastro de novo usuario
        mBtnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
                clean();

            }
        });


    }

    private void clean() {
        mEditNome = null;
        mEditEmail = null;
        mEditSenha = null;
        mImgPerfil = null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            mSelectedUri = data.getData();

            Bitmap bitmap = null;
            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ImageDecoder.Source source;
                    source = ImageDecoder.createSource(this.getContentResolver(), mSelectedUri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                    mImgPerfil.setImageDrawable(new BitmapDrawable(getResources(), bitmap));

                }
                mBtnPhoto.setAlpha(0);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //instanciando photo no btnImg
    public void selectPhoto() {

        Intent intent = (new Intent(Intent.ACTION_PICK));
        intent.setType("image/*");


        startActivityForResult(intent, 0);

    }

    //metodo para cadastras novo usuario
    public void createUser() {
        String email = mEditEmail.getText().toString();
        String senha = mEditSenha.getText().toString();
        String nome = mEditNome.getText().toString();

        //condicao para verificar se os itens email e senha foram preenchidos
        if (nome == null || nome.isEmpty() || email == null || email.isEmpty() || senha == null || senha.isEmpty()) {
            Toast.makeText(this, "Nome email e senha são campos obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Criando usuário pelo mecanismo do Firebase                            //adicionando o usuário a lista FireBase
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            //criando exceções para criação do usuário
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Log.i("Sucesso", task.getResult().getUser().getUid());
                    saveUserInFirebase();
                }

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Falha", e.getMessage());
            }
        });


    }

    private void saveUserInFirebase() {
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);
        ref.putFile(mSelectedUri)

                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                            @Override
                            public void onSuccess(Uri uri) {
                                Log.i("Teste", uri.toString());

                                String uid = FirebaseAuth.getInstance().getUid();
                                String username = mEditNome.getText().toString();
                                String profileUrl = uri.toString();

                                User user = new User(uid, username, profileUrl);

                                FirebaseFirestore.getInstance().collection("users")
                                        .add(user)

                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.i("Teste", documentReference.getId());

                                                Intent intent = new Intent(RegisterActivity.this, MessagesActivity.class);

                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                                startActivity(intent);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("Teste", e.getMessage());


                                    }
                                });

                                //startActivity( intent );
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Teste", e.getMessage(), e);
            }
        });
    }
}
