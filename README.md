# 📄 PageSkim (Version 1.1)

### ⚙ Basic Introduction 
This is an ongoing project, all features have been added at this stage except ai genaration text. (will release in Version 2.1)

---

### 🔧 How to use PageSkim

### Install
- **From Releases**: Download the latest `PageSkim.apk` from the **Releases** tab on this repo and install on your Android device (you may need to allow "Install from unknown sources").
- **From source**:
  1. Clone the repo: `git clone https://github.com/actualakib/pageskim.git`
  2. Open the project in Android Studio.
  3. Click **File → Sync Project with Gradle Files**, then **Build → Run 'app'** on a connected device or emulator.

> Pro tip: to install an APK from your computer via USB: `adb install path/to/pageskim-1.1.apk`.

### Permissions
When you run the app the first time it will request **Camera** permission. Please allow it — the app needs camera access to capture photos for OCR.

### Quick user flow
1. **Open Camera (Capture)** — Tap the *Open Camera* button. The image-cropper chooser will let you take a photo or pick an image from the gallery.
2. **Crop** — Crop the area you want the OCR to read, then confirm.
3. **OCR & Results** — The app runs OCR and displays the extracted text in the large text area.
4. **Retake** — If the result is poor, tap **RETAKE** to capture/crop again.
5. **Copy** — Tap **COPY** to copy the recognized text to clipboard. After copying the UI returns to the initial state: **Open Camera** visible; **RETAKE** and **COPY** hidden.
6. **No new layout** — Everything happens in the same activity — no screen switching.

### Troubleshooting
- **App crashes when tapping Capture**: ensure Camera permission is granted. If it still crashes, send the Logcat `FATAL EXCEPTION` stacktrace in an issue.
- **Cropper not opening / missing imports**: make sure Gradle sync succeeded and dependencies (`com.github.CanHub:Android-Image-Cropper` or configured artifact) are present.
- **OCR returns empty text**: try a clearer photo (good lighting, straight paper) or test with printed text.



---

### 🛠 About the Project

PageSkim is a lightweight Android app designed to capture text from images, extract clean OCR content, and generate a short, readable summary. Perfect for quickly understanding notes, pages, or documents on the go.

---

### 🚀 All Features

- 📸 Capture images using the device camera  
- 🔍 Extract text (OCR) using Google ML Kit  
- ✂ Auto-clean & format the extracted text  
- 📋 Copy output with one tap  
- 🪶 Simple UI, fast performance  
- 🔒 Fully offline, no server required

---

### Stay tuned for updates!  @ADNAN AKIB
