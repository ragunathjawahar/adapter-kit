Adapter Kit 2.0
===========
![Logo](logo.png)

Adapter Kit is a set of useful adapters for Android. The kit currently includes,


 - [**Instant Adapter**](https://github.com/ragunathjawahar/instant-adapter) : Powerful annotations library which helps you build **Single-Row-Type** custom list view quickly.
 - [**Instant Cursor Adapter**](https://github.com/ragunathjawahar/instant-adapter) : InstantAdapter + Cursor Support.
 - [**Simple Section Adapter**](https://github.com/ragunathjawahar/simple-section-adapter) : Building sectioned list made simplest.
 - [**Circular List Adapter**](https://github.com/ragunathjawahar/circular-list-adapter) : Makes your existing list adapter infinitely scrollable.
 - [**Grid ListView Adapters**](https://github.com/birajpatel/GridListViewAdapters) : Play-store like unlimited cards list. **(ListView working as GridView)**
 - [**Easy ListView Adapters**](https://github.com/birajpatel/EasyListViewAdapters) : Another powerful library which helps you build **Multi-Row-Type** listview in most easiest & extensible way.
 
**Note: Checkout Individual repositories for complete set of features, demos & detailed debugging information.**

Upgrading from Version 1.0 to 2.0
------------------------------
If you were already using adapter-kit-**V1.0** then moving to **V2.0** is simplest, just modify your imports and you are done.

```java
import com.mobsandgeeks.adapters.InstantText;
to
import com.mobsandgeeks.adapters.instantadapters.InstantText;
```

```java
import com.mobsandgeeks.adapters.InstantAdapter;
to
import com.mobsandgeeks.adapters.instantadapters.InstantAdapter;
```
```java
import com.mobsandgeeks.adapters.InstantCursorAdapter;
to
import com.mobsandgeeks.adapters.instantadapters.InstantCursorAdapter;
```

```java
import com.mobsandgeeks.adapters.Sectionizer;
to
import com.mobsandgeeks.adapters.simplesectionadapter.Sectionizer;
```

```java
import com.mobsandgeeks.adapters.SimpleSectionAdapter;
to
import com.mobsandgeeks.adapters.simplesectionadapter.SimpleSectionAdapter;
```

```java
import com.mobsandgeeks.adapters.CircularListAdapter; 
to
import com.mobsandgeeks.adapters.circularlistadapter.CircularListAdapter;
```

Maven
---------------------
    <dependency>
        <groupId>com.mobsandgeeks</groupId>
        <artifactId>adapter-kit</artifactId>
        <version>0.5.3</version>
    </dependency>

Gradle
---------------------
    dependencies {
        compile 'com.mobsandgeeks:adapter-kit:0.5.3'
    }

License
-------

    Copyright 2013 Mobs & Geeks

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

<sub>Adapter Kit Logo © 2013, Mobs & Geeks.</sub>


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/mobsandgeeks/adapter-kit/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

