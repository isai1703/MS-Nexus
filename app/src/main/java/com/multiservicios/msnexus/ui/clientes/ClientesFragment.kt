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

        val dialogView = layoutInflater.inflate(
            R.layout.dialog_cliente,
            null
        )

        val etNombre =
            dialogView.findViewById<EditText>(R.id.etNombre)

        val etEmpresa =
            dialogView.findViewById<EditText>(R.id.etEmpresa)

        val etTelefono =
            dialogView.findViewById<EditText>(R.id.etTelefono)

        val etCorreo =
            dialogView.findViewById<EditText>(R.id.etCorreo)

        val etDireccion =
            dialogView.findViewById<EditText>(R.id.etDireccion)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nuevo cliente")
            .setView(dialogView)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Guardar") { _, _ ->

                viewModel.crearCliente(
                    nombre = etNombre.text.toString(),
                    empresa = etEmpresa.text.toString(),
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
            }
            .show()
    }

    private fun mostrarOpciones(cliente: ClienteEntity) {

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
                    "Teléfono: ${
                        cliente.telefono.ifBlank {
                            "Sin teléfono"
                        }
                    }",
                    "Eliminar cliente"
                )
            ) { _, opcion ->

                if (opcion == 3) {

                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Eliminar cliente")
                        .setMessage(
                            "¿Deseas eliminar a ${cliente.nombre}?"
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
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
