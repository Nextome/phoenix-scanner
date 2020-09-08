# phoenix-scanner
A BLE beacon scanner for Nextome SDK.

## Include
1. Add JitPack in your **root build.gradle** at the end of repositories:
```gradle
  allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
  }
```
2. Add the dependency in your **module build.gradle**:
```gradle
  dependencies {
      implementation 'com.github.Nextome:phoenix-scanner:master-SNAPSHOT'
  }

```
