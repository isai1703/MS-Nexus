package com.multiservicios.msnexus.ui.ordenes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.multiservicios.msnexus.data.local.OrdenEntity
import com.multiservicios.msnexus.databinding.ItemOrdenBinding

class OrdenAdapter(
    private val onClick: (OrdenEntity) -> Unit
) : RecyclerView.Adapter<OrdenAdapter.ViewHolder>() {

    private val items = mutableListOf<OrdenEntity>()

    fun submitList(nuevos: List<OrdenEntity>) {
        items.clear()
        items.addAll(nuevos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = ItemOrdenBinding.inflate(
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
        private val binding: ItemOrdenBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(orden: OrdenEntity) {

            binding.tvFolio.text = orden.folio

            binding.tvCliente.text =
                if (orden.empresa.isBlank()) {
                    orden.nombreCliente
                } else {
                    "${orden.nombreCliente} · ${orden.empresa}"
                }

            binding.tvTrabajo.text =
                orden.tipoTrabajo.ifBlank {
                    "Sin tipo de trabajo"
                }

            binding.tvEstado.text =
                "Estado: ${orden.estado}"

            binding.tvTotal.text =
                "Total: $${"%.2f".format(orden.total)}"

            binding.root.setOnClickListener {
                onClick(orden)
            }
        }
    }
}
