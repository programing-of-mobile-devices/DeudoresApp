package com.eljeff.deudoresapp.ui.update

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.eljeff.deudoresapp.DeudoresApp
import com.eljeff.deudoresapp.R
import com.eljeff.deudoresapp.data.dao.DebtorDao
import com.eljeff.deudoresapp.data.entities.Debtor
import com.eljeff.deudoresapp.databinding.FragmentCreateBinding
import com.eljeff.deudoresapp.databinding.FragmentUpdateBinding

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
        var idDebtor = 0

        binding.updateButton.setOnClickListener {

            var debtorDao : DebtorDao = DeudoresApp.database.DebtorDao()
            var name = binding.updateNameEdTx.text.toString()


            if(isSearching){ //actualizando
                var debtor: Debtor = debtorDao.readDeptor(name)
                if(debtor != null){
                    idDebtor = debtor.id
                    binding.updateDebtEdTx.setText(debtor.debt.toString())
                    binding.updatePhoneEdTx.setText(debtor.phone)

                    binding.updateButton.text = getString(R.string.title_update)

                    isSearching = false

                }else{
                    Toast.makeText(requireContext(), "No existe", Toast.LENGTH_SHORT).show()
                    cleanWidgets()
                }
            }else{ // actualizando
                val debtor = Debtor(
                    id = idDebtor,
                    name = binding.updateNameEdTx.text.toString(),
                    debt = binding.updateDebtEdTx.text.toString().toLong(),
                    phone = binding.updatePhoneEdTx.text.toString()
                )
                debtorDao.updateDebtor(debtor)

                binding.updateButton.text = getString(R.string.search)
                isSearching = true
                cleanWidgets()
                Toast.makeText(requireContext(), "Deudor actualizado", Toast.LENGTH_SHORT).show()
            }
        }
        return root
    }

    private fun cleanWidgets() {
        with(binding){
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