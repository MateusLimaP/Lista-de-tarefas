package com.mateuslima.listaafazeres.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.*
import android.widget.CompoundButton
import android.widget.PopupMenu
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mateuslima.listaafazeres.R
import com.mateuslima.listaafazeres.data.db.model.Tarefa
import com.mateuslima.listaafazeres.databinding.ContainerTarefasBinding
import javax.inject.Inject

class TarefasAdapter(
   private val listener: OnItemClickListener
) : ListAdapter<Tarefa, TarefasAdapter.MyViewHolder>(Companion), ItemTouchHelperAdapter.ItemTouchListener {


   private lateinit var touchHelper: ItemTouchHelper
   fun setTouchHelper(itemTouchHelper: ItemTouchHelper){this.touchHelper = itemTouchHelper}
   private lateinit var myMovedList : MutableList<Tarefa>


   companion object: DiffUtil.ItemCallback<Tarefa>(){
       override fun areItemsTheSame(oldItem: Tarefa, newItem: Tarefa): Boolean {
           return oldItem.id == newItem.id
       }

       override fun areContentsTheSame(oldItem: Tarefa, newItem: Tarefa): Boolean {
           return oldItem == newItem
       }

   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ContainerTarefasBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tarefa = getItem(position)
        holder.bind(tarefa)

    }


    inner class MyViewHolder(val b: ContainerTarefasBinding) : RecyclerView.ViewHolder(b.root){
        init {
            b.checkBox.setOnCheckedChangeListener{button,checked->
                if (button.isPressed) {
                    listener.onCheckBoxSelected(getItem(adapterPosition), checked)
                }
            }

            b.root.setOnClickListener {  listener.onTarefaClicked(getItem(adapterPosition)) }
            b.root.setOnLongClickListener { touchHelper.startDrag(this)
            true}


        }

        fun bind(tarefa: Tarefa){
            b.viewPrioridade.isVisible = tarefa.importante
            b.textTarefa.text = tarefa.nome
            if (tarefa.completada){
                b.checkBox.isChecked = true
                b.textTarefa.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }else{
                b.checkBox.isChecked = false
                b.textTarefa.paintFlags = 0
            }

        }

    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {

        if (!::myMovedList.isInitialized) {
            myMovedList = mutableListOf()
            myMovedList.addAll(currentList)
        }
        val fromTarefa = myMovedList[fromPosition]
        myMovedList.remove(fromTarefa)
        myMovedList.add(toPosition, fromTarefa)
        notifyItemMoved(fromPosition, toPosition)

        listener.onTarefaMoved(myMovedList)
    }

    override fun onItemSwipe(position: Int) {
        listener.onTarefaSwiped(getItem(position))
    }

    interface OnItemClickListener{
        fun onCheckBoxSelected(tarefa: Tarefa, checked: Boolean)
        fun onTarefaClicked(tarefa: Tarefa)
        fun onTarefaMoved(listaTarefa: List<Tarefa>)
        fun onTarefaSwiped(tarefa: Tarefa)
    }



}