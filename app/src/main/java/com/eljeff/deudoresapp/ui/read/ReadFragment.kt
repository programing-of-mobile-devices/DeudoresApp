package com.eljeff.deudoresapp.ui.read

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eljeff.deudoresapp.DeudoresApp
import com.eljeff.deudoresapp.R
import com.eljeff.deudoresapp.data.dao.DebtorDao
import com.eljeff.deudoresapp.data.entities.Debtor
import com.eljeff.deudoresapp.databinding.FragmentReadBinding

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
            readDebtors(binding.searchNameEdTx.text.toString())
        }


        return root
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