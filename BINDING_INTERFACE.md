### Binding interface
These are the attributes that can be used to bind data to a ProfileBar or a TabPager

**Also see the [binding adapters folder](https://github.com/DichotoMe/ProfileBar/tree/master/profilebarBinding/src/main/java/com/dichotome/profilebarbinding/bindingAdapters "Binding adapters folder")**

The following attributes should be used in such a way:

`app:attribute = @{value}`

## `ProfileBar`
* `photo: Drawable? or @DrawableRes Int?` - photo image drawable
* `wallpaper: Drawable? or @DrawableRes Int?` - wallpaper image drawable
* `title: String? or @StringRes Int?` - larger text in the view
* `subtitle: String? @StringRes Int?` - smaller text in the view

#### Preset fields
`value: Type = DEFAULT` here means that `value` of type `Type` has a default value equal to `DEFAULT`

* `titleSize: Int = 20sp` - title text size in sp
* `subtitleSize: Int = 12sp` - subtitle text size in sp
* `fontColor: Int = 0xFFFFFF or @ColorRes: Int?` - title and subtitle text color
* `tabsEnabled: Boolean = true` - displays whether `profileBar` needs to hook up `tabPager`
* `tabsIndicatorColor: Int = 0xFFFFFF or @ColorRes: Int?` - color of the tab selection indicator below the title
* `tabsSelectedColor: Int = 0xFFFFFF or @ColorRes: Int?` - color of the selected tab's title
* `tabsUnselectedColor: Int = 0x99FFFFFF or @ColorRes: Int?` color of the unselected tab's title
    
* `photoFrameDrawable: Drawable = DEFAULT_FRAME_DRAWABLE or @DrawableRes Int?` - drawable the layer below the profile photo
* `photoFrameColor: Int = 0xCCFFFFFF or @ColorRes: Int?` - photo frame color
    
* `dimDrawable: Drawable = DEFAULT_DIM_DRAWABLE  or @DrawableRes Int?` - semi-transparent rectangular view that occupies half of `profileBar`
* `bottomGlowDrawable: Drawable = DEFAULT_BOTTOM_GLOW  or @DrawableRes Int?` - glowing strip on the border between `tabPager` and `profileBar`


#### Listeners

* `onPhotoChanged()` - listener for the `Photo` button click in the option menu
* `onWallpaperChanged()` - listener for the `Wallpaper` button click in the option menu
* `onUsernameChanged()` - listener for the `Username` button click in the option menu
* `onLoggedOut()` - listener for the `Log out` button click in the option menu

## `TabPager`
* `fragments: List<TabFragments>` - the list of TabFragments to be displayed
