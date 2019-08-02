# ParcelableHelper
A simple AOP tool that helps Android developers quickly implement Parcelable interfaces
![image](https://github.com/skateboard1991/ParcelableHelper/blob/master/icon.jpeg)

# Usage
## 1.add dependency in root project build.gradle file
```
buildscript {
    repositories {
       ....
        maven{
            url "https://dl.bintray.com/wuhaoxuan1225/maven/"
        }
        
    }
    dependencies {
        ....
        classpath 'com.skateboard.parcelablehelper:parcelablehelper:1.0.0'
        ....
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
```
## 2 apply plugin in module gradle file
```
apply plugin: 'parcelablehelper'
```

## 3 add annoation in class
Attributes annotated by @Ignore will not be serialized
```
@Parcelable
public class Demo {

    private String name;
    
    @Ignore
    private String age;

}

```

# Notice
Now this tools only support builtin type,String and Parcelable,include array and list
