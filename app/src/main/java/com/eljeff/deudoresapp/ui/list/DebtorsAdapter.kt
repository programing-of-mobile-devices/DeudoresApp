package com.eljeff.deudoresapp.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eljeff.deudoresapp.R
import com.eljeff.deudoresapp.data.local.entities.Debtor
import com.eljeff.deudoresapp.data.server.DebtorServer
import com.eljeff.deudoresapp.databinding.CardViewDebtorsItemBinding

class DebtorsAdapter(
    private val onItemClicked: (DebtorServer) -> Unit,
): RecyclerView.Adapter<DebtorsAdapter.ViewHolder>() {

    private var ListDebtor: MutableList<DebtorServer> = mutableListOf()

    // me dice cual car view o layout quiero pintar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_debtors_item, parent, false)

        return ViewHolder(view)
    }

    // se ejecuta para cada uno de los items de la lista
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ListDebtor[position])

        // con click al card view le manda el deudor a onItemClicked
        holder.itemView.setOnClickListener { onItemClicked(ListDebtor[position]) }
    }

    override fun getItemCount(): Int {
        return ListDebtor.size
    }

    fun appendItems(newItems: MutableList<DebtorServer>){
        ListDebtor.clear()
        ListDebtor.addAll(newItems)
        notifyDataSetChanged()
    }

    // retorna la información de cada deudor
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private val binding = CardViewDebtorsItemBinding.bind(view)

        fun bind(debtor: DebtorServer){
            with(binding){
                nameCardVwTxVw.text = debtor.name
                debtCardVwTxVw.text = debtor.debt.toString()
            }
        }
    }
}