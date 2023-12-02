plugins {
  // Root Plugins
  id("io.bkbn.sourdough.root") version "0.12.0"

  // Child Plugins
  kotlin("jvm") version "1.7.22" apply false
  kotlin("plugin.serialization") version "1.7.22" apply false
  id("io.bkbn.sourdough.application.jvm") version "0.12.0" apply false
  id("io.gitlab.arturbosch.detekt") version "1.22.0" apply false
  id("com.adarshr.test-logger") version "3.2.0" apply false
}

allprojects {
  group = "io.github.unredundant"
  version = "0.1.0"
  plugins.withType(JavaPlugin::class.java) {
    configure<JavaPluginExtension> {
      toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
      }
    }
  }
}
