plugins {
	id("com.android.library")
	id("kotlin-android")
	id("kotlin-parcelize")
	id("org.jetbrains.kotlin.android")
	id("dagger.hilt.android.plugin")
	id("kotlin-kapt")
}

android {
	namespace = BuildConfig.core
	compileSdk = BuildConfig.compileSdkVersion

	defaultConfig {
		minSdk = BuildConfig.minSdkVersion
		targetSdk = BuildConfig.targetSdkVersion

		consumerProguardFiles("consumer-rules.pro")
	}

	buildFeatures {
		dataBinding = true
	}

	buildTypes {
		getByName("debug"){
			buildConfigField("String", "AUTH_TOKEN", "\"33271844-7b2839d324c4204526d4459e2\"")
			buildConfigField("String", "BASE_URL", "\"https://pixabay.com/\"")
		}
		getByName("release") {
			buildConfigField("String", "AUTH_TOKEN", "\"33271844-7b2839d324c4204526d4459e2\"")
			buildConfigField("String", "BASE_URL", "\"https://pixabay.com/\"")
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
}

dependencies {
	implementation(project(Module.styles))

	implementation(KotlinDependencies.coreKtx)
	implementation(AndroidXSupportDependencies.appCompat)
	implementation(MaterialDesignDependencies.materialDesign)

	HiltDependency()
	NetworkDependency()

	GlideDependency()

	implementation(Libraries.sdp)
	implementation(Libraries.ssp)
}