package com.mateuslima.listaafazeres.util

import androidx.appcompat.widget.SearchView
// funçao normal geraria varios objetos sobrecarregando a memory heap
// Funçoes extensivas (Extension Functions)
inline fun SearchView.addOnQueryTextChange(crossinline txt: (String) -> Unit){
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
           return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            txt(newText.orEmpty())
            return true
        }

    })
}