package com.giziguru.app.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.giziguru.app.R
import com.giziguru.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val CAMERA_REQUEST_CODE = 101
    private var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val menu = binding.navView.menu

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.popBackStack() // Add this line
                    navController.navigate(R.id.navigation_home)
                    true
                }

                R.id.navigation_explore -> {
                    navController.popBackStack() // Add this line
                    navController.navigate(R.id.navigation_explore)
                    true
                }

                R.id.navigation_history -> {
                    navController.popBackStack()
                    navController.navigate(R.id.navigation_history)
                    true
                }

                R.id.navigation_profile -> {
                    navController.popBackStack()
                    navController.navigate(R.id.navigation_profile)
                    true
                }

                R.id.navigation_scan -> {
                    requestCameraPermission()
                    true
                }

                else -> false
            }
        }
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            startCamera()
        }
    }

    private fun startCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                val compressedImageBitmap = compressBitmap(imageBitmap)
                val bundle = Bundle().apply {
                    putParcelable("imageBitmap", compressedImageBitmap)
                }
                val navController =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home)
                        ?.findNavController()
                navController?.navigate(R.id.navigation_scan, bundle)
            } else {
                onBackPressed()
            }
        }
    }

    private fun compressBitmap(bitmap: Bitmap?): Bitmap? {
        if (bitmap == null) return null

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            80,
            outputStream
        ) // Adjust the compression quality as desired (80 in this example)
        val compressedByteArray = outputStream.toByteArray()
        return BitmapFactory.decodeByteArray(compressedByteArray, 0, compressedByteArray.size)
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
}