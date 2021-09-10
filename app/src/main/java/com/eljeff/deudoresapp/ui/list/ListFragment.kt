package com.eljeff.deudoresapp.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eljeff.deudoresapp.DeudoresApp
import com.eljeff.deudoresapp.data.local.dao.DebtorDao
import com.eljeff.deudoresapp.data.local.entities.Debtor
import com.eljeff.deudoresapp.data.server.DebtorServer
import com.eljeff.deudoresapp.databinding.FragmentListBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

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

        // carga la información de la base de datos

        //loadFromLocal()

        loadFromServer()

        return root
    }

    private fun loadFromServer() {
        val db = Firebase.firestore
        db.collection("deudores").get().addOnSuccessListener { result ->

            var listDebtors: MutableList<DebtorServer> = arrayListOf()

            for (document in result){
                listDebtors.add(document.toObject<DebtorServer>())
            }
            debtorsAdapter.appendItems(listDebtors)
        }
    }

    private fun loadFromLocal() {
        val deptorDao: DebtorDao = DeudoresApp.database.DebtorDao()
        val listDebtors: MutableList<Debtor> = deptorDao.getDebtors()
        // se agregan deudores a la lista
        //debtorsAdapter.appendItems(listDebtors)
    }

    private fun onDebtorItemClicked(debtor: DebtorServer) {
        // me leva al fragmento detail y le manda la info del debtor
        //findNavController().navigate(ListFragmentDirections.actionNavigationListToDetailFragment(debtor = debtor))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}