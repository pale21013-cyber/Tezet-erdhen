# ========================================================
# R8 / ProGuard Aggressive Obfuscation & Shrinking Rules
# Scrambles class, method, and variable names into single letters
# ========================================================

-repackageclasses 'a'
-allowaccessmodification
-overloadaggressively

# Keep Room Database, DAOs and Entities
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase { *; }
-dontwarn androidx.room.paging.**

# Keep Compose internal stability markers
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}

# Keep Application, Activities and Receivers
-keep public class com.example.AuraApplication
-keep public class com.example.MainActivity
-keep public class com.example.widget.AuraCycleWidgetProvider

# Keep FileProvider
-keep public class androidx.core.content.FileProvider

