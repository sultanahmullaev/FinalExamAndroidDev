package com.example.androiddevfinalexam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var input: EditText
    private lateinit var output: TextView
    private lateinit var mainBtn: Button
    private var list: List<Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        input = findViewById(R.id.input)
        mainBtn = findViewById(R.id.main_btn)

        mainBtn.setOnClickListener {
            val thread = Thread {
                try {
                    getJsonFromURL(input.text.toString())
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            thread.start()
        }
    }


    private fun getJsonFromURL(city: String) {
        try {
            val temp1 = "https://api.covid19api.com/countries"
            val temp2 = "https://api.covid19api.com/country/$city"
            val url = URL(temp1)
            val connection = url.openConnection()
            BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->
                var line: String?
                while (inp.readLine().also { line = it } != null) {
                    val obj = JSONObject(line.toString())
                    val country: JSONObject = obj.getJSONObject("Country")
                    val slug: JSONObject = obj.getJSONObject("Slug")

                    if(country.getString("Country") == city) {
                        output = findViewById(R.id.output)
                        val slug: JSONObject = obj.getJSONObject("Slug")
                        output.post { output.text = slug.getString("Slug") }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}