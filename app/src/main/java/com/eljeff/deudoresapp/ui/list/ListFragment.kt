package com.eljeff.deudoresapp.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eljeff.deudoresapp.DeudoresApp
import com.eljeff.deudoresapp.data.dao.DebtorDao
import com.eljeff.deudoresapp.data.entities.Debtor
import com.eljeff.deudoresapp.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private lateinit var listViewModel: ListViewModel
    private var _binding: FragmentListBinding? = null
    private lateinit var debtorsAdapter: DebtorsAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listViewModel =
            ViewModelProvider(this).get(ListViewModel::class.java)

        _binding = FragmentListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
          textView.text = it
        })*/

        // inicializamos el adaptador
        debtorsAdapter = DebtorsAdapter( onItemClicked = { onDebtorItemClicked(it) } )
        // configuramos el recycler view
        binding.debtorsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ListFragment.context)
            adapter = debtorsAdapter
            setHasFixedSize(false)
        }

        val deptorDao: DebtorDao = DeudoresApp.database.DebtorDao()
        val listDebtors: MutableList<Debtor> = deptorDao.getDebtors()

        debtorsAdapter.appendItems(listDebtors)

        return root
    }

    private fun onDebtorItemClicked(debtor: Debtor) {
        // me leva al fragmento detail y le manda la info del debtor
        findNavController().navigate(ListFragmentDirections.actionNavigationListToDetailFragment(debtor = debtor))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}