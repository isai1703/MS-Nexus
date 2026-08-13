package com.multiservicios.msnexus.ui.clientes

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
import com.multiservicios.msnexus.databinding.FragmentClientesBinding
import com.multiservicios.msnexus.viewmodel.ClienteViewModel
import kotlinx.coroutines.launch

class ClientesFragment : Fragment() {

    private var _binding: FragmentClientesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClienteViewModel by viewModels()

    private lateinit var adapter: ClienteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentClientesBinding.inflate(
            inflater,
            container,
            false
        )

        configurarLista()
        configurarEventos()
        observarClientes()

        return binding.root
    }

    private fun configurarLista() {

        adapter = ClienteAdapter { cliente ->
            mostrarOpciones(cliente)
        }

        binding.recyclerClientes.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerClientes.adapter = adapter
    }

    private fun configurarEventos() {

        binding.btnNuevoCliente.setOnClickListener {

            try {
                mostrarFormulario()
            } catch (e: Exception) {

                Toast.makeText(
                    requireContext(),
                    "Error al abrir formulario: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun observarClientes() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.clientes.collect { clientes ->

                    adapter.submitList(clientes)

                    binding.tvSinClientes.visibility =
                        if (clientes.isEmpty()) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                }
            }
        }
    }

    private fun mostrarFormulario() {
        mostrarDialogoCliente(null)
    }

    private fun editarCliente(cliente: ClienteEntity) {
        mostrarDialogoCliente(cliente)
    }

    private fun mostrarDialogoCliente(
        cliente: ClienteEntity?
    ) {

        val dialogView = layoutInflater.inflate(
            R.layout.dialog_cliente,
            null
        )

        val etNombre =
            dialogView.findViewById<EditText>(
                R.id.etNombre
            )

        val etEmpresa =
            dialogView.findViewById<EditText>(
                R.id.etEmpresa
            )

        val etRfc =
            dialogView.findViewById<EditText>(
                R.id.etRfc
            )

        val etRazonSocial =
            dialogView.findViewById<EditText>(
                R.id.etRazonSocial
            )

        val etRegimenFiscal =
            dialogView.findViewById<EditText>(
                R.id.etRegimenFiscal
            )

        val etCodigoPostalFiscal =
            dialogView.findViewById<EditText>(
                R.id.etCodigoPostalFiscal
            )

        val etTelefono =
            dialogView.findViewById<EditText>(
                R.id.etTelefono
            )

        val etCorreo =
            dialogView.findViewById<EditText>(
                R.id.etCorreo
            )

        val etDireccion =
            dialogView.findViewById<EditText>(
                R.id.etDireccion
            )

        if (cliente != null) {

            etNombre.setText(cliente.nombre)
            etEmpresa.setText(cliente.empresa)
            etRfc.setText(cliente.rfc)
            etRazonSocial.setText(cliente.razonSocial)
            etRegimenFiscal.setText(cliente.regimenFiscal)
            etCodigoPostalFiscal.setText(
                cliente.codigoPostalFiscal
            )
            etTelefono.setText(cliente.telefono)
            etCorreo.setText(cliente.correo)
            etDireccion.setText(cliente.direccion)
        }

        val titulo =
            if (cliente == null) {
                "Nuevo cliente"
            } else {
                "Editar ${cliente.numeroCliente}"
            }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(titulo)
            .setView(dialogView)
            .setNegativeButton(
                "Cancelar",
                null
            )
            .setPositiveButton(
                if (cliente == null) {
                    "Guardar"
                } else {
                    "Actualizar"
                }
            ) { _, _ ->

                if (cliente == null) {

                    viewModel.crearCliente(

                        nombre =
                            etNombre.text.toString(),

                        empresa =
                            etEmpresa.text.toString(),

                        rfc =
                            etRfc.text.toString(),

                        razonSocial =
                            etRazonSocial.text.toString(),

                        regimenFiscal =
                            etRegimenFiscal.text.toString(),

                        codigoPostalFiscal =
                            etCodigoPostalFiscal.text.toString(),

                        telefono =
                            etTelefono.text.toString(),

                        correo =
                            etCorreo.text.toString(),

                        direccion =
                            etDireccion.text.toString()

                    ) { correcto ->

                        if (correcto) {

                            Toast.makeText(
                                requireContext(),
                                "Cliente registrado correctamente",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {

                            Toast.makeText(
                                requireContext(),
                                "El nombre del cliente es obligatorio",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                } else {

                    viewModel.actualizarCliente(

                        cliente = cliente,

                        nombre =
                            etNombre.text.toString(),

                        empresa =
                            etEmpresa.text.toString(),

                        rfc =
                            etRfc.text.toString(),

                        razonSocial =
                            etRazonSocial.text.toString(),

                        regimenFiscal =
                            etRegimenFiscal.text.toString(),

                        codigoPostalFiscal =
                            etCodigoPostalFiscal.text.toString(),

                        telefono =
                            etTelefono.text.toString(),

                        correo =
                            etCorreo.text.toString(),

                        direccion =
                            etDireccion.text.toString()

                    ) { correcto ->

                        if (correcto) {

                            Toast.makeText(
                                requireContext(),
                                "Cliente actualizado correctamente",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {

                            Toast.makeText(
                                requireContext(),
                                "El nombre del cliente es obligatorio",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            .show()
    }

    private fun mostrarOpciones(
        cliente: ClienteEntity
    ) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(cliente.nombre)
            .setItems(
                arrayOf(
                    "Número: ${cliente.numeroCliente}",

                    "Empresa: ${
                        cliente.empresa.ifBlank {
                            "Particular"
                        }
                    }",

                    "RFC: ${
                        cliente.rfc.ifBlank {
                            "No registrado"
                        }
                    }",

                    "Razón social: ${
                        cliente.razonSocial.ifBlank {
                            "No registrada"
                        }
                    }",

                    "Régimen fiscal: ${
                        cliente.regimenFiscal.ifBlank {
                            "No registrado"
                        }
                    }",

                    "Código postal fiscal: ${
                        cliente.codigoPostalFiscal.ifBlank {
                            "No registrado"
                        }
                    }",

                    "Teléfono: ${
                        cliente.telefono.ifBlank {
                            "Sin teléfono"
                        }
                    }",

                    "Correo: ${
                        cliente.correo.ifBlank {
                            "Sin correo"
                        }
                    }",

                    "Dirección: ${
                        cliente.direccion.ifBlank {
                            "Sin dirección"
                        }
                    }",

                    "Editar cliente",
                    "Eliminar cliente"
                )
            ) { _, opcion ->

                when (opcion) {

                    9 -> {
                        editarCliente(cliente)
                    }

                    10 -> {
                        confirmarEliminacion(cliente)
                    }
                }
            }
            .show()
    }

    private fun confirmarEliminacion(
        cliente: ClienteEntity
    ) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar cliente")
            .setMessage(
                "¿Deseas eliminar a ${cliente.nombre}?"
            )
            .setNegativeButton(
                "Cancelar",
                null
            )
            .setPositiveButton(
                "Eliminar"
            ) { _, _ ->

                viewModel.eliminarCliente(cliente)

                Toast.makeText(
                    requireContext(),
                    "Cliente eliminado",
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
