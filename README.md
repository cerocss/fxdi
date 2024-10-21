
# fxdi

A dependency injection library for sharing singletons accross Controllers specified in FXML files.


## Installation

```gradle
repositories {
    ...

    maven {
        url = uri("https://maven.pkg.github.com/cerocss/fxdi")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    implementation 'com.cerocss:fxdi:0.1.0'
}
```


## Usage

```java
FXDILoader fxdiLoader = new FXDILoader();
VBox vbox = fxmlLoader.load(getClass().getResource("view.fxml"));
stage.setScene(new Scene(vbox));
```


## License

[Apache 2.0](https://choosealicense.com/licenses/apache-2.0/)

