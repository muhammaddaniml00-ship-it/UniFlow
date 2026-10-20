# UniFlow — Student Task Manager

Aplikasi Android native untuk manajemen tugas mahasiswa, dibuat sebagai tugas mata kuliah **Pengembangan Aplikasi Mobile** semester 2026/2027.

---

## 📱 Deskripsi
[settings.gradle](settings.gradle)
UniFlow adalah aplikasi manajemen tugas mahasiswa yang dirancang dengan tampilan modern bertema hitam/ungu gelap. Aplikasi ini membantu mahasiswa mengorganisir tugas kuliah, memantau progres, dan mengelola profil akademik — semuanya tersimpan secara lokal menggunakan SQLite.

---

## ✨ Fitur

### 🏠 Home / Dashboard
- Sapaan pengguna berdasarkan profil yang tersimpan
- Kartu ringkasan: Total tugas, Belum Selesai, dan Selesai
- Daftar 5 tugas terbaru yang masih aktif
- Tombol navigasi cepat ke halaman My Tasks
- Semua data real-time dari database SQLite

### 📋 My Tasks
- Tampilkan seluruh daftar tugas
- Tambah tugas baru (nama tugas, mata kuliah, tenggat)
- Edit dan hapus tugas dengan konfirmasi dialog
- Tandai tugas selesai/belum selesai (checkbox)
- Validasi input: nama tugas tidak boleh kosong
- Date picker untuk memilih tenggat
- Empty state dengan ilustrasi ketika belum ada tugas

### 👤 Profile
- Tampilkan nama, NIM, dan program studi
- Form edit profil (dialog)
- Statistik tugas selesai dan belum selesai
- Data tersimpan persisten di SQLite

### 🔧 Teknis
- Bottom Navigation Bar dengan 3 tab
- Semua data tersimpan lokal (SQLite) — tetap ada setelah aplikasi ditutup
- CRUD lengkap untuk tugas
- Validasi input dan empty state

---

## 🛠️ Teknologi

| Komponen | Detail |
|---|---|
| Bahasa | Java |
| Layout | XML |
| Database | SQLite (SQLiteOpenHelper) |
| UI | Material Components, CardView, RecyclerView |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 34 (Android 14) |
| Package | `com.student.uniflow` |

---

## 📁 Struktur Proyek

```
UniFlow/
├── app/
│   └── src/main/
│       ├── java/com/student/uniflow/
│       │   ├── MainActivity.java              # Entry point + BottomNav
│       │   ├── AddEditTaskActivity.java        # Activity tambah/edit tugas (cadangan)
│       │   ├── model/
│       │   │   ├── Task.java                   # Model data tugas
│       │   │   └── UserProfile.java            # Model data profil
│       │   ├── database/
│       │   │   └── DatabaseHelper.java         # SQLiteOpenHelper + CRUD
│       │   ├── adapter/
│       │   │   ├── TaskAdapter.java            # RecyclerView adapter for tasks
│       │   │   └── RecentTaskAdapter.java      # RecyclerView adapter for recent tasks
│       │   └── fragment/
│       │       ├── HomeFragment.java           # Halaman dashboard
│       │       ├── TasksFragment.java          # Halaman daftar tugas
│       │       └── ProfileFragment.java        # Halaman profil
│       ├── res/
│       │   ├── layout/                         # XML layout files
│       │   ├── values/                         # colors, strings, themes, dimens
│       │   ├── drawable/                       # Background shapes, icons
│       │   └── menu/                           # Bottom navigation menu
│       └── AndroidManifest.xml
├── build.gradle                                # Root build config
├── app/build.gradle                            # App-level build config
├── settings.gradle
├── gradle.properties
├── gradlew / gradlew.bat
└── README.md
```

---

## 📋 Persyaratan

- **Android Studio** Hedgehog (2023.1.1) atau lebih baru
- **JDK** 8 atau lebih baru (disarankan JDK 17)
- **Android SDK** dengan API 34 ter-install
- **Gradle** 8.0+ (diunduh otomatis oleh Gradle Wrapper)

---

## 🚀 Cara Menjalankan Aplikasi

### 1. Clone Repository
```bash
git clone https://github.com/<username>/UniFlow.git
cd UniFlow
```

### 2. Buka di Android Studio
1. Buka Android Studio
2. Pilih **File → Open**
3. Arahkan ke folder `UniFlow`
4. Tunggu Gradle sync selesai

### 3. Jalankan di Emulator / Device
1. Pastikan emulator aktif atau device terhubung via USB (dengan USB Debugging)
2. Klik tombol **Run ▶** atau tekan `Shift+F10`

---

## 🏗️ Cara Build APK Debug

### Via Android Studio
1. Pilih **Build → Build Bundle(s) / APK(s) → Build APK(s)**
2. Tunggu proses selesai
3. APK tersedia di: `app/build/outputs/apk/debug/app-debug.apk`

### Via Command Line (Terminal di root project)
```bash
# Windows
.\gradlew.bat assembleDebug

# macOS / Linux
./gradlew assembleDebug
```
APK output: `app/build/outputs/apk/debug/app-debug.apk`

---

## 🎨 Desain

- **Tema**: Dark mode (hitam/ungu gelap)
- **Aksen**: Ungu neon (`#BB86FC`)
- **Background**: `#0D0D1A`
- **Card Background**: `#1A1A2E`
- **Font**: Default Android (Roboto)
- **Corner Radius**: 16dp (kartu), 12dp (elemen kecil)

---

## 📝 Catatan

- Aplikasi tidak memerlukan koneksi internet
- Data tersimpan sepenuhnya di perangkat (SQLite lokal)
- Tidak ada login atau autentikasi
- Kompatibel dengan Android 7.0+ (API 24+)

---

## 👨‍💻 Pembuat

**Mata Kuliah**: Pengembangan Aplikasi Mobile  
**Semester**: 2026/2027  
**Package**: `com.student.uniflow`
