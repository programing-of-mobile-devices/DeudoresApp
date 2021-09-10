package com.eljeff.deudoresapp.ui.create

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eljeff.deudoresapp.DeudoresApp
import com.eljeff.deudoresapp.R
import com.eljeff.deudoresapp.data.local.dao.DebtorDao
import com.eljeff.deudoresapp.data.local.entities.Debtor
import com.eljeff.deudoresapp.data.server.DebtorServer
import com.eljeff.deudoresapp.databinding.FragmentCreateBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.sql.Types.NULL


class CreateFragment : Fragment() {

    private lateinit var dashboardViewModel: CreateViewModel
    private var _binding: FragmentCreateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //imagen
    private var urlImage: String? = null
    private val REQUEST_IMAGE_CAPTURE = 1000

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val imageBitMap = data?.extras?.get("data") as Bitmap
            binding.takePictureImgVw.setImageBitmap(imageBitMap)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(CreateViewModel::class.java)

        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
          textView.text = it
        })*/

        //Lógica
        with(binding) {

            // click
            takePictureImgVw.setOnClickListener {
                dispatchTakePictureIntent()
            }

            createButton.setOnClickListener {
                saveDeptor()
                //savePicture()
            }
        }
        return root
    }

    private fun saveDeptor() {

        // instanciamos la base de datos en firebase
        val db = Firebase.firestore
        // se hubica el documento en la colección
        val document = db.collection("deudores").document()
        // genera un nuevo id
        val id = document.id

        // información de la imagen
        var storage = FirebaseStorage.getInstance()
        val pictureRef = storage.reference.child("debtors").child(id)
        binding.takePictureImgVw.isDrawingCacheEnabled = true
        binding.takePictureImgVw.buildDrawingCache()
        val bitmap = (binding.takePictureImgVw.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        //mandar data a la nube
        var uploadTask = pictureRef.putBytes(data)
        val urlTask = uploadTask.continueWithTask{ task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            pictureRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val urlPicture = task.result.toString()

                with(binding) {

                    // capturamos datos de la interfaz
                    val name = nameEdTx.text.toString()
                    val phone = phoneEdTx.text.toString()
                    val debt = debtEdTx.text.toString().toLong()

                    //creamos el dudor
                    val debtorServer = DebtorServer(id, name, phone, debt, urlPicture)

                    // Agregamos el deudor a la base de datos
                    db.collection("deudores").document(id).set(debtorServer)

                    // Limpiamos las vistas
                    cleanViews()
                }

            } else {
                // Handle failures
                // ...
            }
        }


    }

    private fun dispatchTakePictureIntent() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(intent)

    }

    private fun cleanViews() {
        with(binding) {
            nameEdTx.setText("")
            phoneEdTx.setText("")
            debtEdTx.setText("")
            //Picasso.get().load(R.drawable.ic_camera).into(takePictureImgVw)
            takePictureImgVw.setImageResource(R.drawable.ic_camera)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}