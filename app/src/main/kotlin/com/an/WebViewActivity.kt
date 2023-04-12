package com.an

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.themeadapter.material.MdcTheme
import kotlin.math.roundToInt


class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = getIntent();
        val url = intent.getStringExtra("URL")
        val title = intent.getStringExtra("TITLE")
        setContent {
            // Calling the composable function
            // to display element and its contents
            MainContent(title, url)
        }

//        val actionBar = getSupportActionBar();
//        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        finish();
//        return true;
//    }
}

// Creating a composable
// function to display Top Bar
@Composable
fun MainContent(title: String?, url: String?) {
    val activity = (LocalContext.current as? Activity)
    Scaffold(
        topBar = {
            MdcTheme {
                TopAppBar(
                    title = { Text(text = title!!) },
                    navigationIcon = {
                            IconButton(onClick = { activity?.finish() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                    }

                )
            }},
        content = { padding ->
            MdcTheme {
                MyContent(url!!)
            }
        }
    )
}

private class MyWebView(context: Context) : WebView(context) {
    val verticalScrollRange: Int get() = computeVerticalScrollRange() - height
}

@Composable
fun WebViewCompose(url: String, modifier: Modifier = Modifier, onCreated: (WebView) -> Unit = {}) {
    val context = LocalContext.current
    val webView: MyWebView = remember(context) {
        MyWebView(context).also(onCreated)
    }
    DisposableEffect(webView) {
        onDispose {
            webView.stopLoading()
            webView.destroy()
        }
    }
    val scrollabeState = rememberScrollableState { delta ->
        val scrollY = webView.scrollY
        val consume = (scrollY - delta).coerceIn(0f, webView.verticalScrollRange.toFloat())
        webView.scrollTo(0, consume.roundToInt())
        (scrollY - webView.scrollY).toFloat()
    }
    AndroidView(
        factory = { webView },
        modifier = modifier
            .scrollable(scrollabeState, Orientation.Vertical)
    ) { webView2 ->
        webView2.loadUrl(url)
    }
}

// Creating a composable
// function to create WebView
// Calling this function as
// content in the above function
@Composable
fun MyContent(url: String){

    // Declare a string that contains a url
//    val mUrl = "https://www.geeksforgeeks.org"

    // Adding a WebView inside AndroidView
    // with layout as full screen

    WebViewCompose(url = url)

//    AndroidView(factory = {
//        WebView(it).apply {
//            layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            )
//            webViewClient = WebViewClient()
//            loadUrl(url)
//        }
//    }, update = {
//        it.loadUrl(url)
//    })
}

// For displaying preview in
// the Android Studio IDE emulator
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainContent("title", "https://www.geeksforgeeks.org")
}