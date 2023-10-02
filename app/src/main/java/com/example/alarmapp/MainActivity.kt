package com.example.alarmapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var timePicker: TimePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        timePicker = findViewById(R.id.timepickerID)

        binding.setAlarmButton.setOnClickListener {
            activeAlarm()
            Toast.makeText(this, "Báo thức đã được đặt", Toast.LENGTH_SHORT).show()
        }

        binding.repeatCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Báo thức sẽ được lặp lại", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun activeAlarm() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val isRepeating = binding.repeatCheckbox.isChecked
        // Thời gian hiện tại
        val calendar: Calendar = Calendar.getInstance()
        // Thời gian hẹn giờ
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
        calendar.set(Calendar.MINUTE, timePicker.minute)
        calendar.set(Calendar.SECOND, 0)


        val intent: Intent = Intent(this, MyBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        if(isRepeating) {
            // Đặt báo thức hàng ngày
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,  // thời gian muốn báo thức chạy
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
        // Đặt báo thức 1 lần
        else {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }

        // Cho giờ đã chọn vào EditText để hiển thị lên
        val etSelectedTime = binding.selectedTime
        val selectedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
        etSelectedTime.setText(selectedTime)
    }
}