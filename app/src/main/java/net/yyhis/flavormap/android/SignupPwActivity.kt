package net.yyhis.flavormap.android

//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.ImageButton
//import androidx.appcompat.app.AppCompatActivity
//
//class SignupPwActivity: AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_signup_pw) // 위에서 제공한 XML 파일 이름과 일치시켜야 합니다.
//
//        // 뒤로가기 버튼
//        val backButton: ImageButton = findViewById(R.id.backButton)
//        backButton.setOnClickListener {
//            finish() // 현재 Activity를 종료하여 이전 화면으로 이동
//        }
//
//        // 다음 버튼
//        val nextButton: Button = findViewById(R.id.nextButton)
//        nextButton.setOnClickListener {
//            // 다음 처리
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }
//    }
//}