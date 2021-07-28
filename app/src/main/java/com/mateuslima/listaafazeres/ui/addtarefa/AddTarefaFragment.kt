package com.mateuslima.listaafazeres.ui.addtarefa

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mateuslima.listaafazeres.R
import com.mateuslima.listaafazeres.databinding.FragmentAddTarefaBinding

class AddTarefaFragment : Fragment(R.layout.fragment_add_tarefa) {

    private var _binding: FragmentAddTarefaBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddTarefaBinding.bind(view)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}