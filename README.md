# Assignment 2

Tìm hiểu về ngoại vi của hệ điều hành Android (camera, wifi, bluetooth, sim, ...)

Trước tiên ta có cấu trúc của Android như sau:

<img src='./images/picture_1.png' width=400dp align='center'>

Trong Assignment này ta sẽ tim hiểu về **HAL**

- A **HAL** defines a standard interface for hardware vendors to implement, which
  enables Android to be agnostic about low-level driver implementations. Using a HAL
  allows you to implement functionality without affecting or modifying the higher
  level system. HAL implementations are packaged into modules and loaded by the Android
  system at the appropriate time.

## Binderized HALs

Android requires the following HALS to to be binderized on all Android devices
regardless of whether they are launch devices or upgrade devices:

- android.hardware.bimetrics.fingerprint@2.1.
- android.hardware.configstore@1.0
- android.hardware.dumpstate@1.0
- android.hardware.graphics.allocator@2.0
- android.hardware.radio@1.0
- android.hardware.usb@1.0
- android.hardware.wifi@1.0
- android.hardware.wifi.supplicant@1.0

## Passthrough HALs

Android requires the following HALs to be in passthrough mode on all Android devices
regardless of whether they are launch devices or upgrade devices:

- android.hardware.graphics.mapper@1.0
- android.hardware.renderscript@1.0

## Same-Process HALs

Same-Process HALS (SP-HALs) always open in the same process in which they are used.
They include all HALs not expressed in HIDL as well as some that not binderized.
Membership in the SP-HAL set is controlled only by Google, with no exceptions.

SP-HALs include the following:

- openGL
- Vulkan
- android.hidl.memory@1.0
- android.hardware.graphics.mapper@1.0
- android.hardware.renderscript@1.0

## Conventional & legacy HALs

Conventional HALs (deprecated in Android 8.0) are interfaces that conform to a specific
application binary interface (ABI). The bulk of Android system interfaces (camera,
audio, sensors, etc.) are in the form of conventional HALs)

Legacy HALs (also deprecated in Android 8.0) are interfaces that predate conventional HALs.
A few import subsystem (Wi-Fi, Radio, Interface Layer, and Bluetooth) are legacy HALs.
While there's no uniform or standardized way to describe a legacy HAL, anything
predating Android 8.0 that is not a conventional HAL is legacy HAL. Parts of some

##

Trong quá trình sử dụng thì app phải xin quyền đề xử dụng các thiệt bị đó (truy cập
bộ nhớ, danh bạ, camera, ...) hoặc chỉnh **dependencies** trong file **build.gradle**
trong mục **dependencies**

Ví dụ ta thêm _dependencies_ của _camerax_.

```Java
    def camerax_version = '1.1.0'
    implementation "androidx.camera:camera-core:${camerax_version}"
    implementation "androidx.camera:camera-camera2:${camerax_version}"
    implementation "androidx.camera:camera-lifecycle:${camerax_version}"
    implementation "androidx.camera:camera-view:${camerax_version}"
    implementation "androidx.camera:camera-extensions:${camerax_version}"
```

Thì đề xin quyền ta có logic như sau:

- Kiểm tra đã được cấp phép quyền hay chưa.
- Nếu đã được cấp phép thì ta tiếp tục còn không thì ta phải xin quyền.

Ví dụ về hàm kiểm tra quyền

```Java
    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
```

Còn đây là hàm xin quyền

```Java
        private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.CAMERA},
                MY_CAMERA_REQUEST_CODE);
    }

```
