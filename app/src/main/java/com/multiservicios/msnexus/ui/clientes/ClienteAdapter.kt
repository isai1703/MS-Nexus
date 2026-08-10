package com.multiservicios.msnexus.ui.clientes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.multiservicios.msnexus.data.local.ClienteEntity
import com.multiservicios.msnexus.databinding.ItemClienteBinding

class ClienteAdapter(
    private val onClick: (ClienteEntity) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ViewHolder>() {

    private val items = mutableListOf<ClienteEntity>()

    fun submitList(nuevos: List<ClienteEntity>) {
        items.clear()
        items.addAll(nuevos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = ItemClienteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        private val binding: ItemClienteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cliente: ClienteEntity) {

            binding.tvNumero.text = cliente.numeroCliente
            binding.tvNombre.text = cliente.nombre

            binding.tvEmpresa.text =
                cliente.empresa.ifBlank { "Cliente particular" }

            binding.root.setOnClickListener {
                onClick(cliente)
            }
        }
    }
}
