package com.example.test11

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test11.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),  ImageAdapter.OnItemClickListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private val imageList: ArrayList<String> = ArrayList()

    private val REQUEST_CODE_FULLSCREEN = 789
    private val READ_EXTERNAL_STORAGE_REQUEST_CODE = 123
    private val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 456

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        imageAdapter = ImageAdapter(this, imageList)
        binding.recyclerView.adapter = imageAdapter

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST_CODE
            )
        } else {
        }

        //write
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_EXTERNAL_STORAGE_REQUEST_CODE
            )
        } else {
            //Permission un check
        }
        loadImages()


        imageAdapter.setOnItemClickListener(this)
        binding.btnSortName.setOnClickListener { onClickSortName() }
        binding.btnSortSize.setOnClickListener { onClickSortSize() }
        binding.btnSortTime.setOnClickListener { onClickSortTime() }

        binding.swipeRefreshLayout.setOnRefreshListener {


            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun onClickSortSize() {
        imageAdapter.setSortType(ImageAdapter.SortType.TIME)
    }

    private fun onClickSortName() {
        imageAdapter.setSortType(ImageAdapter.SortType.NAME)
    }

    private fun onClickSortTime() {
        imageAdapter.setSortType(ImageAdapter.SortType.SIZE)
    }

    private fun loadImages() {
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        val cursor = contentResolver
            .query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , projection,null,null,null)

        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (it.moveToNext()) {
                val imagePath = it.getString(columnIndex)
                imageList.add(imagePath)
            }
        }
        imageAdapter.sortImageList()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE || requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImages()
            } else {
                Toast.makeText(this, "Ứng dụng cần quyền truy cập bộ nhớ để hiển thị ảnh", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemClick(imagePath: String) {
        val intent = Intent(this, FullScreenActivity::class.java)
        intent.putExtra("path", imagePath)
        startActivityForResult(intent, REQUEST_CODE_FULLSCREEN)
    }

}