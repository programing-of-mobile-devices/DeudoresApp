package com.eljeff.deudoresapp.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eljeff.deudoresapp.DeudoresApp
import com.eljeff.deudoresapp.data.dao.DebtorDao
import com.eljeff.deudoresapp.data.entities.Debtor
import com.eljeff.deudoresapp.databinding.FragmentCreateBinding
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
                createDebtor(name, phone, debt)
            }
        }
        return root
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