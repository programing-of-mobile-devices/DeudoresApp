package com.eljeff.deudoresapp.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eljeff.deudoresapp.DeudoresApp
import com.eljeff.deudoresapp.data.local.dao.DebtorDao
import com.eljeff.deudoresapp.data.local.entities.Debtor
import com.eljeff.deudoresapp.data.server.DebtorServer
import com.eljeff.deudoresapp.databinding.FragmentCreateBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Types.NULL


class CreateFragment : Fragment() {

    private lateinit var dashboardViewModel: CreateViewModel
    private var _binding: FragmentCreateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
            createButton.setOnClickListener {
                val name = nameEdTx.text.toString()
                val phone = phoneEdTx.text.toString()
                val debt = debtEdTx.text.toString().toLong()

                // funcion para crear usuario
                //createDebtor(name, phone, debt)
                createDebtorServer(name, phone, debt)
            }
        }
        return root
    }

    private fun createDebtorServer(name: String, phone: String, debt: Long) {

        // instanciamos la base de datos en firebase
        val db = Firebase.firestore
        // se hubica el documento en la colección
        val document = db.collection("deudores").document()
        // genera un nuevo id
        val id = document.id

        //creamos el dudor
        val debtorServer = DebtorServer(id, name, phone, debt)

        // agregamos los datos del nuevo deudor al servidor
        db.collection("deudores").document(id).set(debtorServer)

        cleanViews()


    }

    private fun createDebtor(name: String, phone: String, debt: Long) {
        // se cre la entidad
        val debtor = Debtor(id = NULL, name = name, phone = phone, debt = debt)
        val debtorDao: DebtorDao = DeudoresApp.database.DebtorDao()
        debtorDao.createDebtor(debtor)

        cleanViews()

    }

    private fun cleanViews() {
        with(binding) {
            nameEdTx.setText("")
            phoneEdTx.setText("")
            debtEdTx.setText("")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}