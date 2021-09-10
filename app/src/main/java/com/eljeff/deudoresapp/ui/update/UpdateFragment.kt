package com.eljeff.deudoresapp.ui.update

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
import com.eljeff.deudoresapp.databinding.FragmentUpdateBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class UpdateFragment : Fragment() {

    companion object {
        fun newInstance() = UpdateFragment()
    }

    private lateinit var viewModel: UpdateViewModel
    private var _binding: FragmentUpdateBinding? = null

    private val binding get() = _binding!!
    private var isSearching = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var idDebtor: String? = ""

        binding.updateButton.setOnClickListener {

            var debtorDao: DebtorDao = DeudoresApp.database.DebtorDao()
            val name = binding.updateNameEdTx.text.toString()


            if (isSearching) { //actualizando
                //searchInLocal(debtorDao, name, idDebtor)
                val db = Firebase.firestore
                db.collection("deudores").get().addOnSuccessListener { result ->
                    var debtorEsxist = false
                    for (document in result) {
                        val debtor: DebtorServer = document.toObject<DebtorServer>()
                        if (name == debtor.name) {

                            // guardamos el ID
                            idDebtor = debtor.id

                            debtorEsxist = true

                            with(binding) {
                                binding.updateDebtEdTx.setText(debtor.debt.toString())
                                binding.updatePhoneEdTx.setText(debtor.phone)
                                binding.updateButton.text = getString(R.string.title_update)
                                isSearching = false

                            }
                        }
                    }
                    if (!debtorEsxist) {
                        Toast.makeText(requireContext(), "El deudor no existe", Toast.LENGTH_SHORT).show()
                    }
                }
            } else { // actualizando
                //updateLocal(idDebtor, debtorDao)
                var documentUpdate = HashMap<String, Any>()

                documentUpdate["name"] = binding.updateNameEdTx.text.toString()
                documentUpdate["phone"] = binding.updatePhoneEdTx.text.toString()
                documentUpdate["debt"] = binding.updateDebtEdTx.text.toString().toLong()

                val db = Firebase.firestore
                idDebtor?.let { id ->
                    db.collection("deudores").document(id)
                        .update(documentUpdate).addOnSuccessListener {
                            Toast.makeText(requireContext(), "Deudor actualizado con exito", Toast.LENGTH_SHORT).show()
                        }
                }

                binding.updateButton.text = getString(R.string.search)
                isSearching = true
                cleanWidgets()
            }
        }
        return root
    }

    private fun updateLocal(idDebtor: String, debtorDao: DebtorDao) {
        /*val debtor = Debtor(
            id = idDebtor,
            name = binding.updateNameEdTx.text.toString(),
            phone = binding.updatePhoneEdTx.text.toString(),
            debt = binding.updateDebtEdTx.text.toString().toLong()
        )
        debtorDao.updateDebtor(debtor)*/
    }

    private fun searchInLocal(debtorDao: DebtorDao, name: String, idDebtor: Int) {
        var idDebtor1 = idDebtor
        var debtor: Debtor = debtorDao.readDeptor(name)
        if (debtor != null) {
            //idDebtor1 = debtor.id
            binding.updateDebtEdTx.setText(debtor.debt.toString())
            binding.updatePhoneEdTx.setText(debtor.phone)

            binding.updateButton.text = getString(R.string.title_update)

            isSearching = false

        } else {
            Toast.makeText(requireContext(), "No existe", Toast.LENGTH_SHORT).show()
            cleanWidgets()
        }
    }

    private fun cleanWidgets() {
        with(binding) {
            updateDebtEdTx.setText("")
            updateNameEdTx.setText("")
            updatePhoneEdTx.setText("")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UpdateViewModel::class.java)
        // TODO: Use the ViewModel
    }

}