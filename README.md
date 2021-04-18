# Minecraft Cursed Legacy API

The (unofficial) [Fabric](https://fabricmc.net/) home for 1.2.5 and Beta 1.7.3 - if you want the newer versions see [here](https://github.com/FabricMC/fabric).

## Setup
Run the following command (if you are not using eclipse, replace “eclipse” with your relevant ide)

```
./gradlew eclipse
```

NOTE: if you want sources (recommended), instead run this. You only need to run this the first time you need to generate sources.

```
./gradlew :rebuildLVT :genSources eclipse
```

To be able to run the API as a whole in an API dev environment, ensure the root project has been set up in your ide. This should be automatic, but it pays to make sure.

If you wish to build a copy of API, you can run:

```
./gradlew build -x compileTestJava -x checkstyleTest
```

## Contributing

Make sure to follow the code style guidelines, which can be seen in use in existing files.

A General Summary of the code style:
- Use tabs for indentation
- Use same-line braces
- Put a new line between “regular code lines” and if/for/while etc. statements.

## License
This API is available under the MIT license. Feel free to learn from it and incorporate it in your own projects.

## Running Tests
If your ide runs using gradle change settings to make your ide compile it itself so you can run the test modules.
If your ide doesn't have this feature get a better ide not a glorified text editor please kthx
