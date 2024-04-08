package com.example.androidtranslatorapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.androidtranslatorapi.API.retrofitService
import com.example.androidtranslatorapi.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private var allLanguages = emptyList<Language>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        initListeners()
        getLanguages()
    }

    private fun initListeners() {
        mainBinding.btnDetectLanguage.setOnClickListener(){
            val text:String = mainBinding.etDescription.text.toString()
            if(text.isNotEmpty()){
                getTextLanguage(text)
            }
        }
    }

    private fun getTextLanguage(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result:Response<DetectionResponse> = retrofitService.getTextLanguage(text)
            if(result.isSuccessful){
                checkResult(result.body())
            }else{
                showError()
            }
        }
    }

    private fun checkResult(detectionResponse: DetectionResponse?) {
        if(detectionResponse != null && !detectionResponse.data.detections.isNullOrEmpty()){
            val correctLanguages:List<Detection> = detectionResponse.data.detections.filter{it.isReliable}
            if(correctLanguages.isNotEmpty()){
                val languageName:Language? = allLanguages.find { it.code == correctLanguages.first().language }
                if(languageName != null){
                    runOnUiThread{
                        Toast.makeText(this,"LANGUAGE IS: ${languageName.name}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun getLanguages() {
        CoroutineScope(Dispatchers.IO).launch {
            val languages:Response<List<Language>> = retrofitService.getLanguages()
            if(languages.isSuccessful){
                allLanguages = languages.body() ?: emptyList()
                showSuccess()
            }else{
                showError()
            }
        }
    }

    private fun showSuccess() {
        runOnUiThread{
            Toast.makeText(this,"¡Petition Success!:", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showError() {
        runOnUiThread{
            Toast.makeText(this,"Error:", Toast.LENGTH_SHORT).show()
        }
    }
}