# ProfileBar
Custom animated AppbarLayout designed as a profile screen

![zoom_resized](https://user-images.githubusercontent.com/31614124/54141780-fc161200-442e-11e9-84e6-225942c3d5b4.gif)

## Supported Android versions
* API 22 and higher
Earlier versions are to be added soon

## Supported technologies
* Databinding
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

### Building a layout
Place a ProfileBar and a TabPager inside a **CoordinatorLayout** as follows:
```
<androidx.coordinatorlayout.widget.CoordinatorLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/coordinator"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.dichotome.profilebar.ui.profileBar.ProfileBar
        android:id="@+id/profileBar"
        android:layout_width="match_parent"
        android:layout_height="300dp" />

    <com.dichotome.profilebar.ui.tabPager.TabPager
        android:id="@+id/profilePager"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="@string/appbar_scrolling_view_behavior" />

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```
**Note:** if you are using databinding, set `<layout>` as your layout root and specify the data you are going to bind:
```
<layout>
    <data>
        <variable ... />
    </data>
    
    ...
    <androidx.coordinatorlayout.widget.CoordinatorLayout
        android:id="@+id/coordinator"...>
    ...
</layout>
```
