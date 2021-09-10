package com.eljeff.deudoresapp.ui.read

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eljeff.deudoresapp.DeudoresApp
import com.eljeff.deudoresapp.R
import com.eljeff.deudoresapp.data.local.dao.DebtorDao
import com.eljeff.deudoresapp.data.local.entities.Debtor
import com.eljeff.deudoresapp.data.server.DebtorServer
import com.eljeff.deudoresapp.databinding.FragmentReadBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ReadFragment : Fragment() {

    private lateinit var notificationsViewModel: ReadViewModel
    private var _binding: FragmentReadBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(ReadViewModel::class.java)

        _binding = FragmentReadBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        // código de lectura
        binding.searchButton.setOnClickListener {
            //readDebtors(binding.searchNameEdTx.text.toString())
            readDebtorsFromServer(binding.searchNameEdTx.text.toString())
        }


        return root
    }

    private fun readDebtorsFromServer(name: String) {
        val db = Firebase.firestore
        db.collection("deudores").get().addOnSuccessListener { result ->
            var debtorEsxist = false
            for (document in result) {
                val debtor: DebtorServer = document.toObject<DebtorServer>()
                if (name == debtor.name) {
                    debtorEsxist = true
                    with(binding) {
                        searchPhoneTxVw.text = getString(R.string.phone_value, debtor.phone)
                        searchDebtTxVw.text = getString(R.string.debt_values, debtor.debt.toString())
                    }
                }
            }
            if(!debtorEsxist){
                Toast.makeText(requireContext(), "El deudor no existe", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // función leer deudores
    private fun readDebtors(name: String) {

        val debtorDao: DebtorDao = DeudoresApp.database.DebtorDao()
        val debtor: Debtor = debtorDao.readDeptor(name)

        if (debtor != null) {
            with(binding) {
                searchPhoneTxVw.text = getString(R.string.phone_value, debtor.phone)
                searchDebtTxVw.text = getString(R.string.debt_values, debtor.debt.toString())
            }
        } else {
            Toast.makeText(requireContext(), "No existe", Toast.LENGTH_SHORT).show()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}