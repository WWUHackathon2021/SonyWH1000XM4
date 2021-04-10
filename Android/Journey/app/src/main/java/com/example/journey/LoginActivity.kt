package com.example.journey

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_ui)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("167088893835-dpu20kopj0rlommabl7e3nma6sj5v4qt.apps.googleusercontent.com")
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        val signInButton = findViewById<ImageButton>(R.id.loginButton)

        signInButton.setOnClickListener(this)


    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        Toast.makeText(this, "gets here", Toast.LENGTH_LONG).show()
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    override fun onClick(view: View){
        signIn()
    }




}