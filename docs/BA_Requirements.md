# ĐẶC TẢ YÊU CẦU ỨNG DỤNG ĐỌC TRUYỆN (LOCAL-FIRST)

## 1. Tổng quan Kiến trúc (Tech Stack)
- **UI Framework:** Kotlin + Jetpack Compose.
- **Navigation:** Jetpack Navigation Compose.
- **Local Database:** Room Database (Lưu trữ User profile ảo, Lịch sử đọc, Yêu thích, Cài đặt).
- **Local Preferences:** Jetpack DataStore (Lưu trạng thái đăng nhập, theme).
- **Network/API:** Retrofit + OkHttp (Call MangaDex API).
- **Image Loading:** Coil.
- **Architecture:** MVVM (Model-View-ViewModel) + Clean Architecture cơ bản (chia layer UI - Domain - Data).

## 2. Thực thể Dữ liệu (Data Entities)

### 2.1. Local Entities (Room Database)
* **UserEntity:** `id` (PK), `username`, `password` (mô phỏng), `avatar_path`, `created_at`.
* **LibraryItemEntity:** (Lưu sách người dùng tương tác)
    * `manga_id` (PK - Lấy từ MangaDex).
    * `title`, `cover_url` (Lưu đệm để hiển thị nhanh không cần call API).
    * `status` (Enum: READING, COMPLETED, FAVORITE).
    * `last_read_chapter_id` (ID chapter đọc gần nhất).
    * `updated_at` (Thời gian tương tác cuối).

### 2.2. Remote Models (MangaDex API)
*(Các model này không lưu DB, chỉ parse từ JSON trả về để map lên UI)*
* **MangaModel:** `id`, `title`, `description`, `cover_art`, `rating`, `tags/genres`.
* **ChapterModel:** `id`, `manga_id`, `chapter_number`, `title`, `pages` (List URL ảnh).

## 3. Phân định Logic (API vs Local DB)
- **MangaDex API đảm nhiệm:** Lấy danh sách truyện ở tab Discover, tìm kiếm truyện ở tab Search, lấy chi tiết truyện (các chapter), và tải ảnh nội dung truyện.
- **Room Database đảm nhiệm:** Xác thực người dùng (đọc/ghi UserEntity), quản lý tab My Library (đọc/ghi LibraryItemEntity khi user thả tim hoặc đọc 1 chapter).

## 4. Luồng Điều hướng (Navigation Flow)

**A. Auth Flow**
- `LoginScreen` (Màn hình đầu tiên nếu DataStore báo chưa đăng nhập) -> Nhập user/pass -> Lưu vào Room -> Chuyển sang Main Flow.

**B. Main Flow (Bottom Navigation với 4 Tabs)**
1.  **Discover Tab (`DiscoverScreen`):**
    - Gọi API lấy danh sách truyện nổi bật/mới cập nhật.
    - Hiển thị danh sách (LazyColumn/LazyVerticalGrid).
    - Click vào 1 truyện -> Mở `MangaDetailPopup/Screen`.
2.  **Search Tab (`SearchScreen`):**
    - Ô nhập liệu (TextField). Gọi API Search MangaDex theo keyword.
    - Hiển thị danh sách kết quả (chỉ hiện title, cover, rating ngắn gọn).
    - Click vào kết quả -> Mở `MangaDetailPopup/Screen`.
3.  **My Library Tab (`LibraryScreen`):**
    - Có Top Tab/Filter: Lịch sử (Đang đọc/Đã đọc) & Yêu thích.
    - Query từ Room DB bảng `LibraryItemEntity`.
    - Click vào truyện -> Mở thẳng Chapter đang đọc dở (hoặc mở Detail).
4.  **User Setting Tab (`SettingScreen`):**
    - Hiển thị thông tin UserEntity.
    - Nút Xóa Cache (Clear ảnh lưu bởi Coil/Data).
    - Nút Đăng xuất (Xóa trạng thái trong DataStore, quay về Auth Flow).

**C. Detail & Reader Flow**
- `MangaDetailPopup/Screen`: Hiển thị ảnh, mô tả, đánh giá từ API. Nút "Đọc ngay", nút "Yêu thích" (lưu vào Room). Danh sách chapter.
- `ReaderScreen`: Hiển thị các trang ảnh của 1 chapter. Khi vuốt hết chapter, tự động cập nhật `last_read_chapter_id` xuống Room DB.