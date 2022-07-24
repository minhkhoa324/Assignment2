# Assignment 2

## ROM (Android OS)

### ROM là gì?

ROM (Android OS) là hệ điều hành giúp chạy thiết bị và được lưu trong ROM (Read only memory). Ta có Google
Pixel sử dụng stock Android chưa được tuỳ biết. Ngoài ra các nhà sản xuất thường
tuỳ chỉnh ROM riêng cho họ như _MIUI_

Việc tuỳ biến ROM có thể thay đổi 1 số tuỳ tính năng, tối ưu hoá phần mềm.

ROM có 2 partition chính:

- System partition (/system)
- Userdata partition (/userdata)

App theo ROM được cài trong /system.
App được người tải thì sẽ nằm trong /userdata.
Và bản bản cập nhật cho áp hệ thống cũng được cài trong /userdata.

## App và ROM

Mỗi app Android lives in its own security box, được bảo vệ bởi cách tính năng Android sau:

- Android OS là hệ điều hành Linux đa người dùng, với mỗi app là 1 người dùng.
- Mặc định, hệ thống cấp cho mỗi app 1 User ID riêng (chỉ hệ thống biết). Và hệ
  thống set quyền cho tất cả các file trong app và chỉ có ID theo app đó được
  truy cập.
- Mỗi process có VM riêng, nên code của mỗi app chạy tách biết khỏi nhau.
- Mặc định thì mỗi app chạy trong process của riêng nó. Android system chạy process
  khi mà app component cần executed, và tắt khi không cần để thu hồi bộ nhớ.

Mặc dù như thế nhưng:

- Hai app vẫn có thể chia sẻ file cho nhau. nhưng phải có cùng certificate.
- Ta vẫn có thể cấp quyền truy cập cho app.

## Sim card

Trong Android thì Sim card được quản lý bới API _TelephonyManager_.

Với API này thì app có thể xác định được state và service, cũng như là truy cập vào
một số loại thông tin nhất định.

```Java
telephonyManager = defaultSubTelephonyManager.createForSubscriptionID(subId)
```

Cần sử dụng **FEATURE_TELPHONY**

Còn cách lệnh và tham số xem thêm tại [đây](https://developer.android.com/reference/android/telephony/TelephonyManager)

Tìm hiểu UIIC (Universal Integrated Circuit)

## Calling

Có trong framework _android.telecom_

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

```xml
<manifest ...>
    <uses-permission android:name="android.permission.CAMERA"/>
    <application ...>
        ...
    </application>
</manifest>
```

```xml
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

## Cách chỉnh dependencies

Chỉnh **dependencies** trong file **build.gradle** trong mục **dependencies**

Ví dụ ta thêm _dependencies_ của _camerax_.

```java
    def camerax_version = '1.1.0'
    implementation "androidx.camera:camera-core:${camerax_version}"
    implementation "androidx.camera:camera-camera2:${camerax_version}"
    implementation "androidx.camera:camera-lifecycle:${camerax_version}"
    implementation "androidx.camera:camera-view:${camerax_version}"
    implementation "androidx.camera:camera-extensions:${camerax_version}"
```

## Camera

Về Camera thì ta có API _CameraX_, đây là một API trong bộ _Jetpack library_,
đây là một API tốt.
Để sử dụng cameraX thì ta cũng xin cấp quyền camera, kèm với đó là chỉnh
dependencies. Và ta sử _CameraProviders_

## ArrayAdapter

Kết nối source -> list view

```Java
ArrayAdapter<String> itemsAdapter =
    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
```

```Java
ListView listView = (ListView) findViewById(R.id.lvItems);
listView.setAdapter(itemsAdapter);
```

## Bluetooth

Tất cả API của Bluetooth nằm trong _android.bluetooth_
API chính là _BluetoothAdapter_

Xem thêm tại [đây](https://developer.android.com/guide/topics/connectivity/bluetooth)

## Wifi

API chính là _WifiManager_

Quyền

```xml
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
```

## Cách ta cài đặt một chương trình bằng lệnh

Trước hết là ta phải xin quyền cài đặt như sau:

```xml
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

Tạo một _provider_

```xml
<provider
        android:name="androidx.core.content.FileProvider"
        android:authorities="${applicationId}.provider"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/provider_paths" />
    </provider>
```

Tạo ra thư mục để ta có thể lưu app

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path
        name="files_root"
        path="Android/data/${applicationId}" />
    <external-path
        name="external_files"
        path="." />
</paths>
```

**Từ đây ta sẽ có logic để viết 1 app store như sau**

- Ta sẽ tải file xuống từ server.
- Sau đó cài đặt như trên.
- Trong quá trình cài đặt nếu người dùng huỷ hoặc xảy ra sự cố thì ta huỷ và xoá
  file.

## Thread và Multi Threading trong Android

Tất cả Android app dùng _Main Thread_ để chạy UI operations. Gọi một _long-running
operation_ có thể dẫn tới đứng app hoặc không phản hồi.

Hiện tại thì _Async_ đã bị deprecated và ta sẽ dụng _Executor_ để thay thế.

Syntax của _Executor_ như sau:

```Java
 class SerialExecutor implements Executor {
   final Queue<Runnable> tasks = new ArrayDeque<>();
   final Executor executor;
   Runnable active;

   SerialExecutor(Executor executor) {
     this.executor = executor;
   }

   public synchronized void execute(Runnable r) {
     tasks.add(() -> {
       try {
         r.run();
       } finally {
         scheduleNext();
       }
     });
     if (active == null) {
       scheduleNext();
     }
   }

   protected synchronized void scheduleNext() {
     if ((active = tasks.poll()) != null) {
       executor.execute(active);
     }
   }
 }
```
