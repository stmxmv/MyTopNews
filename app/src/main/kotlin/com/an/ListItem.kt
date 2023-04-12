package com.an

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.RecyclerView
import com.an.mytopnews.R

import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.startActivity

import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.google.android.material.snackbar.Snackbar
import java.nio.channels.UnresolvedAddressException

@Composable
fun ListItem(title: String) {
    Row {
        Column {
            Text(text = title, style = typography.h6)
            Text(text = "VIEW DETAIL", style = typography.caption)
        }
    }
}

@Composable
private fun ListItemImage(url: String) {
    Image(
        painter = rememberImagePainter(url),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(8.dp)
            .size(84.dp)
            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
    )
}

@Composable
fun ListItem(news: News) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                /// web view
                val intent = Intent(context, WebViewActivity::class.java)
                intent.putExtra("URL", news.url)
                intent.putExtra("TITLE", news.title)
                context.startActivity(intent)

                /// start in a browser
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.data = Uri.parse(news.url)
//                context.startActivity(intent)


            },
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),

    ) {
        Row (
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                ListItemImage(news.imageUrl)
            }
            Column(
                modifier = Modifier
                    .padding(13.dp)
                    .align(Alignment.Top)
//                    .background(Color.LightGray)
            ) {
                Text(text = news.title, style = typography.h6)
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = news.source,
                        style = typography.caption,
                    )
                    Text(
                        text = news.date,
                        style = typography.caption,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
fun DisplayList(category: String, viewModel: NewsListViewModel = viewModel()) {
    // on below line we arecreating a simple list
    // of strings and adding different programming
    // languages in it.

    val view = LocalView.current

    // State
    val newsList = viewModel.newsList.observeAsState()
    var refreshCount by remember { mutableStateOf(1) }

    val unresolvedAddressErrorMsg = stringResource(id = R.string.UnresolvedAddress)

    // API call
    LaunchedEffect(key1 = refreshCount) {
        viewModel.fetchNews(category, { error ->
            if (error is UnresolvedAddressException) {

                Snackbar.make(view, unresolvedAddressErrorMsg, Snackbar.LENGTH_SHORT).show()

            } else {
                if (error.localizedMessage != null) {
                    Snackbar.make(view, error.localizedMessage!!, Snackbar.LENGTH_SHORT).show()
                } else if (error.message != null) {
                    Snackbar.make(view, error.message!!, Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 3.dp, vertical = 3.dp)
    ) {
        val list = newsList.value
        if (list != null) {
            items(list) { news ->
                ListItem(news)
            }
        }
    }

}


class ListItemViewHolder(
    val composeView: AbstractComposeView
) : RecyclerView.ViewHolder(composeView) {

    init {
        // This is from the previous guidance
        // NOTE: **Do not** do this with Compose 1.2.0-beta02+
        // and RecyclerView 1.3.0-alpha02+
//        composeView.setViewCompositionStrategy(
//            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
//        )
    }

}


class ListItemView
@JvmOverloads
constructor(context: Context, val category: String, attrs: AttributeSet? = null) :
    AbstractComposeView(context, attrs) {

    @Composable
    override fun Content() {
        DisplayList(category = category);
    }
}

@Preview
@Composable
fun SimpleComposablePreview() {
    DisplayList("top");
}