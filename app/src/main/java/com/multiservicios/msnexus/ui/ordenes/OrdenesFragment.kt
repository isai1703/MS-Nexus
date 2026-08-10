package com.multiservicios.msnexus.ui.ordenes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.multiservicios.msnexus.R
import com.multiservicios.msnexus.data.local.ClienteEntity
import com.multiservicios.msnexus.data.local.OrdenEntity
import com.multiservicios.msnexus.databinding.FragmentOrdenesBinding
import com.multiservicios.msnexus.viewmodel.ClienteViewModel
import com.multiservicios.msnexus.viewmodel.OrdenViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class OrdenesFragment : Fragment() {

    private var _binding: FragmentOrdenesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrdenViewModel by viewModels()
    private val clienteViewModel: ClienteViewModel by viewModels()

    private lateinit var adapter: OrdenAdapter

    private var clientesActuales: List<ClienteEntity> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOrdenesBinding.inflate(
            inflater,
            container,
            false
        )

        configurarLista()
        configurarEventos()
        observarOrdenes()
        observarClientes()

        return binding.root
    }

    private fun configurarLista() {
        adapter = OrdenAdapter { orden ->
            mostrarOpciones(orden)
        }

        binding.recyclerOrdenes.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerOrdenes.adapter = adapter
    }

    private fun configurarEventos() {
        binding.btnNuevaOrden.setOnClickListener {
            mostrarFormulario()
        }
    }

    private fun observarOrdenes() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                viewModel.ordenes.collect { ordenes ->

                    adapter.submitList(ordenes)

                    binding.tvSinOrdenes.visibility =
                        if (ordenes.isEmpty()) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                }
            }
        }
    }

    private fun observarClientes() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                clienteViewModel.clientes.collect { clientes ->
                    clientesActuales = clientes
                }
            }
        }
    }

    private fun mostrarFormulario() {
        val clientes = clientesActuales

        if (clientes.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Primero registra al menos un cliente",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val nombres = clientes.map {
            "${it.numeroCliente} - ${it.nombre}"
        }.toTypedArray()

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Seleccionar cliente")
            .setItems(nombres) { _, posicion ->
                mostrarFormularioOrden(clientes[posicion])
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarFormularioOrden(cliente: ClienteEntity) {
        val dialogView = layoutInflater.inflate(
            R.layout.dialog_orden,
            null
        )

        val etTipoTrabajo =
            dialogView.findViewById<EditText>(R.id.etTipoTrabajo)

        val etDescripcion =
            dialogView.findViewById<EditText>(R.id.etDescripcionTrabajo)

        val etFechaProgramada =
            dialogView.findViewById<EditText>(R.id.etFechaProgramada)

        val etSubtotal =
            dialogView.findViewById<EditText>(R.id.etSubtotal)

        val etDescuento =
            dialogView.findViewById<EditText>(R.id.etDescuento)

        val etIva =
            dialogView.findViewById<EditText>(R.id.etIva)

        etIva.setText("16")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nueva orden")
            .setMessage(
                "${cliente.numeroCliente} • ${cliente.nombre}"
            )
            .setView(dialogView)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Crear orden") { _, _ ->

                val subtotal =
                    etSubtotal.text.toString().toDoubleOrNull() ?: 0.0

                val descuento =
                    etDescuento.text.toString().toDoubleOrNull() ?: 0.0

                val iva =
                    etIva.text.toString().toDoubleOrNull() ?: 0.0

                viewModel.crearOrden(
                    numeroCliente = cliente.numeroCliente,
                    nombreCliente = cliente.nombre,
                    empresa = cliente.empresa,
                    telefono = cliente.telefono,
                    correo = cliente.correo,
                    direccion = cliente.direccion,
                    tipoTrabajo = etTipoTrabajo.text.toString(),
                    descripcionTrabajo = etDescripcion.text.toString(),
                    fechaProgramada = etFechaProgramada.text.toString(),
                    subtotal = subtotal,
                    descuento = descuento,
                    ivaPorcentaje = iva
                ) { correcto ->

                    if (correcto) {
                        Toast.makeText(
                            requireContext(),
                            "Orden creada correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "No se pudo crear la orden",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .show()
    }

    private fun mostrarOpciones(orden: OrdenEntity) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(orden.folio)
            .setItems(
                arrayOf(
                    "Cliente: ${orden.nombreCliente}",
                    "Trabajo: ${orden.tipoTrabajo}",
                    "Estado: ${orden.estado}",
                    "Total: $${String.format(
                        Locale.getDefault(),
                        "%.2f",
                        orden.total
                    )}",
                    "Eliminar orden"
                )
            ) { _, opcion ->
                if (opcion == 4) {
                    confirmarEliminacion(orden)
                }
            }
            .show()
    }

    private fun confirmarEliminacion(orden: OrdenEntity) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar orden")
            .setMessage(
                "¿Deseas eliminar ${orden.folio}?"
            )
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->

                viewModel.eliminarOrden(orden)

                Toast.makeText(
                    requireContext(),
                    "Orden eliminada",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
