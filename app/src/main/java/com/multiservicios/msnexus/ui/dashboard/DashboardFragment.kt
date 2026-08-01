package com.multiservicios.msnexus.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.multiservicios.msnexus.databinding.FragmentDashboardBinding
import com.multiservicios.msnexus.viewmodel.DashboardViewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        binding.tvDashboardTitle.text = viewModel.appName
        binding.tvCompany.text = viewModel.companyName

        binding.btnInventory.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Módulo Inventario (próximamente)",
                Toast.LENGTH_SHORT
            ).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
