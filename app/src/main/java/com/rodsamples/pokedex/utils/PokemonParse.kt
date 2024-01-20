package com.rodsamples.pokedex.utils

import androidx.compose.ui.graphics.Color
import com.rodsamples.pokedex.data.remote.responses.Stat
import com.rodsamples.pokedex.data.remote.responses.Type
import com.rodsamples.pokedex.ui.theme.AtkColor
import com.rodsamples.pokedex.ui.theme.DefColor
import com.rodsamples.pokedex.ui.theme.HPColor
import com.rodsamples.pokedex.ui.theme.SpAtkColor
import com.rodsamples.pokedex.ui.theme.SpDefColor
import com.rodsamples.pokedex.ui.theme.SpdColor
import com.rodsamples.pokedex.ui.theme.TypeBug
import com.rodsamples.pokedex.ui.theme.TypeDark
import com.rodsamples.pokedex.ui.theme.TypeDragon
import com.rodsamples.pokedex.ui.theme.TypeElectric
import com.rodsamples.pokedex.ui.theme.TypeFairy
import com.rodsamples.pokedex.ui.theme.TypeFighting
import com.rodsamples.pokedex.ui.theme.TypeFire
import com.rodsamples.pokedex.ui.theme.TypeFlying
import com.rodsamples.pokedex.ui.theme.TypeGhost
import com.rodsamples.pokedex.ui.theme.TypeGrass
import com.rodsamples.pokedex.ui.theme.TypeGround
import com.rodsamples.pokedex.ui.theme.TypeIce
import com.rodsamples.pokedex.ui.theme.TypeNormal
import com.rodsamples.pokedex.ui.theme.TypePoison
import com.rodsamples.pokedex.ui.theme.TypePsychic
import com.rodsamples.pokedex.ui.theme.TypeRock
import com.rodsamples.pokedex.ui.theme.TypeSteel
import com.rodsamples.pokedex.ui.theme.TypeWater
import java.util.Locale

fun parseTypeToColor(type: Type): Color {
    return when(type.type.name.toLowerCase(Locale.ROOT)) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.Black
    }
}

fun parseStatToColor(stat: Stat): Color {
    return when(stat.stat.name.toLowerCase()) {
        "hp" -> HPColor
        "attack" -> AtkColor
        "defense" -> DefColor
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> Color.White
    }
}

fun parseStatToAbbr(stat: Stat): String {
    return when(stat.stat.name.toLowerCase()) {
        "hp" -> "HP"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> ""
    }
}