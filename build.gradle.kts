import com.replaymod.gradle.preprocess.PreprocessExtension
import moe.nea.rxcraft.build.Versions
import moe.nea.rxcraft.build.parseEnvFile
import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
	id("java")
	kotlin("jvm")
}

val version = Versions.values().find { it.projectPath == project.path }!!
if (version.forgeDep != null)
	extra.set("loom.platform", "forge")
apply(plugin = "gg.essential.loom")
apply(plugin = "com.replaymod.preprocess")

val loom = the<LoomGradleExtensionAPI>()
val preprocess = the<PreprocessExtension>()

if (version.needsPack200) {
	loom.forge.pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
}

preprocess.run {
	vars.put("FORGE", if ((version.forgeDep != null)) 1 else 0)
}

repositories {
	mavenCentral()
	maven("https://maven.minecraftforge.net") {
		metadataSources {
			artifact()
		}
	}
}

loom.run {
	this.runs {
		this.removeIf { it.name != "client" }
		this.named("client") {
			parseEnvFile(file(".env")).forEach { (t, u) ->
				this.environmentVariable(t, u)
			}
			parseEnvFile(file(".properties")).forEach { (t, u) ->
				this.property(t, u)
			}
		}
	}
}

dependencies {
	"minecraft"("com.mojang:minecraft:" + version.minecraftVersion)
	"mappings"(version.mappingDependency)
	if (version.forgeDep != null) {
		"forge"(version.forgeDep!!)
	} else {
		"modImplementation"("net.fabricmc:fabric-loader:0.15.10")
	}
}
