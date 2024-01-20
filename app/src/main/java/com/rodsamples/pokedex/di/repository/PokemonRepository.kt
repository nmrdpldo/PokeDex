package com.rodsamples.pokedex.di.repository

import com.rodsamples.pokedex.data.remote.PokeApi
import com.rodsamples.pokedex.data.remote.responses.Pokemon
import com.rodsamples.pokedex.data.remote.responses.PokemonList
import com.rodsamples.pokedex.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val api : PokeApi
) {

    suspend fun getPokemonList(limit : Int, offset : Int) : Resource<PokemonList>{
        val response = try {
            api.getPokemonList(limit,offset)
        }catch (e : Exception){
            return Resource.Error("An unknown error occurred: ${e.message}")
        }
        return Resource.Success(response)
    }

    suspend fun getPokemonInfo(name : String) : Resource<Pokemon>{
        val response = try {
            api.getPokemon(name)
        }catch (e : Exception){
            return Resource.Error("An unknown error occurred: ${e.message}")
        }
        return Resource.Success(response)
    }
}