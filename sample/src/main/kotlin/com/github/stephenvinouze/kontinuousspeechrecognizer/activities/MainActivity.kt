package com.github.stephenvinouze.kontinuousspeechrecognizer.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.SpeechRecognizer
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.github.stephenvinouze.core.interfaces.RecognitionCallback
import com.github.stephenvinouze.core.managers.KontinuousRecognitionManager
import com.github.stephenvinouze.core.models.RecognitionStatus
import com.github.stephenvinouze.kontinuousspeechrecognizer.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RecognitionCallback {

    companion object {
        /**
         * Put any keyword that will trigger the speech recognition
         */
        private const val ACTIVATION_KEYWORD = "one"
        private const val RECORD_AUDIO_REQUEST_CODE = 101
    }

    private val recognitionManager: KontinuousRecognitionManager by lazy {
        KontinuousRecognitionManager(this, activationKeyword = ACTIVATION_KEYWORD, callback = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recognitionManager.createRecognizer()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_REQUEST_CODE)
        }

        button1.setOnClickListener(){
            Toast.makeText(this, "AGAIN!", Toast.LENGTH_SHORT).show()
        }

        button2.setOnClickListener(){
            Toast.makeText(this, "HARD!", Toast.LENGTH_SHORT).show()
        }

        button3.setOnClickListener(){
            Toast.makeText(this, "GOOD!", Toast.LENGTH_SHORT).show()
        }

        button4.setOnClickListener(){
            Toast.makeText(this, "EASY!", Toast.LENGTH_SHORT).show()
        }

        button5.setOnClickListener(){
            Toast.makeText(this, "ANSWER!", Toast.LENGTH_SHORT).show()
        }

        button6.setOnClickListener(){
            Toast.makeText(this, "NEXT!", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroy() {
        recognitionManager.destroyRecognizer()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startRecognition()
        }
    }

    override fun onPause() {
        stopRecognition()
        super.onPause()
    }

    private fun startRecognition() {

        recognitionManager.startRecognition()
    }

    private fun stopRecognition() {

        recognitionManager.stopRecognition()
    }

    private fun getErrorText(errorCode: Int): String = when (errorCode) {
        SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
        SpeechRecognizer.ERROR_CLIENT -> "Client side error"
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
        SpeechRecognizer.ERROR_NETWORK -> "Network error"
        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
        SpeechRecognizer.ERROR_NO_MATCH -> "No match"
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
        SpeechRecognizer.ERROR_SERVER -> "Error from server"
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
        else -> "Didn't understand, please try again."
    }

    override fun onBeginningOfSpeech() {
        Log.i("Recognition","onBeginningOfSpeech")
    }

    override fun onBufferReceived(buffer: ByteArray) {
        Log.i("Recognition", "onBufferReceived: $buffer")
    }

    override fun onEndOfSpeech() {
        Log.i("Recognition","onEndOfSpeech")
    }

    override fun onError(errorCode: Int) {
        val errorMessage = getErrorText(errorCode)
        Log.i("Recognition","onError: $errorMessage")
        textView.text = errorMessage
    }

    override fun onEvent(eventType: Int, params: Bundle) {
        Log.i("Recognition","onEvent")
    }

    override fun onReadyForSpeech(params: Bundle) {
        Log.i("Recognition","onReadyForSpeech")
    }

    override fun onRmsChanged(rmsdB: Float) {
        Log.i("Recognition","onRmsChanged")
    }

    override fun onPrepared(status: RecognitionStatus) {
        when (status) {
            RecognitionStatus.SUCCESS -> {
                Log.i("Recognition","onPrepared: Success")
                textView.text = "Recognition ready"
            }
            RecognitionStatus.UNAVAILABLE -> {
                Log.i("Recognition", "onPrepared: Failure or unavailable")
                AlertDialog.Builder(this)
                        .setTitle("Speech Recognizer unavailable")
                        .setMessage("Your device does not support Speech Recognition. Sorry!")
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
            }
        }
    }

    override fun onKeyword1Detected() {
        Log.i("Recognition","keyword detected !!!")
        textView.text = "Keyword detected"
        button1.performClick();
    }

    override fun onKeyword2Detected() {
        Log.i("Recognition","keyword detected !!!")
        textView.text = "Keyword detected"
        button2.performClick();
    }

    override fun onKeyword3Detected() {
        Log.i("Recognition","keyword detected !!!")
        textView.text = "Keyword detected"
        button3.performClick();
    }

    override fun onKeyword4Detected() {
        Log.i("Recognition","keyword detected !!!")
        textView.text = "Keyword detected"
        button4.performClick();
    }

    override fun onKeyword5Detected() {
        Log.i("Recognition","keyword detected !!!")
        textView.text = "Keyword detected"
        button5.performClick();
    }

    override fun onKeyword6Detected() {
        Log.i("Recognition","keyword detected !!!")
        textView.text = "Keyword detected"
        button6.performClick();
    }

    override fun onPartialResults(results: List<String>) {}

    override fun onResults(results: List<String>, scores: FloatArray?) {
        val text = results.joinToString(separator = "\n")
        Log.i("Recognition","onResults : $text")
        textView.text = text
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_AUDIO_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startRecognition()
                }
            }
        }
    }

}
