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
            mostrarFormulario()
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

        mostrarDialogoCliente(
            cliente = null
        )
    }

    private fun editarCliente(cliente: ClienteEntity) {

        mostrarDialogoCliente(
            cliente = cliente
        )
    }

    private fun mostrarDialogoCliente(cliente: ClienteEntity?) {

        val dialogView = layoutInflater.inflate(
            R.layout.dialog_cliente,
            null
        )

        val etNombre =
            dialogView.findViewById<EditText>(R.id.etNombre)

        val etEmpresa =
            dialogView.findViewById<EditText>(R.id.etEmpresa)

        val etRfc =
            dialogView.findViewById<EditText>(R.id.etRfc)

        val etRazonSocial =
            dialogView.findViewById<EditText>(R.id.etRazonSocial)

        val etCodigoPostalFiscal =
            dialogView.findViewById<EditText>(
                R.id.etCodigoPostalFiscal
            )

        val etRegimenFiscal =
            dialogView.findViewById<EditText>(
                R.id.etRegimenFiscal
            )

        val etTelefono =
            dialogView.findViewById<EditText>(R.id.etTelefono)

        val etCorreo =
            dialogView.findViewById<EditText>(R.id.etCorreo)

        val etDireccion =
            dialogView.findViewById<EditText>(R.id.etDireccion)

        if (cliente != null) {

            etNombre.setText(cliente.nombre)
            etEmpresa.setText(cliente.empresa)
            etRfc.setText(cliente.rfc)
            etRazonSocial.setText(cliente.razonSocial)
            etCodigoPostalFiscal.setText(cliente.codigoPostalFiscal)
            etRegimenFiscal.setText(cliente.regimenFiscal)
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
            .setNegativeButton("Cancelar", null)
            .setPositiveButton(
                if (cliente == null) "Guardar" else "Actualizar"
            ) { _, _ ->

                if (cliente == null) {

                    viewModel.crearCliente(
                        nombre = etNombre.text.toString(),
                        empresa = etEmpresa.text.toString(),
                        rfc = etRfc.text.toString(),
                        razonSocial = etRazonSocial.text.toString(),
                        codigoPostalFiscal =
                            etCodigoPostalFiscal.text.toString(),
                        regimenFiscal =
                            etRegimenFiscal.text.toString(),
                        telefono = etTelefono.text.toString(),
                        correo = etCorreo.text.toString(),
                        direccion = etDireccion.text.toString()
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
                        nombre = etNombre.text.toString(),
                        empresa = etEmpresa.text.toString(),
                        rfc = etRfc.text.toString(),
                        razonSocial = etRazonSocial.text.toString(),
                        codigoPostalFiscal =
                            etCodigoPostalFiscal.text.toString(),
                        regimenFiscal =
                            etRegimenFiscal.text.toString(),
                        telefono = etTelefono.text.toString(),
                        correo = etCorreo.text.toString(),
                        direccion = etDireccion.text.toString()
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

    private fun mostrarOpciones(cliente: ClienteEntity) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(cliente.nombre)
            .setItems(
                arrayOf(
                    "Número: ${cliente.numeroCliente}",
                    "Editar cliente",
                    "Ver datos fiscales",
                    "Eliminar cliente"
                )
            ) { _, opcion ->

                when (opcion) {

                    1 -> editarCliente(cliente)

                    2 -> mostrarDatosFiscales(cliente)

                    3 -> confirmarEliminar(cliente)
                }
            }
            .show()
    }

    private fun mostrarDatosFiscales(cliente: ClienteEntity) {

        val rfc =
            cliente.rfc.ifBlank { "No registrado" }

        val razonSocial =
            cliente.razonSocial.ifBlank { "No registrada" }

        val codigoPostal =
            cliente.codigoPostalFiscal.ifBlank { "No registrado" }

        val regimen =
            cliente.regimenFiscal.ifBlank { "No registrado" }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Datos fiscales")
            .setMessage(
                "Cliente: ${cliente.numeroCliente}\n\n" +
                    "RFC: $rfc\n" +
                    "Razón social: $razonSocial\n" +
                    "Código postal fiscal: $codigoPostal\n" +
                    "Régimen fiscal: $regimen\n\n" +
                    "Estos datos se almacenan únicamente " +
                    "como información del cliente."
            )
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun confirmarEliminar(cliente: ClienteEntity) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar cliente")
            .setMessage(
                "¿Deseas eliminar a ${cliente.nombre} " +
                    "(${cliente.numeroCliente})?"
            )
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->

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
