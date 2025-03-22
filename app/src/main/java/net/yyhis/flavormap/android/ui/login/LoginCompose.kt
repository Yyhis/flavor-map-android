package net.yyhis.flavormap.android.ui.login

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.yyhis.flavormap.android.R
import net.yyhis.flavormap.android.util.kakaoLogin

@Composable
fun LoginPage(context: Context) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier.padding(start = 12.dp, end = 12.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Button and Title
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "LOGIN",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Email and Password Fields
        Column {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("아이디 (이메일)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("비밀번호") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Handle Login */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("로그인하기", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SNS Login Section
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("SNS계정으로 로그인하기", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp), // 각 아이콘 사이의 간격
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Image(
                    painter = painterResource(R.drawable.kakao_login_medium_wide),
                    contentDescription = "Setting Button",
                    modifier = Modifier.size(300.dp, 45.dp).clickable {
                        kakaoLogin(context)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Footer Section
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { /* Handle Signup */ }
            ) {
                Text("계정이 없으신가요? 간편가입하기")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                TextButton (
                    onClick = { /* Handle Find ID */ }
                ) {
                    Text("아이디 (이메일) 찾기")
                }
                TextButton(onClick = { /* Handle Find Password */ }) {
                    Text("비밀번호 찾기")
                }
            }
        }
    }
}