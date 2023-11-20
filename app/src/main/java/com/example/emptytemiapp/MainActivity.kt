package com.example.emptytemiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.robotemi.sdk.*
import com.robotemi.sdk.Robot.*
import com.robotemi.sdk.Robot.Companion.getInstance
import com.robotemi.sdk.TtsRequest.Companion.create
import com.robotemi.sdk.activitystream.ActivityStreamObject
import com.robotemi.sdk.activitystream.ActivityStreamPublishMessage
import com.robotemi.sdk.constants.*
import com.robotemi.sdk.exception.OnSdkExceptionListener
import com.robotemi.sdk.exception.SdkException
import com.robotemi.sdk.face.ContactModel
import com.robotemi.sdk.face.OnContinuousFaceRecognizedListener
import com.robotemi.sdk.face.OnFaceRecognizedListener
import com.robotemi.sdk.listeners.*
import com.robotemi.sdk.map.Floor
import com.robotemi.sdk.map.MapModel
import com.robotemi.sdk.map.OnLoadFloorStatusChangedListener
import com.robotemi.sdk.map.OnLoadMapStatusChangedListener
import com.robotemi.sdk.model.CallEventModel
import com.robotemi.sdk.model.DetectionData
import com.robotemi.sdk.navigation.listener.OnCurrentPositionChangedListener
import com.robotemi.sdk.navigation.listener.OnDistanceToDestinationChangedListener
import com.robotemi.sdk.navigation.listener.OnDistanceToLocationChangedListener
import com.robotemi.sdk.navigation.listener.OnReposeStatusChangedListener
import com.robotemi.sdk.navigation.model.Position
import com.robotemi.sdk.navigation.model.SafetyLevel
import com.robotemi.sdk.navigation.model.SpeedLevel
import com.robotemi.sdk.permission.OnRequestPermissionResultListener
import com.robotemi.sdk.permission.Permission
import com.robotemi.sdk.sequence.OnSequencePlayStatusChangedListener
import com.robotemi.sdk.sequence.SequenceModel
import com.robotemi.sdk.telepresence.CallState
import com.robotemi.sdk.telepresence.LinkBasedMeeting
import com.robotemi.sdk.telepresence.Participant
import com.robotemi.sdk.voice.ITtsService
import com.robotemi.sdk.voice.model.TtsVoice

class MainActivity : AppCompatActivity(), OnRequestPermissionResultListener, OnSdkExceptionListener, OnRobotReadyListener, OnLocationsUpdatedListener, OnGreetModeStateChangedListener {

    private lateinit var places: List<String>
    private lateinit var robot: Robot

    private lateinit var text1: TextView

    // ACTIVITY STATES

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        robot = getInstance()

        robot.addOnRequestPermissionResultListener(this)
        robot.addOnSdkExceptionListener(this)

        text1 = findViewById<TextView>(R.id.text1)
    }

    override fun onStart() {
        super.onStart()
        robot.showTopBar()
        robot.addOnRobotReadyListener(this)
        robot.addOnLocationsUpdatedListener(this)
        robot.addOnGreetModeStateChangedListener(this)

    }

    override fun onStop() {

        robot.removeOnLocationsUpdateListener(this)
        robot.removeOnRobotReadyListener(this)
        robot.removeOnGreetModeStateChangedListener(this)
        super.onStop()
    }

    override fun onDestroy() {
        robot.removeOnRequestPermissionResultListener(this)
        robot.removeOnSdkExceptionListener(this)
        super.onDestroy()
    }

    // LISTENERS

    override fun onRobotReady(isReady: Boolean) {
        places = robot.locations
    }

    override fun onSdkError(sdkException: SdkException) {
        speak("Error")
    }

    override fun onRequestPermissionResult(
        permission: Permission,
        grantResult: Int,
        requestCode: Int
    ) {
    }

    override fun onLocationsUpdated(locations: List<String>) {
        places = robot.locations
    }

    // FUNCTIONS

    fun speak(sentence: Any?) {
        val sentenceString = sentence?.toString() ?: ""
        robot.speak(create(sentenceString, false, TtsRequest.Language.SYSTEM, true, false))
    }

    fun shortToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onGreetModeStateChanged(state: Int) {
        var greet: String = when(state) {
            0 -> {
                "IDLE"
            }
            1 -> {
                "SEARCHING"
            }
            2 -> {
                "PREPARING"
            }
            3 -> {
                "GREETING"
            }
            4 -> {
                "INTERACTION"
            }
            5 -> {
                "POST_INTERACTION"
            }
            -1 -> {
                "ERROR"
            } else -> {
                ""
            }
        }
        text1.text = greet
        Log.d("test", "$greet")
    }


}
