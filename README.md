# Stock Manger

Simple JavaFX application to manage products in stock. This program has no intentions of real-world usage and was made with the solo purpose of being an learning experience. 
Use it at your own risk.

The data is stored as a JSON file, which will be created at `user.home/.StockManager/data/` if it doesn't already exist.

To see the program in action without having to manually create any products, go to `Products > Use Sample Data`. Any changes you make to the sample data will be reverted once you restart the program or go back to your data. 

This project uses Gradle 6.6.1, JLink 2.22.0, JavaFX 11.0.1 and the JavaSDK 1.11

## Run the application
```bash
# Windows
gradlew.bat run

# Linux/macOS
./gradlew run
``` 

## Create a distributable zip file

```bash
# Windows
gradlew.bat assemble

# Linux/macOS
./gradlew assemble

# The result is in "build/distributions"
# The application can be run with a batch or a bash file
# A JRE is needed to run the application
``` 

## Create a native application with JLink

The native application created with Jlink will work with the OS that you are currently using:
```bash
# Windows
gradlew.bat jlink

# Linux/macOS
./gradlew jlink

# The result is in "build/image" (the content of the folder is needed to run the application)
# You run the application with "build/image/bin/exe_name.bat" (Windows) or "build/image/bin/exe_name" (Linux/macOS)
# You don't need a JRE to be able to run it
```  

## License

[CC0-1.0](./LICENSE)
