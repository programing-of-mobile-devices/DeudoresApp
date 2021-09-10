package com.eljeff.deudoresapp.ui.delete

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.eljeff.deudoresapp.DeudoresApp
import com.eljeff.deudoresapp.R
import com.eljeff.deudoresapp.data.local.dao.DebtorDao
import com.eljeff.deudoresapp.data.local.entities.Debtor
import com.eljeff.deudoresapp.data.server.DebtorServer
import com.eljeff.deudoresapp.databinding.FragmentDeleteBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class DeleteFragment : Fragment() {

    private var _binding: FragmentDeleteBinding? = null

    companion object {
        fun newInstance() = DeleteFragment()
    }

    private lateinit var viewModel: DeleteViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDeleteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.deleteButton.setOnClickListener {
            //deleteDebtor(binding.deleteNameEdTx.text.toString())
            deleteDebtorFromServer(binding.deleteNameEdTx.text.toString())
        }

        return root
    }

    private fun deleteDebtorFromServer(name: String) {
        val db = Firebase.firestore
        db.collection("deudores").get().addOnSuccessListener { result ->
            var debtorEsxist = false
            for (document in result) {
                val debtor: DebtorServer = document.toObject<DebtorServer>()
                if (name == debtor.name) {
                    debtorEsxist = true
                    with(binding) {
                        debtor.id?.let {
                            db.collection("deudores").document(it).delete().addOnSuccessListener {
                                Toast.makeText(requireContext(), "Deudor eliminado exitosamente.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
            if (!debtorEsxist) {
                Toast.makeText(requireContext(), "El deudor no existe", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteDebtor(name: String) {
        val debtorDao: DebtorDao = DeudoresApp.database.DebtorDao()
        val debtor: Debtor = debtorDao.readDeptor(name)

        if (debtor != null) {

            // crear cuadro de alerta
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setMessage("Desea eliinar a " + debtor.name + ", su deuda es: " + debtor.debt.toString() + "?")
                    setPositiveButton(R.string.accept) { dialog, id ->
                        debtorDao.deleteDebtor(debtor)
                        Toast.makeText(requireContext(), "Deudor eliminado", Toast.LENGTH_SHORT).show()
                        binding.deleteNameEdTx.setText("")
                    }

                    setNegativeButton(R.string.cancel) { dialog, id ->
                    }
                }
                builder.create()
            }
            alertDialog?.show()

        } else {
            Toast.makeText(requireContext(), "No existe", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}