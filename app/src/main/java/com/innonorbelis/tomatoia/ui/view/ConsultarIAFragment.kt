package com.innonorbelis.tomatoia.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.innonorbelis.tomatoia.databinding.FragmentConsultarIABinding
import com.innonorbelis.tomatoia.ui.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class ConsultarIAFragment : Fragment() {

    private var _binding: FragmentConsultarIABinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentConsultarIABinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




