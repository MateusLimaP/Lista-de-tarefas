package com.mateuslima.listaafazeres.util

sealed class Resource<T>(
    val data: T? = null,
    val msg : String? = null
) {
    class Sucesso<T>(data: T) : Resource<T>(data)
    class Carregando<T>() : Resource<T>()
    class Erro<T>(msg: String) : Resource<T>(msg = msg)
}