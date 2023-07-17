package com.example.test11

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.test11.databinding.ActivityFullScreenBinding
import java.io.File

class FullScreenActivity : AppCompatActivity() {
    private val TAG = "BBBBBBBBBBBBBBBBBBB"
    private lateinit var binding: ActivityFullScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var imagePath = intent.getStringExtra("path")
        val imageFile = File(imagePath)
        val imageName = imageFile.name
        Log.e(TAG, "onCreate:old Name $imagePath")
        Glide.with(this).load(imagePath).into(binding.imageView)

        binding.editText.setText(imageName)

        binding.button.setOnClickListener {
            val newImageName = binding.editText.text.toString()
            if (newImageName.isNotEmpty()) {
                val newImageFile = File(imageFile.parent, newImageName)
                Log.e(TAG, "onCreate: newImageFile $newImageFile", )

                if (imageFile.renameTo(newImageFile)) {
                    binding.editText.clearFocus()
                    // update new path
                    imagePath = newImageFile.absolutePath

                    imageFile.delete()


                    Log.e(TAG, "onCreate:new Name $imagePath")
                    Toast.makeText(this, "Đổi tên thành công", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "Đổi tên thất bại", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập tên mới", Toast.LENGTH_SHORT).show()
            }
            Log.e(TAG, "onCreate: $imagePath", )
        }

        binding.btnName.setOnClickListener{
            intent.putExtra("newImagePath", imagePath)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}