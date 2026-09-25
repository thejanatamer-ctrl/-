package com.example.model

enum class DhikrType(
    val id: String,
    val arabicText: String,
    val vocalizedText: String,
    val target: Int,
    val virtue: String,
    val shortLabel: String
) {
    SUBHANALLAH(
        id = "subhanallah",
        arabicText = "سبحان الله",
        vocalizedText = "سُبْحَانَ اللَّهِ",
        target = 33,
        virtue = "كلمتان خفيفتان على اللسان، ثقيلتان في الميزان",
        shortLabel = "تسبيح"
    ),
    ALHAMDULILLAH(
        id = "alhamdulillah",
        arabicText = "الحمد لله",
        vocalizedText = "الْحَمْدُ لِلَّهِ",
        target = 33,
        virtue = "الحمد لله تملأ الميزان، وأفضل الدعاء الحمد لله",
        shortLabel = "تحميد"
    ),
    ALLAHU_AKBAR(
        id = "allahu_akbar",
        arabicText = "الله أكبر",
        vocalizedText = "اللَّهُ أَكْبَرُ",
        target = 33,
        virtue = "أحب الكلام إلى الله، وخير مما طلعت عليه الشمس",
        shortLabel = "تكبير"
    ),
    ASTAGHFIRULLAH(
        id = "astaghfirullah",
        arabicText = "أستغفر الله",
        vocalizedText = "أَسْتَغْفِرُ اللَّهَ",
        target = 100,
        virtue = "من لزم الاستغفار جعل الله له من كل ضيق مخرجاً ومن كل هم فرجاً",
        shortLabel = "استغفار"
    ),
    SALAWAT(
        id = "salawat",
        arabicText = "اللهم صل وسلم على نبينا محمد ﷺ",
        vocalizedText = "اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّنَا مُحَمَّدٍ ﷺ",
        target = 100,
        virtue = "من صلى عليّ صلاة صلى الله عليه بها عشراً وحُطت عنه عشر خطيئات",
        shortLabel = "صلاة على النبي"
    );

    companion object {
        fun fromId(id: String): DhikrType {
            return entries.firstOrNull { it.id == id } ?: SUBHANALLAH
        }
    }
}
