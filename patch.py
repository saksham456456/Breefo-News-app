import re

with open('app/build.gradle.kts', 'r') as f:
    content = f.read()

signing_config = """
    signingConfigs {
        create("release") {
            storeFile = file("release-keystore.jks")
            storePassword = "android123"
            keyAlias = "my-key-alias"
            keyPassword = "android123"
        }
    }

    buildTypes {"""

content = content.replace('    buildTypes {', signing_config)

build_type_release = """        release {
            signingConfig = signingConfigs.getByName("release")"""

content = content.replace('        release {', build_type_release)

with open('app/build.gradle.kts', 'w') as f:
    f.write(content)
