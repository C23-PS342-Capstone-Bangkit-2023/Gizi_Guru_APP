package com.giziguru.app.ui.scan

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.giziguru.app.R
import com.giziguru.app.databinding.FragmentScanBinding
import com.giziguru.app.ml.Model
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

@AndroidEntryPoint
@Suppress("DEPRECATION")
class ScanFragment : Fragment() {

    private lateinit var binding: FragmentScanBinding
    private val viewModel: ScanViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentScanBinding.inflate(inflater, container, false)
        return (binding.root)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageBitmap = arguments?.getParcelable<Bitmap>("imageBitmap")
        binding.scanImage.setImageBitmap(imageBitmap)
        getData(imageBitmap)

        binding.addButton.setOnClickListener {
            Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.scanImage.setImageDrawable(null)
    }

    private fun classifyImage(image: Bitmap?, resultCallback: (String) -> Unit) {
        try {
            val model: Model = Model.newInstance(requireContext())
            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3)
            byteBuffer.order(ByteOrder.nativeOrder())

            val intValues = IntArray(224 * 224)
            image!!.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)

            var pixel = 0
            for (i in 0 until 224) {
                for (j in 0 until 224) {
                    val `val` = intValues[pixel++] // RGB
                    byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat((`val` and 0xFF) * (1f / 255f))
                }
            }
            inputFeature0.loadBuffer(byteBuffer)

            val outputs: Model.Outputs = model.process(inputFeature0)
            val outputFeature0: TensorBuffer = outputs.outputFeature0AsTensorBuffer
            val confidences = outputFeature0.floatArray

            val classes = loadLabels(requireContext())

            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }

            val result = classes[maxPos]
            resultCallback(result) // Pass the result to the callback function

            model.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadLabels(context: Context): Array<String> {
        val labelsList = ArrayList<String>()
        context.resources.openRawResource(R.raw.labels).bufferedReader().useLines { lines ->
            lines.forEach { line ->
                labelsList.add(line)
            }
        }
        return labelsList.toTypedArray()
    }

    companion object {
        private const val ARG_IMAGE_BITMAP = "arg_image_bitmap"

        fun newInstance(imageBitmap: Bitmap): ScanFragment {
            return ScanFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_IMAGE_BITMAP, imageBitmap)
                }
            }
        }
    }

    private fun getData(image: Bitmap?) {
        classifyImage(image) { imageResult ->
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.getDataScan(imageResult).collect { result ->
                        if (result.isSuccess) {
                            val dataResponse = result.getOrNull()
                            dataResponse?.let { response ->
                                binding.apply {
                                    val scanResponse = response.data.orEmpty().firstOrNull()
                                    scanResponse?.let { data ->
                                        foodNameValue.text = data.mealName
                                        caloriesValue.text = data.calories.toString()
                                        fatValue.text = data.fat.toString()
                                        proteinValue.text = data.protein.toString()
                                        carbsValue.text = data.carb.toString()
                                        foodNameTag.text = data.tag
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}