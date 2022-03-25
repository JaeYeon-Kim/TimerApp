package com.kjy.timerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.kjy.timerapp.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // 전체시간을 저장하는 total , 처음은 0초
    var total = 0
    // 시작됨을 체크할 수 있는 started, 시작되지 않았으므로 false
    var started = false

    // total과 started를 이용해서 화면에 시간값을 출력하는 Handle를 구현하고 handler 변수에 저장
    // 핸들러로 메시지가 전달되면 total에 입력되어 있는 시간(초)을 60으로 나눈 값은 분단위로
    // 60으로 나눈 나머지 값은 초 단위로 사용해서 textTimer에 입력.
    val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val minute = String.format("%02d", total/60)
            val second = String.format("%02d", total%60)
            binding.textTimer.text = "$minute:$second"
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 시작 코드 구현
        // 버튼 클릭시 started를 true로 변경하고 스레드 실행.
        // 스레드는 while 문의 started가 true인 동안 while 문을 반복하면서 1초에 한 번씩 total의 값을 1씩 증가시키고
        // 핸들러에 메시지를 전송. 핸들러를 호출하는 곳이 하나밖에 없으므로 메시지에 0을 담아서 호출
        binding.buttonStart.setOnClickListener {
            started = true
            thread(start=true) {
                while(started) {
                    Thread.sleep(1000)
                    if (started) {
                        total = total + 1
                        handler?.sendEmptyMessage(0)
                    }
                }

            }
        }

        // 종료 코드 구현
        // started에 false 처리 total = 0, 시간을 표시하는 텍스트 뷰 '00:00' 으로 초기화
        binding.buttonStop.setOnClickListener {
            if (started) {
                started = false
                total = 0
                binding.textTimer.text="00:00"
            }
        }
    }
}