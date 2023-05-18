package com.example.chatgptapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.chatgptapi.databinding.ActivityMainBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    lateinit var binding:  ActivityMainBinding
    lateinit var adapter: MessageAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val etQuestion = findViewById<EditText>(R.id.etQuestion)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        //val tvResponse = findViewById<TextView>(R.id.tvResponse)

        initial()

        btnSubmit.setOnClickListener{
            val question = etQuestion.text.toString()
            Toast.makeText(this, question, Toast.LENGTH_SHORT).show()
            getResponse(question){response ->
                runOnUiThread{
                    //tvResponse.text = response
                    adapter.setList(myMessage(response))
                }
            }
        }
    }

    private fun initial() {
        recyclerView  = binding.rvChat
        adapter = MessageAdapter(this)
        recyclerView.adapter = adapter

    }

    fun myMessage(msg: String) : ArrayList<String>{
        val messageList = ArrayList<String>()
        messageList.add(msg)
        return messageList
    }

    fun getResponse(question: String, callback: (String) -> Unit){
        val apiKey = ""
        val url = "https://api.openai.com/v1/completions"

        val  requestBody = """
            {
            "model": "text-davinci-003",
            "prompt": "$question",
            "max_tokens": 500,
            "temperature": 0
            } 
        """.trimIndent()

        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("error", "API failed", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if (body != null) {
                    Log.v("data", body)
                }
                else{
                    Log.v("data", "empty")
                }

                val jsonObject = JSONObject(body)
                val jsonArray: JSONArray = jsonObject.getJSONArray("choices")
                val textResult = jsonArray.getJSONObject(0).getString("text")
                callback(textResult)
            }
        })
    }
}