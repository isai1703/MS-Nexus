package com.multiservicios.msnexus.ui.ordenes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
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
import com.multiservicios.msnexus.util.OrdenPdfGenerator
import com.multiservicios.msnexus.viewmodel.ClienteViewModel
import com.multiservicios.msnexus.viewmodel.OrdenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale

class OrdenesFragment : Fragment() {

    private var _binding: FragmentOrdenesBinding? = null

    private val binding
        get() = _binding!!

    private val viewModel: OrdenViewModel by viewModels()

    private val clienteViewModel: ClienteViewModel by viewModels()

    private lateinit var adapter: OrdenAdapter

    private var clientesActuales: List<ClienteEntity> =
        emptyList()

    private var disenoSeleccionadoUri: Uri? = null

    private var tvDisenoSeleccionado: android.widget.TextView? = null

    private val selectorImagen =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {

                disenoSeleccionadoUri = uri

                tvDisenoSeleccionado?.text =
                    "✓ Diseño seleccionado\n${obtenerNombreArchivo(uri)}"

                tvDisenoSeleccionado?.setTextColor(
                    requireContext().getColor(
                        android.R.color.holo_green_dark
                    )
                )

            } else {

                Toast.makeText(
                    requireContext(),
                    "No se seleccionó ninguna imagen",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentOrdenesBinding.inflate(
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

        adapter =
            OrdenAdapter { orden ->
                mostrarOpciones(orden)
            }

        binding.recyclerOrdenes.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerOrdenes.adapter =
            adapter
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

                    clientesActuales =
                        clientes
                }
            }
        }
    }

    private fun mostrarFormulario() {

        val clientes =
            clientesActuales

        if (clientes.isEmpty()) {

            Toast.makeText(
                requireContext(),
                "Primero registra al menos un cliente",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        val nombres =
            clientes.map {
                "${it.numeroCliente} - ${it.nombre}"
            }.toTypedArray()

        MaterialAlertDialogBuilder(
            requireContext()
        )
            .setTitle("Seleccionar cliente")
            .setItems(nombres) { _, posicion ->

                mostrarFormularioOrden(
                    clientes[posicion]
                )
            }
            .setNegativeButton(
                "Cancelar",
                null
            )
            .show()
    }

    private fun mostrarFormularioOrden(
        cliente: ClienteEntity
    ) {

        disenoSeleccionadoUri = null

        val dialogView =
            layoutInflater.inflate(
                R.layout.dialog_orden,
                null
            )

        val etTipoTrabajo =
            dialogView.findViewById<EditText>(
                R.id.etTipoTrabajo
            )

        val etDescripcion =
            dialogView.findViewById<EditText>(
                R.id.etDescripcionTrabajo
            )

        val etFechaProgramada =
            dialogView.findViewById<EditText>(
                R.id.etFechaProgramada
            )

        val etTipoElemento =
            dialogView.findViewById<EditText>(
                R.id.etTipoElemento
            )

        val etAlto =
            dialogView.findViewById<EditText>(
                R.id.etAlto
            )

        val etAncho =
            dialogView.findViewById<EditText>(
                R.id.etAncho
            )

        val etLargo =
            dialogView.findViewById<EditText>(
                R.id.etLargo
            )

        val etCantidad =
            dialogView.findViewById<EditText>(
                R.id.etCantidad
            )

        val etMaterial =
            dialogView.findViewById<EditText>(
                R.id.etMaterial
            )

        val etPerfilCalibre =
            dialogView.findViewById<EditText>(
                R.id.etPerfilCalibre
            )

        val etAcabado =
            dialogView.findViewById<EditText>(
                R.id.etAcabado
            )

        val etColor =
            dialogView.findViewById<EditText>(
                R.id.etColor
            )

        val etObservacionesTecnicas =
            dialogView.findViewById<EditText>(
                R.id.etObservacionesTecnicas
            )

        val btnSeleccionarDiseno =
            dialogView.findViewById<android.widget.Button>(
                R.id.btnSeleccionarDiseno
            )

        tvDisenoSeleccionado =
            dialogView.findViewById(
                R.id.tvDisenoSeleccionado
            )

        val etSubtotal =
            dialogView.findViewById<EditText>(
                R.id.etSubtotal
            )

        val etDescuento =
            dialogView.findViewById<EditText>(
                R.id.etDescuento
            )

        val etIva =
            dialogView.findViewById<EditText>(
                R.id.etIva
            )

        etIva.setText("16")

        btnSeleccionarDiseno.setOnClickListener {

            selectorImagen.launch(
                "image/*"
            )
        }

        MaterialAlertDialogBuilder(
            requireContext()
        )
            .setTitle("Nueva orden")
            .setMessage(
                "${cliente.numeroCliente} • ${cliente.nombre}"
            )
            .setView(dialogView)
            .setNegativeButton(
                "Cancelar",
                null
            )
            .setPositiveButton(
                "Crear orden"
            ) { _, _ ->

                val subtotal =
                    etSubtotal.text
                        .toString()
                        .toDoubleOrNull()
                        ?: 0.0

                val descuento =
                    etDescuento.text
                        .toString()
                        .toDoubleOrNull()
                        ?: 0.0

                val iva =
                    etIva.text
                        .toString()
                        .toDoubleOrNull()
                        ?: 0.0

                val alto =
                    etAlto.text
                        .toString()
                        .toDoubleOrNull()
                        ?: 0.0

                val ancho =
                    etAncho.text
                        .toString()
                        .toDoubleOrNull()
                        ?: 0.0

                val largo =
                    etLargo.text
                        .toString()
                        .toDoubleOrNull()
                        ?: 0.0

                val cantidad =
                    etCantidad.text
                        .toString()
                        .toIntOrNull()
                        ?: 0

                viewModel.crearOrden(

                    numeroCliente =
                        cliente.numeroCliente,

                    nombreCliente =
                        cliente.nombre,

                    empresa =
                        cliente.empresa,

                    telefono =
                        cliente.telefono,

                    correo =
                        cliente.correo,

                    direccion =
                        cliente.direccion,

                    tipoTrabajo =
                        etTipoTrabajo.text.toString(),

                    descripcionTrabajo =
                        etDescripcion.text.toString(),

                    fechaProgramada =
                        etFechaProgramada.text.toString(),

                    subtotal =
                        subtotal,

                    descuento =
                        descuento,

                    ivaPorcentaje =
                        iva,

                    tipoElemento =
                        etTipoElemento.text.toString(),

                    alto =
                        alto,

                    ancho =
                        ancho,

                    largo =
                        largo,

                    cantidad =
                        cantidad,

                    material =
                        etMaterial.text.toString(),

                    perfilCalibre =
                        etPerfilCalibre.text.toString(),

                    acabado =
                        etAcabado.text.toString(),

                    color =
                        etColor.text.toString(),

                    observacionesTecnicas =
                        etObservacionesTecnicas.text.toString(),

                    disenoUri =
                        disenoSeleccionadoUri
                            ?.toString()
                            ?: ""

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

    private fun obtenerNombreArchivo(
        uri: Uri
    ): String {

        var nombre =
            "Imagen seleccionada"

        try {

            val cursor =
                requireContext()
                    .contentResolver
                    .query(
                        uri,
                        arrayOf(
                            android.provider.OpenableColumns.DISPLAY_NAME
                        ),
                        null,
                        null,
                        null
                    )

            cursor?.use {

                if (it.moveToFirst()) {

                    val indice =
                        it.getColumnIndex(
                            android.provider.OpenableColumns.DISPLAY_NAME
                        )

                    if (indice >= 0) {

                        nombre =
                            it.getString(indice)
                    }
                }
            }

        } catch (_: Exception) {
        }

        return nombre
    }

    private fun mostrarOpciones(
        orden: OrdenEntity
    ) {

        MaterialAlertDialogBuilder(
            requireContext()
        )
            .setTitle(orden.folio)
            .setItems(
                arrayOf(
                    "Cliente: ${orden.nombreCliente}",
                    "Trabajo: ${orden.tipoTrabajo}",
                    "Estado: ${orden.estado}",
                    "Total: $${
                        String.format(
                            Locale.getDefault(),
                            "%.2f",
                            orden.total
                        )
                    }",
                    "Cambiar estatus",
                    "Generar PDF",
                    "Eliminar orden"
                )
            ) { _, opcion ->

                when (opcion) {

                    4 ->
                        mostrarEstados(orden)

                    5 ->
                        generarPdf(orden)

                    6 ->
                        confirmarEliminacion(orden)
                }
            }
            .show()
    }

    private fun mostrarEstados(
        orden: OrdenEntity
    ) {

        val estados =
            arrayOf(
                "Pendiente",
                "Autorizada",
                "En proceso",
                "Finalizada",
                "Cancelada"
            )

        MaterialAlertDialogBuilder(
            requireContext()
        )
            .setTitle(
                "Estatus de ${orden.folio}"
            )
            .setSingleChoiceItems(
                estados,
                estados.indexOf(orden.estado)
            ) { dialog, posicion ->

                val nuevoEstado =
                    estados[posicion]

                viewModel.cambiarEstado(
                    orden,
                    nuevoEstado
                ) { correcto ->

                    Toast.makeText(
                        requireContext(),
                        if (correcto) {
                            "Estatus actualizado: $nuevoEstado"
                        } else {
                            "No se pudo actualizar el estatus"
                        },
                        Toast.LENGTH_SHORT
                    ).show()
                }

                dialog.dismiss()
            }
            .setNegativeButton(
                "Cancelar",
                null
            )
            .show()
    }

    private fun generarPdf(
        orden: OrdenEntity
    ) {

        Toast.makeText(
            requireContext(),
            "Generando PDF...",
            Toast.LENGTH_SHORT
        ).show()

        viewLifecycleOwner.lifecycleScope.launch {

            val resultado =
                withContext(Dispatchers.IO) {

                    OrdenPdfGenerator.generar(
                        requireContext(),
                        orden
                    )
                }

            if (resultado != null) {

                viewModel.marcarPdfGenerado(
                    orden
                )

                Toast.makeText(
                    requireContext(),
                    "PDF generado en Descargas/MS Nexus",
                    Toast.LENGTH_LONG
                ).show()

                abrirPdf(resultado)

            } else {

                Toast.makeText(
                    requireContext(),
                    "No se pudo generar el PDF",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun abrirPdf(
        resultado: String
    ) {

        try {

            val uri: Uri

            if (
                resultado.startsWith(
                    "content://"
                )
            ) {

                uri =
                    Uri.parse(resultado)

            } else {

                val file =
                    File(resultado)

                uri =
                    FileProvider.getUriForFile(
                        requireContext(),
                        "${requireContext().packageName}.fileprovider",
                        file
                    )
            }

            val intent =
                Intent(
                    Intent.ACTION_VIEW
                ).apply {

                    setDataAndType(
                        uri,
                        "application/pdf"
                    )

                    addFlags(
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }

            startActivity(intent)

        } catch (_: Exception) {

            Toast.makeText(
                requireContext(),
                "PDF guardado. No hay una aplicación para abrirlo.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun confirmarEliminacion(
        orden: OrdenEntity
    ) {

        MaterialAlertDialogBuilder(
            requireContext()
        )
            .setTitle("Eliminar orden")
            .setMessage(
                "¿Deseas eliminar ${orden.folio}?"
            )
            .setNegativeButton(
                "Cancelar",
                null
            )
            .setPositiveButton(
                "Eliminar"
            ) { _, _ ->

                viewModel.eliminarOrden(
                    orden
                )

                Toast.makeText(
                    requireContext(),
                    "Orden eliminada",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .show()
    }

    override fun onDestroyView() {

        tvDisenoSeleccionado = null

        super.onDestroyView()

        _binding = null
    }
}
