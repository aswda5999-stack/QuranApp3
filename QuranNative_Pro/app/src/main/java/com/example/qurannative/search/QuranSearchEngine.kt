package com.example.qurannative.search

data class QuranSearchEntry(
    val surahNumber: Int,
    val surahName: String,
    val ayahNumber: Int,
    val text: String
)

data class QuranSearchResult(
    val entry: QuranSearchEntry,
    val matchedText: String
)

object QuranSearchEngine {

    fun normalizeArabic(text: String): String {
        return text
            .lowercase()
            .replace(Regex("[\\u064B-\\u065F\\u0670]"), "")
            .replace("ـ", "")
            .replace("أ", "ا")
            .replace("إ", "ا")
            .replace("آ", "ا")
            .replace("ٱ", "ا")
            .replace("ى", "ي")
            .replace("ؤ", "و")
            .replace("ئ", "ي")
            .replace("ة", "ه")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    fun search(
        query: String,
        entries: List<QuranSearchEntry>,
        limit: Int = 100
    ): List<QuranSearchResult> {

        val normalizedQuery = normalizeArabic(query)

        if (normalizedQuery.isBlank()) {
            return emptyList()
        }

        return entries.asSequence()
            .filter { entry ->
                normalizeArabic(entry.text)
                    .contains(normalizedQuery)
            }
            .take(limit.coerceAtLeast(1))
            .map { entry ->
                QuranSearchResult(
                    entry = entry,
                    matchedText = entry.text
                )
            }
            .toList()
    }
}
