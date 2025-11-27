package com.example.pageskim;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_CODE = 100;
    private CardView cardButton;
    private Button buttonCopy, buttonRetake;
    private TextView textView_data;

    private ActivityResultLauncher<CropImageContractOptions> cropImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cardButton = findViewById(R.id.card_button);
        buttonCopy = findViewById(R.id.copy_clip);
        buttonRetake = findViewById(R.id.retake);
        textView_data = findViewById(R.id.textView_data);
        // initial UI
        cardButton.setVisibility(View.VISIBLE);
        buttonRetake.setVisibility(View.GONE);
        buttonCopy.setVisibility(View.GONE);
        checkCameraPermission();
        // open camera
        cropImageLauncher = registerForActivityResult(new CropImageContract(), result -> {
            if (result.isSuccessful()) {
                Uri uriContent = result.getUriContent();
                if (uriContent != null) {
                    try {
                        // fix1: better bitmap loading for newer android versions
                        Bitmap bitmap;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uriContent));
                        } else {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriContent);
                        }
                        //  ocr
                        get_text(bitmap);
                        // vice-versa UI
                        cardButton.setVisibility(View.GONE);
                        buttonRetake.setVisibility(View.VISIBLE);
                        buttonCopy.setVisibility(View.VISIBLE);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Exception error = result.getError();
                if (error != null) {
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        cardButton.setOnClickListener(v -> launchCropImage());
        buttonRetake.setOnClickListener(v -> launchCropImage());

        buttonCopy.setOnClickListener(v -> {
            String text = textView_data.getText().toString();
            if (!text.isEmpty()) {
                textToClip(text);
                // reset UI
                textView_data.setText(""); // Clear text, can actually print something else !!!
                cardButton.setVisibility(View.VISIBLE);
                buttonRetake.setVisibility(View.GONE);
                buttonCopy.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "No text to copy", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void launchCropImage() {
        CropImageOptions cropOptions = new CropImageOptions();
        cropOptions.imageSourceIncludeGallery = true;
        cropOptions.imageSourceIncludeCamera = true;

        // fix3: ensure visibility
        cropOptions.activityTitle = "Crop Image";
        cropOptions.activityMenuIconColor = -16777216; // Black color integer
        cropOptions.toolbarColor = -1;       // White color integer
        cropOptions.toolbarTitleColor = -16777216; // Black title

        CropImageContractOptions contractOptions = new CropImageContractOptions(null, cropOptions);
        cropImageLauncher.launch(contractOptions);
    }

    private void get_text(Bitmap bitmap) {
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if (!recognizer.isOperational()) {
            Toast.makeText(this, "Dependencies are not yet available.", Toast.LENGTH_LONG).show();
            return;
        }
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> items = recognizer.detect(frame);
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            TextBlock item = items.valueAt(i);
            if (item != null && item.getValue() != null) {
                stringBuilder.append(item.getValue()).append("\n");
            }
        }
        textView_data.setText(stringBuilder.toString());
    }

    private void textToClip(String str) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("OCR Text", str);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Text Copied!", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }
}