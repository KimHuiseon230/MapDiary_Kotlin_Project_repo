package com.example.mapdiary.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapdiary.R
import com.example.mapdiary.activity.MyApplication.Companion.email
import com.example.mapdiary.dataclass.User
import com.example.mapdiary.firebase.FBAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class GoogleLogin : AppCompatActivity() {

    // Firebase
    private lateinit var googleSignInClient: GoogleSignInClient
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val RC_SIGN_IN = 1313

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen { googleLogin() }
        }
    }

    // 로그인 객체 생성
    fun googleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignIn()
    }

    // 구글 회원가입
    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "구글 회원가입에 실패하였습니다: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            /*no-op*/
        }
    }

    // account 객체에서 id 토큰 가져온 후 Firebase 인증
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                test()
                toMainActivity(auth.currentUser)
            }
        }
    }

    private fun test() {

        // Firebase 데이터베이스 참조를 가져옵니다.
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("User/users") // "users"는 데이터베이스 경로입니다.

        val user = FBAuth.auth.currentUser

        user?.getIdToken(true)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken = task.result?.token
                    if (idToken != null) {
                        // 사용자의 ID 토큰과 UID를 Firebase 데이터베이스에 저장합니다.
                        val uid = user.uid
                        val id = createAccountWithRandomName()
                        val userEmail = user?.email
                        val userNickname = user?.displayName
                        val photoUrl = user?.photoUrl.toString() // 사용자 프로필 사진 URL을 문자열로 가져옴

                        usersRef.child(uid.toString()).setValue(
                                User(
                                    uid,
                                    id,
                                    userEmail,
                                    "",
                                    userNickname,
                                    idToken,
                                    0,
                                    0,
                                    photoUrl
                                )
                            )       //setValue
                    } else {
                        Toast.makeText(this, "ID 토큰이 null", Toast.LENGTH_SHORT).show()
                        Log.e("ID 토큰이 null", idToken.toString())
                        // ID 토큰이 null인 경우 처리할 내용을 추가할 수 있습니다.
                    }
                } else {
                    // 작업이 실패한 경우 처리할 내용을 추가할 수 있습니다.
                    val exception = task.exception
                    if (exception != null) {
                        // 실패한 이유를 처리할 수 있습니다.
                    }
                }
            }
    }

    // 랜덤한 이름 생성
    fun createAccountWithRandomName(): String {
        val randomName = "user_" + generateRandomString(8) // 예: user6521
        return randomName
    }

    // 안전한 랜덤한 패스워드 생성
    fun generateRandomString(length: Int): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun toMainActivity(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    //Login - Compose UI
    @Composable
    fun LoginScreen(content: () -> Unit) {
        Surface(color = Color.White) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 15.dp, end = 15.dp),
                verticalArrangement = Arrangement.Center

            ) {
                Greeting(text = "시작하시겠습니까?")
                SignInGoogleButton { content() }
            }
        }
    }

    @Composable
    fun Greeting(text: String) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 12.dp)
        )
    }

    @Composable
    fun SignInGoogleButton(onClick: () -> Unit) {
        Surface(
            modifier = Modifier
                .clickable(onClick = onClick)
                .fillMaxWidth(),
            border = BorderStroke(width = 1.dp, color = Color.LightGray),
            color = MaterialTheme.colors.surface,
            shape = MaterialTheme.shapes.small,
            elevation = 10.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(
                    start = 14.dp,
                    end = 12.dp,
                    top = 11.dp,
                    bottom = 11.dp
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.img_4),
                    contentDescription = "Google sign button",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(35.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Sign in with Google",
                    style = MaterialTheme.typography.overline,
                    color = Color.Gray,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}