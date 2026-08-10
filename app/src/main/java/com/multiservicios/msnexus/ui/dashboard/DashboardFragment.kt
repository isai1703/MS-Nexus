package com.multiservicios.msnexus.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.multiservicios.msnexus.databinding.FragmentDashboardBinding
import com.multiservicios.msnexus.ui.clientes.ClientesFragment
import com.multiservicios.msnexus.viewmodel.DashboardViewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel = DashboardViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(
            inflater,
            container,
            false
        )

        binding.tvDashboardTitle.text = viewModel.appName
        binding.tvCompany.text = viewModel.companyName

        binding.btnClientes.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(
                    com.multiservicios.msnexus.R.id.main_container,
                    ClientesFragment()
                )
                .addToBackStack(null)
                .commit()
        }

        binding.btnInventory.setOnClickListener {

            // Inventario se conectará en la siguiente etapa.
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
