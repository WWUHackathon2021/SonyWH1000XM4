package com.example.journey

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.ServerError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.profile_ui.*


const val LOGIN ="http://ec2-54-190-234-6.us-west-2.compute.amazonaws.com:5000/login"
const val PING ="http://ec2-54-190-234-6.us-west-2.compute.amazonaws.com:5000/ping"
class LoginActivity : AppCompatActivity(){

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

        loginButton.setOnClickListener {
//            signInTest()
            signIn()
        }

    }

    private fun signInTest() {
        val Intent = Intent(this, MainActivity::class.java)
        startActivity(Intent)
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            Toast.makeText(this, "DONE SIGNING IN", Toast.LENGTH_LONG).show()

            var task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken = account!!.idToken
            sendRequest(idToken!!)
            signInTest()



        } catch (e: ApiException) {
            Log.v("SignInActivity", "handleSignInResult:error", e)


        }
    }

    fun sendRequest(token: String){
        try {
            val queue = Volley.newRequestQueue(this)



            val stringRequest = object: StringRequest(
                    Request.Method.POST,
                    LOGIN,
                    { response ->
                        // Display the first 500 characters of the response string.
                        var msg = token
                        Toast.makeText(this, "msg: "+msg, Toast.LENGTH_LONG).show()

                    },
                    { error ->
                        var msg = "That didn't work!"
                    })
                    {
                        override fun getBodyContentType(): String {
                            return "application/x-www-form-urlencoded"
                        }

                        override fun getParams(): MutableMap<String, String> {
                            var params = mutableMapOf<String, String>()
                            params.put("token", token)
                            return params
                        }
                    }

            queue.add(stringRequest)
        }

        catch(e:ServerError){
            Toast.makeText(this, ""+e, Toast.LENGTH_LONG).show()
        }




    }




}