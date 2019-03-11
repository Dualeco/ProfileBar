# ProfileBar
Custom animated AppbarLayout designed as a profile screen

![zoom_resized](https://user-images.githubusercontent.com/31614124/54141780-fc161200-442e-11e9-84e6-225942c3d5b4.gif)

## Supported Android versions
* API 22 and higher

## Supported technologies
* Databinging
* Livedata

## Features
* Collapsing toolbar
* Zoomable photo image
* Tabs pager
* Option menu
* Landscape and portrait orientations

## Setup

### Adding a dependency
1. In *build.gradle(Project)* add as follows:
```
allprojects {
    repositories {
    ...
    maven {
        url "https://jitpack.io"
    }
```
2. In *build.gradle(Module)* add the following code:
  * If you need databinding:
```
android {
    ...
    dataBinding {
        enabled = true
    }
}

...

implementation "com.github.DichotoMe.ProfileBar:profilebarBinding:1.1"
```
  * Otherwise:
```
implementation "com.github.DichotoMe.ProfileBar:profilebar:1.1"
```
