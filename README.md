### Welcome to wikkiot IOT framework

The wikkiot API is a service that allows devices equipped with a microcontroller and wifi to interact between each other. A device communicates with another device via a wikkiot server using the wikkiot protocol. The protocol has been defined generic enough to allow any type of interaction.

The framework addresses the following issue:
When building inter object communication systems , there are 2 types of implementations.
* Building the objects i.e. devices.
* Implement the interactions between the objects.

The framework has been designed in such a way so as to allow the object builder have complete abstraction on the interaction builder and vice versa.
To achieve this, we use a [device definition](https://github.com/eucleed/wikkiot/wiki/Device-Definition).


#####The device definition emulator.
As wikkiot programmer will probably not already have the physical device before hand. We provide an online device definition emulator.  This is a convenient tool to build interactions and test them until the physical device is ready.

The emulator does the following:
* Logs functions being invoked
* Provides buttons to emit events specified in the device definition.



### Get started

#### Install the server

At least JDK 1.7 is required.

Download wikkiot server [wikkiot](http://72.13.93.222:8080/wikkiot-0.0.1.zip)

run 
```
bin/startup.sh on linux 
```
or
```
bin/startup.bat on windows
```

Check if server is running http://localhost:8080/wikkiot

#### Create a driver

```
<dependency>
  <groupId>org.castafiore</groupId>
  <artifactId>iot.driver.java</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>

```

Get your hands wet with the [Tutorial](https://github.com/eucleed/wikkiot/wiki/Tutorial)



