data class Meditation(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val duration: Int = 0,
    val mood: MeditationType = MeditationType.CALM,
    val audioPath: String = ""
)

enum class MeditationType {
    MOOD,
    ENERGIC,
    CALM,
    MEMORIES
}