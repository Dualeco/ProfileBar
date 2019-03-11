# Interface
Here all the ProfileBar and Toolbar public fields and methods are described

## `ProfileBar`
* `photo: Drawable?` - photo image drawable
* `wallpaper: Drawable?` - wallpaper image drawable
* `title: String?` - larger text in the view
* `subtitle: String?` - smaller text in the view

#### Preset fields
`value: Type = DEFAULT` here means that `value` of type `Type` has a default value equal to `DEFAULT`

* `titleSize: Int = 20sp` - title text size in sp
* `subtitleSize: Int = 12sp` - subtitle text size in sp
* `fontColor: Int = 0xFFFFFF` - title and subtitle text color
* `tabsEnabled: Boolean = true` - displays whether `profileBar` needs to hook up `tabPager`
* `tabsIndicatorColor: Int = 0xFFFFFF` - color of the tab selection indicator below the title
* `tabsSelectedColor: Int = 0xFFFFFF` - color of the selected tab's title
* `tabsUnselectedColor: Int = 0x99FFFFFF` color of the unselected tab's title
    
* `photoFrameDrawable: Drawable = DEFAULT_FRAME_DRAWABLE` - drawable the layer below the profile photo
* `photoFrameColor: Int = 0xCCFFFFFF` - photo frame color
    
* `dimDrawable: Drawable = DEFAULT_DIM_DRAWABLE` - semi-transparent rectangular view that occupies half of `profileBar`
* `bottomGlowDrawable: Drawable = DEFAULT_BOTTOM_GLOW` - glowing strip on the border between `tabPager` and `profileBar`

#### Methods
* setupWithViewPager(ViewPager) - link up `profileBar` and the ViewPager in the arguments

## `TabPager`
* `fragments: List<TabFragments>` - the list of TabFragments to be displayed

#### TabAdapter fields and properies
_access TabAdapter this way: `tabPager.adapter`_

* `setPageTitle(Int, String)` - sets the String value to the title of the fragment with index Int
* `getPageTitle(Int)` - gets the String value to the title of the fragment with index Int
* `addTab(TabFragment)` - adds a TabFragment
* `removeTab(TabFragment)` - removes a TabFragment
