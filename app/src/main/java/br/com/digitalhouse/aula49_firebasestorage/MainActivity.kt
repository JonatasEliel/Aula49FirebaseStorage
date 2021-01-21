package br.com.digitalhouse.aula49_firebasestorage

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.com.digitalhouse.aula49_firebasestorage.databinding.ActivityMainBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog

class MainActivity : AppCompatActivity() {
    private lateinit var bind: ActivityMainBinding
    lateinit var alertDialog: AlertDialog
    lateinit var storageReference: StorageReference
    private val CODE_IMG = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        config()

        bind.fbUpload.setOnClickListener{
            setIntents()
        }
    }

    fun config(){
        alertDialog = SpotsDialog.Builder().setContext(this).build()
        storageReference = FirebaseStorage.getInstance().getReference(bind!!.edFileName!!.toString())
    }

    // Configurar a intent para obter a imagem da galeria
    fun setIntents(){
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Capturar imagem"), CODE_IMG)
    }

    // Capturar eventos de uma intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_IMG){
            alertDialog.show()
            val uploadTask = storageReference.putFile(data!!.data!!)

            uploadTask.continueWithTask { task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                }
                storageReference!!.downloadUrl
            }.addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val downloadUri = task.result
                    //val url = downloadUri!!.toString().substring(0, downloadUri.toString().indexOf("&token"))

                    alertDialog.dismiss()
                    Picasso.get().load(downloadUri).into(bind.ivRec)
                }
            }
        }
    }
}