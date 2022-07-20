# Assignment 2

## Tìm hiểu về quyền

Quyền bảo vệ người dụng và bảo vệ quyền truy cập vào

- **Restricted data**, trạng thái của hệ thống và thông tin người dùng.
- **Restricted actions**, ghi, thu và kết nối.

Biểu đồ mô tả về quyền trong Android

<img src="./images/permission_1.svg" width=400dp>

Các loại quyền:

- Install-time permission
- Normal permission
- Signature permission
- Runtime permission
- Special permission

## Khi nào chúng ta cần xin quyền

- Cần truy cập địa chỉ
- Truy cập camera hay chụp hình quay phim
- Mở media, các văn bản, ...
- Bluetooth
- Danh bạ
  ....

## Cách thức khai báo quyền

```Java
<manifest ...>
    <uses-permission android:name="android.permission.CAMERA"/>
    <application ...>
        ...
    </application>
</manifest>
```

Yêu cầu như là phụ

```Java
<manifest ...>
    <application>
        ...
    </application>
    <uses-feature android:name="android.hardware.camera"
                  android:required="false" />
<manifest>
```

### Logic xin quyền

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
