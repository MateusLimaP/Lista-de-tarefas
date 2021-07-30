package com.mateuslima.listaafazeres.ui.addtarefa

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.mateuslima.listaafazeres.R
import com.mateuslima.listaafazeres.databinding.FragmentAddTarefaBinding
import com.mateuslima.listaafazeres.ui.addtarefa.AddTarefaViewModel.AddTarefaEvent.*
import com.mateuslima.listaafazeres.ui.main.MainActivity
import com.mateuslima.listaafazeres.util.TIPO_ADICIONAR_TAREFA
import com.mateuslima.listaafazeres.util.TIPO_EDITAR_TAREFA
import com.mateuslima.listaafazeres.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddTarefaFragment : Fragment(R.layout.fragment_add_tarefa) {

    private var _binding: FragmentAddTarefaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddTarefaViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddTarefaBinding.bind(view)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = viewModel.tituloToolbar()

        // editar tarefa
        binding.apply {//
            editNomeTarefa.setText(viewModel.nomeTarefa())
            checkBoxImportante.isChecked = viewModel.tarefaImportante()
            checkBoxImportante.jumpDrawablesToCurrentState()
            textDataCriada.isVisible = viewModel.dataCriadaVisivel()
            textDataCriada.text = viewModel.dataCriada()

            fabSalvar.setOnClickListener {//
                val nome = editNomeTarefa.text.toString()
                val importante = checkBoxImportante.isChecked
                viewModel.salvarTarefa(nome, importante)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.tarefaEvent.collect { event ->
                when (event){
                    is MostrarNomeTarefaVazio -> mostrarSnackBar(event.msg)
                    is NavegarVoltaComResultado -> irParaTelaPrincipal(event.resultado)
                }.exhaustive
            }
        }

    }

    private fun mostrarSnackBar(msg: String){
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT).show()
    }

    private fun irParaTelaPrincipal(resultado: String){
        setFragmentResult("addTarefaFragment", bundleOf("tipoSalvo" to resultado))
        findNavController().popBackStack()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}