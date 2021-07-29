package com.mateuslima.listaafazeres.ui.tarefas

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mateuslima.listaafazeres.R
import com.mateuslima.listaafazeres.adapter.ItemTouchHelperAdapter
import com.mateuslima.listaafazeres.adapter.TarefasAdapter
import com.mateuslima.listaafazeres.adapter.TarefasAdapter.*
import com.mateuslima.listaafazeres.data.db.model.Tarefa
import com.mateuslima.listaafazeres.data.db.preference.PreferencesManager
import com.mateuslima.listaafazeres.databinding.FragmentTarefasBinding
import com.mateuslima.listaafazeres.ui.tarefas.TarefasViewModel.TarefaEvento
import com.mateuslima.listaafazeres.ui.tarefas.TarefasViewModel.TarefaEvento.*
import com.mateuslima.listaafazeres.util.addOnQueryTextChange
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TarefasFragment : Fragment(R.layout.fragment_tarefas), OnItemClickListener {

    var _binding: FragmentTarefasBinding? = null
    val binding get() = _binding!!
    val viewModel: TarefasViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTarefasBinding.bind(view)
        setHasOptionsMenu(true)

        binding.fab.setOnClickListener {
            findNavController().navigate(TarefasFragmentDirections.actionToAddTarefaFragment()) }

        val adapter = TarefasAdapter(this)

        val callback = ItemTouchHelperAdapter(adapter)
        val itemTouchHelper = ItemTouchHelper(callback)
        adapter.setTouchHelper(itemTouchHelper)
        itemTouchHelper.attachToRecyclerView(binding.recyclerTarefas)

        binding.recyclerTarefas.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
            it.setHasFixedSize(true)
        }


        viewModel.getListaTarefa().observe(viewLifecycleOwner){ list ->
            adapter.submitList(list)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.getTarefaEvento().collect { event ->
                when (event){
                    is MostrarDesfazerExclusao -> snackBarDesfazerExclusao(event.tarefa)
                }
            }
        }





    }

    private fun snackBarDesfazerExclusao(tarefa: Tarefa){
        Snackbar.make(requireView(), "Tarefa excluida", Snackbar.LENGTH_LONG)
            .setAction("Desfazer"){
                viewModel.desfazerExclusao(tarefa)
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_tarefas, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val menuItem = menu.findItem(R.id.action_pesquisa)
        val searchView = menuItem.actionView as SearchView

        searchView.addOnQueryTextChange { pesquisa ->
            viewModel.pesquisarTarefa(pesquisa)
        }
        val menuHide = menu.findItem(R.id.action_esconder)

        lifecycleScope.launch(IO) {
            menuHide.isChecked = viewModel.marcarTarefaMenu()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_esconder -> {
                item.isChecked = !item.isChecked
                viewModel.esconderCompletos(item.isChecked)
            }
            R.id.action_sort_by_name -> viewModel.organizarPorNome()
            R.id.action_sort_by_date -> viewModel.organizarPorData()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCheckBoxSelected(tarefa: Tarefa, checked: Boolean) {
        viewModel.atualizarTarefa(tarefa, checked)
    }

    override fun onTarefaClicked(tarefa: Tarefa) {
        Toast.makeText(context, tarefa.nome, Toast.LENGTH_SHORT).show()
    }

    override fun onTarefaMoved(listaTarefa: List<Tarefa>) {

    }

    override fun onTarefaSwiped(tarefa: Tarefa) {
        viewModel.swipeTarefa(tarefa)
    }
}