Background Service Line Indicator
============================
***

Sync multiple SQLITE db entities from your API with ease
----------------------------
***VERSIONS***
``VERSIONS =>`` `0.0.1`

**Gradle**
```java
maven { url "https://jitpack.io" }

compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
}
```


***INSTALLATION***
`implementation 'com.github.FelixKAcheampong:FlxBackgroundProcessIndicatorLine:0.0.1'`

![](https://drive.google.com/uc?export=view&id=1CmZecuma4KPXlxiC84SCaqy5_tz2NVBG)

***USAGE***
============================

```xml
<com.felinkotech.flxbgprocessindicatorline.FlxBackgroundProcessIndicatorLine
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:indicator_color="#ff0000"
        app:indicator_speed="1"
        app:indicator_width="30dp"
        app:indicator_height="3dp"
        android:id="@+id/indicator" />
```

`indicator_color` => Indicator color
`indicator_speed`  => Indicator speed, min should be 1
`indicator_width`  => Indicator width
`indicator_height`  => Indicator height

```java
private FlxBackgroundProcessIndicatorLine indicatorLine ;

//Initialize 
indicatorLine = findViewById(R.id.indicator) ;

// Start indicator runner
indicatorLine.start();

// Stop or destroy a running indicator
indicatorLine.destroy();
```



# Important Note
Make sure to destroy indicator when activity or fragment is destroyed or paused to prevent memory leaks

```java

@Override
    public void onDestroy() {
        super.onDestroy();
        indicatorLine.destroy();
    }
	
	@Override
    public void onPause() {
        super.onPause();
		indicatorLine.destroy();
    }
```

## Proguard
If you are using proguard, make sure to add this to your keep rules

`
-keep class com.felinkotech.flxbgprocessindicatorline.**{*;}`

##End





