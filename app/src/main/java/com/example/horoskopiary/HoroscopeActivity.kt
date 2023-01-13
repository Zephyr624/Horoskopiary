package com.example.horoskopiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HoroscopeActivity : AppCompatActivity() ,
    AdapterView.OnItemSelectedListener{
    var sunSign = "Aries"
    var resultView: TextView? = null
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            sunSign = parent.getItemAtPosition(position).toString()
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        sunSign = "Aries"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horoscope)
        var buttonView: Button = findViewById(R.id.button)

        buttonView.setOnClickListener {
            GlobalScope.async {
                getPredictions(buttonView)
            }
        }
        val spinner = findViewById<Spinner>(R.id.spinner)
        val adapter = ArrayAdapter.createFromResource(this,R.array.zodiac_signs,android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter;
        spinner.onItemSelectedListener = this
        resultView = findViewById(R.id.resultView)
    }
    public suspend fun getPredictions(view: android.view.View) {
        try {
            val result = GlobalScope.async {
                callAztroAPI("https://sameer-kumar-aztro-v1.p.rapidapi.com/?sign=" + sunSign + "&day=today")
            }.await()
            onResponse(result)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun callAztroAPI(apiUrl:String ):String?{
        var result: String? = ""
        val url: URL;
        var connection: HttpURLConnection? = null
        try {
            url = URL(apiUrl)
            connection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty("x-rapidapi-host", "sameer-kumar-aztro-v1.p.rapidapi.com")
            connection.setRequestProperty("x-rapidapi-key", "<API KEY>")
            connection.setRequestProperty("content-type", "application/x-www-form-urlencoded")
            connection.requestMethod = "POST"
            val `in` = connection.inputStream
            val reader = InputStreamReader(`in`)
            var data = reader.read()
            while (data != -1) {
                val current = data.toChar()
                result += current
                data = reader.read()
            }
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    private fun onResponse(result: String?) {
        try {
            val resultJson = JSONObject(result)
            var prediction ="Today's prediction \n"
            prediction += this.sunSign+"\n"
            prediction += resultJson.getString("date_range")+"\n"
            prediction += resultJson.getString("description")
            setText(this.resultView,prediction)
        } catch (e: Exception) {
            e.printStackTrace()
            this.resultView!!.text = "Oops!! something went wrong, please try again"
        }
    }
    private fun setText(text: TextView?, value: String) {
        runOnUiThread { text!!.text = value }
    }
}