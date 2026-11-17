package com.example.qurannative.quran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Surah(
    val number: Int,
    val name: String,
    val englishName: String,
    val revelationType: String,
    val numberOfAyahs: Int
)

data class Ayah(
    val number: Int,
    val numberInSurah: Int,
    val text: String,
    val juz: Int,
    val page: Int
)

data class QuranUiState(
    val surahs: List<Surah> = emptyList(),
    val selectedSurah: Surah? = null,
    val ayahs: List<Ayah> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class QuranViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(QuranUiState())
    val uiState: StateFlow<QuranUiState> = _uiState.asStateFlow()

    init {
        loadSurahs()
    }

    /**
     * تحميل أسماء السور.
     *
     * هذه بيانات أولية، وسيتم ربطها بقاعدة بيانات القرآن
     * الكاملة في الملفات القادمة.
     */
    private fun loadSurahs() {
        val names = listOf(
            "الفاتحة", "البقرة", "آل عمران", "النساء",
            "المائدة", "الأنعام", "الأعراف", "الأنفال",
            "التوبة", "يونس", "هود", "يوسف",
            "الرعد", "إبراهيم", "الحجر", "النحل",
            "الإسراء", "الكهف", "مريم", "طه",
            "الأنبياء", "الحج", "المؤمنون", "النور",
            "الفرقان", "الشعراء", "النمل", "القصص",
            "العنكبوت", "الروم", "لقمان", "السجدة",
            "الأحزاب", "سبأ", "فاطر", "يس",
            "الصافات", "ص", "الزمر", "غافر",
            "فصلت", "الشورى", "الزخرف", "الدخان",
            "الجاثية", "الأحقاف", "محمد", "الفتح",
            "الحجرات", "ق", "الذاريات", "الطور",
            "النجم", "القمر", "الرحمن", "الواقعة",
            "الحديد", "المجادلة", "الحشر", "الممتحنة",
            "الصف", "الجمعة", "المنافقون", "التغابن",
            "الطلاق", "التحريم", "الملك", "القلم",
            "الحاقة", "المعارج", "نوح", "الجن",
            "المزمل", "المدثر", "القيامة", "الإنسان",
            "المرسلات", "النبأ", "النازعات", "عبس",
            "التكوير", "الانفطار", "المطففين", "الانشقاق",
            "البروج", "الطارق", "الأعلى", "الغاشية",
            "الفجر", "البلد", "الشمس", "الليل",
            "الضحى", "الشرح", "التين", "العلق",
            "القدر", "البينة", "الزلزلة", "العاديات",
            "القارعة", "التكاثر", "العصر", "الهمزة",
            "الفيل", "قريش", "الماعون", "الكوثر",
            "الكافرون", "النصر", "المسد", "الإخلاص",
            "الفلق", "الناس"
        )

        val surahs = names.mapIndexed { index, name ->
            Surah(
                number = index + 1,
                name = name,
                englishName = "",
                revelationType = "",
                numberOfAyahs = 0
            )
        }

        _uiState.value = _uiState.value.copy(
            surahs = surahs,
            isLoading = false
        )
    }

    /**
     * اختيار سورة.
     */
    fun selectSurah(surah: Surah) {
        _uiState.value = _uiState.value.copy(
            selectedSurah = surah,
            ayahs = emptyList(),
            error = null
        )
    }

    /**
     * البحث عن سورة بالاسم.
     */
    fun searchSurahs(query: String): List<Surah> {
        if (query.isBlank()) {
            return _uiState.value.surahs
        }

        return _uiState.value.surahs.filter {
            it.name.contains(query.trim(), ignoreCase = true)
        }
    }

    /**
     * الانتقال إلى سورة حسب رقمها.
     */
    fun selectSurahByNumber(number: Int) {
        val surah = _uiState.value.surahs
            .find { it.number == number }

        if (surah != null) {
            selectSurah(surah)
        }
    }

    /**
     * العودة لقائمة السور.
     */
    fun clearSelection() {
        _uiState.value = _uiState.value.copy(
            selectedSurah = null,
            ayahs = emptyList(),
            error = null
        )
    }
}
