<div align="center">
    <p><img alt="logo" src="./art/filmpedia_logo.png" width="150" /></p>
    <h1>Filmpedia</h1>
    <p>
    </p>
    <p>
    Filmpedia is an unofficial TMDB movie information application for Android.
    </p>
    <p>
    This project is based on MVVM architecture and using famous android libraries like Retrofit, Glide, Coroutines and AAC.
    It can be a sample of android application using AAC and MVVM.
    I hope it will be helpful to android developers and learners.
    </p>
</div>

## Features

- Movie list
- Movie detail
- Movie series
- Person
- Search

## Tech Stacks

- **Language**: Kotlin
- **Network**: Coroutines + Retrofit
- **Architecture**: MVVM + Repository Pattern + Data Binding + View Binding
- **Version Control**: Git + GitHub
- **Logo Design**: Figma

## Open Source Libraries

- [Kotlinx.Coroutines](https://github.com/Kotlin/kotlinx.coroutines) (Apache-2.0 License)
- [Android Jetpack Libraries](https://github.com/androidx/androidx) (Apache-2.0 License)
  - [Core](https://github.com/androidx/androidx/tree/androidx-main/core)
  - [Activity](https://github.com/androidx/androidx/tree/androidx-main/activity)
  - [Appcompat](https://github.com/androidx/androidx/tree/androidx-main/appcompat)
  - [Constraint Layout](https://github.com/androidx/constraintlayout)
  - [Fragment](https://github.com/androidx/androidx/tree/androidx-main/fragment)
  - [Lifecycle](https://github.com/androidx/androidx/tree/androidx-main/lifecycle)
  - [Swipe Refresh Layout](https://github.com/androidx/androidx/tree/androidx-main/swiperefreshlayout)
  - [ViewPager2](https://github.com/androidx/androidx/tree/androidx-main/viewpager2)
- [Material Components](https://github.com/material-components/material-components-android) (Apache-2.0 License)
- [Retrofit](https://github.com/square/retrofit) (Apache-2.0 License)
- [Okhttp](https://github.com/square/okhttp) (Apache-2.0 License)
- [Glide](https://github.com/bumptech/glide) ([Custom License](https://github.com/bumptech/glide/blob/master/LICENSE))

## Architecture

Filmpedia is based on MVVM (Model-View-ViewModel) architecture + Repository pattern.

![](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)
<div align="right">
    <small>
        &lt;Image from <a href="https://developer.android.com/jetpack/guide?gclid=CjwKCAjw1JeJBhB9EiwAV612y_g-BW7h2BTGnW3IppKi9ZqdxaOpgKO02VhcRUCzH-7ECMUs7Q7F9RoCcbQQAvD_BwE&gclsrc=aw.ds">Android Developers&gt;</a>.
    </small>
</div>

## Package Structure

```
.
├─ data: data classes
├─ network: Retrofit instance and services
├─ repository: Repository classes
├─ ui
│  ├─ component: Custom views
│  └─ page: Each activities, fragments, viewmodels and adapters
│      ├─ home
│      ├─ moviedetail
│      └─ ...
└─ util: Utility functions, classes and objects
```

## Open API

Filmpedia is using [TMDB API](https://developers.themoviedb.org/3).

TMDB API provides thousands of movie's information and RESTful API.

## MAD Score

MAD (Modern Android Development) Score is a score card service provided by Google.

![madscore-summary](./madscore/summary.png)

![madscore-kotlin](./madscore/kotlin.png)

![madscore-jetpack](./madscore/jetpack.png)

<div align="right">
    <small>
        &lt;Score based on Filmpedia x.x.x version&gt;
    </small>
</div>

## License

Filmpedia app is licensed under the [Apache-2.0 License](./LICENSE).

Material Icons made by Google and licensed under the [Apache-2.0 License](http://www.apache.org/licenses/LICENSE-2.0.txt).

Logo made by Jaewoong Cheon.
