@file:Suppress("DEPRECATION")

package com.example.buscacepapp

import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.Modifier
import com.example.buscacepapp.HttpService
import androidx.compose.material3.Typography


class MainActivity : AppCompatActivity() {

    private lateinit var btnBuscaCep: Button
    private lateinit var txtCep: EditText
    private lateinit var lblResposta: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtCep = findViewById(R.id.txtCep)
        lblResposta = findViewById(R.id.lblResposta)
        btnBuscaCep = findViewById(R.id.btnBuscaCep)

        btnBuscaCep.setOnClickListener {
            val cep = txtCep.text.toString().trim()

            if (cep.length != 8) {
                lblResposta.text = "CEP inválido"
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val retorno = HttpService(cep).execute().get()

                    if (retorno == null) {
                        runOnUiThread {
                            lblResposta.text = retorno.toString()
                        }
                        return@launch
                    }

                    runOnUiThread {
                        lblResposta.text = retorno.toString()
                    }

                } catch (e : Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        lblResposta.text = "Erro ao buscar CEP: ${e.message}"
                    }
                }
            }
        }
    }

    private fun buscaCEP() {
        val cep = txtCep.text.toString().trim()

        if (cep.length != 8) {
            lblResposta.text = "CEP inválido"
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val retorno = HttpService(cep).execute().get()

                runOnUiThread {
                    lblResposta.text = retorno.toString()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    lblResposta.text = "Erro ao buscar CEP"
                }
            }
        }
    }
}

@Composable
fun BuscaCEPAppTheme(content : @Composable () -> Unit) {

    MaterialTheme(
        colorScheme = lightColorScheme(),
        //typography = Typography,
        content = content
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BuscaCEPAppTheme {
        Greeting("Android")
    }
}