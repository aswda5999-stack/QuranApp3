package com.example.qurannative.audio

/**
 * بيانات قارئ القرآن الكريم.
 *
 * audioBaseUrl: رابط مجلد الصوتيات الخاص بالقارئ.
 * نتركه فارغاً إلى حين إضافة روابط مصدر صوتي موثوق.
 */
data class Reciter(
    val id: String,
    val name: String,
    val audioBaseUrl: String,
    val riwayah: String = "حفص عن عاصم",
    val country: String = ""
)

/**
 * قائمة القراء المتاحة داخل التطبيق.
 */
object Reciters {

    val all = listOf(
        Reciter(
            id = "abdulbasit",
            name = "عبد الباسط عبد الصمد",
            audioBaseUrl = "",
            riwayah = "حفص عن عاصم",
            country = "مصر"
        ),

        Reciter(
            id = "mishary",
            name = "مشاري العفاسي",
            audioBaseUrl = "",
            riwayah = "حفص عن عاصم",
            country = "الكويت"
        ),

        Reciter(
            id = "maher",
            name = "ماهر المعيقلي",
            audioBaseUrl = "",
            riwayah = "حفص عن عاصم",
            country = "السعودية"
        ),

        Reciter(
            id = "sudais",
            name = "عبد الرحمن السديس",
            audioBaseUrl = "",
            riwayah = "حفص عن عاصم",
            country = "السعودية"
        ),

        Reciter(
            id = "shuraim",
            name = "سعود الشريم",
            audioBaseUrl = "",
            riwayah = "حفص عن عاصم",
            country = "السعودية"
        ),

        Reciter(
            id = "husary",
            name = "محمود خليل الحصري",
            audioBaseUrl = "",
            riwayah = "حفص عن عاصم",
            country = "مصر"
        )
    )

    fun findById(id: String): Reciter? {
        return all.find { it.id == id }
    }
}
