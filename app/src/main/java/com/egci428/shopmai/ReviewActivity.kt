package com.egci428.shopmai

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.egci428.shopmai.Model.Review
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.util.Locale

class ReviewActivity : AppCompatActivity() {
    lateinit var itemName: TextView
    lateinit var nameEditText: EditText
    lateinit var reviewImage: ImageView
    lateinit var commentEditText: EditText
    lateinit var submitBtn: Button
    lateinit var imgBtn: Button
    lateinit var ratingEditText: EditText

    lateinit var dataReference: FirebaseFirestore

    private var uriKey: Uri? = null
    private lateinit var  outputDirectory: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.review_input)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        itemName = findViewById(R.id.itemName)
        nameEditText = findViewById(R.id.nameEditText)
        reviewImage = findViewById(R.id.reviewImage)
        commentEditText = findViewById(R.id.commentEditText)
        submitBtn = findViewById(R.id.submitBtn)
        imgBtn = findViewById(R.id.imgBtn)
        ratingEditText = findViewById(R.id.starEditText)
        itemName.text = intent.getStringExtra("itemname")

        submitBtn.setOnClickListener {
            val name = nameEditText.text.toString()
            val comment = commentEditText.text.toString()
            val rating = ratingEditText.text.toString().toInt()
            val img = reviewImage.toString()
            val item = itemName.text.toString()
            submitData(item,name,img,rating,comment)
        }
    }
    private fun submitData(item: String, name: String, img: String, rating:Int, comment: String) {
        var db = dataReference.collection("review")
        val messageId = db.document().id
        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(System.currentTimeMillis())
        val reviewData = Review(item, name, date, img, rating,comment)

        db.add(reviewData)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Message is saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(applicationContext, "Fail to save message", Toast.LENGTH_SHORT).show()
            }
        Log.d("test add", reviewData.toString())
    }

    fun takePhoto(view: View) {
        requestCameraPermission.launch(android.Manifest.permission.CAMERA)
    }
    private val requestCameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isSuccess : Boolean ->
        if(isSuccess) {
            Log.d("Take Photo", "Permission Granted")
            takePicture()
        } else {
            Toast.makeText(applicationContext, "Camera has no permission", Toast.LENGTH_LONG).show()
        }
    }
    private fun takePicture() {
        uriKey = getTempFileUri()
        captureImage.launch(uriKey!!)
    }

    private val captureImage = registerForActivityResult(ActivityResultContracts.TakePicture()){
        if(it){
            uriKey.let{
                    uri ->
                if(uri!=null){
                    uriKey = uri
                    reviewImage.setImageURI(uriKey)
                    Log.d("Capture Image",uriKey.toString())
                }
            }
        }
    }

    private fun getTempFileUri(): Uri {
        outputDirectory = getOutputDirectory(this)
        val tmpFile = File.createTempFile(
            SimpleDateFormat(FILENAME, Locale.ENGLISH).format(System.currentTimeMillis()), PHOTO_EXTENSION, outputDirectory).apply {
            createNewFile()
//            deleteOnExit()
        }
        return FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }

    companion object {
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpeg"

        private fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
            }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }
}