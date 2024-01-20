package com.rodsamples.pokedex.presentation.ui.pokemonlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.rodsamples.pokedex.R
import com.rodsamples.pokedex.data.models.PokedexListEntry
import com.rodsamples.pokedex.ui.theme.Roboto
import com.rodsamples.pokedex.ui.theme.RobotoCondensed

@Composable
fun PokemonListScreen(
    navController : NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
){
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "Pokemon",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
            SearchBar(
                hint = "Search...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                viewModel.searchPokemonList()
            }
            Spacer(modifier = Modifier.height(20.dp))
            PokemonLazyColumn(navController = navController)
        }

    }
}
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    viewModel: PokemonListViewModel = hiltViewModel(),
    onSearch: (String) -> Unit = {}
) {
    val text by remember {
        viewModel.query
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                //text = it
                viewModel.query.value = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = !it.isFocused && text.isEmpty()
                }
        )
        if(isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun PokemonLazyColumn(
    navController : NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
){
    val pokemonList by remember { viewModel.pokemonList }
    val isLoading by remember { viewModel.isLoading }
    val loadError by remember { viewModel.loadError }
    val endReached by remember { viewModel.endReached }
    val isSearching by remember { viewModel.isSearching }

    LazyColumn(contentPadding = PaddingValues(16.dp)){
        val itemCount = if(pokemonList.size % 2 == 0){  // update list count since we have two entries per row/item (since one row is one item)
            pokemonList.size / 2
        }else{
            pokemonList.size / 2 + 1
        }
        
        items(itemCount){ currentIndex ->
            if(currentIndex >= itemCount - 1 && !endReached && !isLoading && !isSearching){ // todo paginate only if
                LaunchedEffect(key1 = true){
                    viewModel.loadPokemonPaginated()
                }
            }
            PokemonRow(rowIndex = currentIndex, data = pokemonList, navController = navController)
        }
    }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center){
        if(isLoading){
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        if(loadError.isNotEmpty()){
            ErrorRetryButton(message = loadError) {
                viewModel.loadPokemonPaginated()
            }
        }
    }

}

@Composable
fun PokemonRow(
    rowIndex : Int,
    data : List<PokedexListEntry>,
    navController: NavController
){
    Column {
        Row {
            PokemonListEntry(
                entry = data[rowIndex * 2],
                navController = navController,
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier
                .width(16.dp))
            if(data.size >= rowIndex * 2 + 2){ // todo : To determine if we can still add 1 more entry in the Row because two items per row
                PokemonListEntry(
                    entry = data[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier
                        .weight(1f)
                )
            }else {
                Spacer(modifier = Modifier
                    .weight(1f))
            }
        }
        Spacer(modifier = Modifier
            .height(16.dp))
    }
}

@Composable
fun PokemonListEntry(
    entry : PokedexListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel : PokemonListViewModel = hiltViewModel()
){

    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
            .clickable {
                navController.navigate(
                    "pokemon_detail_screen/${dominantColor.toArgb()}/${entry.pokemonName}"
                )
            },
        contentAlignment = Center){
        Column {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(entry.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = entry.pokemonName,
                loading = {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary, modifier = Modifier.scale(0.5F)
                    )
                },
                success = {
                    viewModel.getDominantColor(it.result.drawable){color ->
                        dominantColor = color
                    }
                    SubcomposeAsyncImageContent()
                },
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)
            )
            Text(
                text = entry.pokemonName,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            /* Coil is Deprecated
            CoilImage(request = ImageRequest.Builder(LocalContext.current)
                .data(entry.imageUrl)
                .target {
                    viewModel.getDominantColor(it){color ->
                        dominantColor = color
                    }
                }
                .build(),
                contentDescription = entry.pokemonName,
                fadeIn = true,
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(15.dp)
                )
                Text(
                    text = entry.pokemonName,
                    fontFamily = RobotoCondensed,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }*/
        }
        
    }

}

@Composable
fun ErrorRetryButton(
    message : String,
    onRetry : () -> Unit
){
    Column{
        Text(
            text = message,
            color = Color.Red,
            fontSize = 20.sp,
            fontFamily = Roboto)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)) {
            Text(text = "Retry")
        }
    }
}