# ProfileBar
A beautiful animated profile screen with a zooming photo and tabs.
Implemented as an AppBar with a ViewPager

![zoom_resized](https://user-images.githubusercontent.com/31614124/54141780-fc161200-442e-11e9-84e6-225942c3d5b4.gif)

### Note: the animation *always* starts from the place it has been triggered!
# Try it yourself!
#### [Download the apk](https://github.com/DichotoMe/ProfileBar/releases/download/1.1.1/ProfileBar.apk "Apk")

# More examples

![gogh_full](https://user-images.githubusercontent.com/31614124/54158363-0a762500-4453-11e9-9563-4f6fb8fc6e1e.jpg) 
![gogh_small](https://user-images.githubusercontent.com/31614124/54158364-0b0ebb80-4453-11e9-8cb0-acfe3b2a4db4.jpg)



![dali_full](https://user-images.githubusercontent.com/31614124/54158561-693b9e80-4453-11e9-92fb-a2e853175d82.jpg)
![dali_small](https://user-images.githubusercontent.com/31614124/54158562-693b9e80-4453-11e9-8250-6186e97318e5.jpg)

![pic_full](https://user-images.githubusercontent.com/31614124/54159479-bde01900-4455-11e9-86c6-71d951507cec.jpg)
![pic_small](https://user-images.githubusercontent.com/31614124/54159480-bde01900-4455-11e9-80e9-b2c600eae9e1.jpg)

## Supported Android versions
* API 22 and higher

*Earlier versions are to be added soon*

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

* Without databinding:
```
implementation "com.github.DichotoMe.ProfileBar:profilebar:1.1"
```

* With databinding:
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
**Note:** if you are using databinding, set `<layout>` as your root tag and specify the data to be bound:
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

### Hooking up data

#### Adding fragments to the pager

TabPager is designed to work with the [TabFragment](https://github.com/DichotoMe/ProfileBar/blob/master/profilebar/src/main/java/com/dichotome/profilebar/ui/tabPager/TabFragment.kt "TabFragment") class. Its difference from the simple Fragment is the **mutable `title` field**. It stands for the name of the tab, that contains the fragment.

To add fragments to a TabPager, first of all, you need to extend TabFragment and implement a *static newInstance() method*, similar to this: 
```
class FavouritesTabFragment() : TabFragment() {
    companion object {
        fun newInstance(tabTitle: String) = FavouritesTabFragment().apply {
            title = tabTitle
        }
    }
}
```
**See an example [here](https://github.com/DichotoMe/ProfileBar/blob/master/profilebar/src/main/java/com/dichotome/profilebar/stubs/fragments/FavouritesTabFragment.kt "FavouritesTabFragment")**

Next, create an arrayList named `pagerFragment` and add all the fragments to it, in the order you want to see them in the TabLayout
```
val pagerFragments = arrayListOf(
    SubscriptionsTabFragment.newInstance("Subsriptions"),
    FavouritesTabFragment.newInstance("Favourites")
)
```

**Note:** titles can be changed dynamically via TabPager.adapter using [the following interface](https://github.com/DichotoMe/ProfileBar/blob/master/profilebar/src/main/java/com/dichotome/profilebar/ui/tabPager/TabPagerAdapter.kt "TabAdapter")


#### Supplying values

* Without databinding:
    
[See the full property interface](https://github.com/DichotoMe/ProfileBar/blob/master/PROPERTY_INTERFACE.md "Profilebar property interface")

*In code:*
```
profileBar.apply {
    photoDrawable = photo
    subtitleText = "Joined on 19 April 2017"
    titleText = "Pavlo Bondan"
    wallpaperDrawable = wallpaper
    tabsEnabled = true
}
profilePager.fragments = pagerFragments

profileBar.setupWithViewPager(profilePager)
```
**See a full example [here](https://github.com/DichotoMe/ProfileBar/blob/master/app/src/main/java/com/dichotome/profilebarapp/ui/main/ProfileActivity.kt "Activity no binding example")**

* With databinding:

[See the full binding interface](https://github.com/DichotoMe/ProfileBar/blob/master/BINDING_INTERFACE.md "Profilebar binding interface")

*1. In layout.xml*
```
<data>
    <variable name="logic"
    ... />
</data>
<com.dichotome.profilebar.ui.profileBar.ProfileBar
    android:id="@+id/profileBar"
    
    app:photo="@{logic.photo}"
    app:wallpaper="@{logic.wallpaper}" 
    app:subtitle="@{logic.subtitle}"
    app:title="@{logic.title}" 
    ... />
    
<com.dichotome.profilebar.ui.tabPager.TabPager 
    android:id="@+id/profilePager"
    app:fragments="@{logic.pagerFragments}" 
    ... />
```
**See a full example [here](https://github.com/DichotoMe/ProfileBar/blob/master/app/src/main/res/layout/fragment_profile_with_adapters.xml "XML binding example")**

*2. In code:*
```
profileBar.setupWithViewPager(profilePager)
```
**See a full example [here](https://github.com/DichotoMe/ProfileBar/blob/master/app/src/main/java/com/dichotome/profilebarapp/ui/mainBinding/ProfileBindingActivity.kt "Activity binding example")**
