### Welcome to wikkiot IOT framework

The wikkiot API is a service that allows devices equipped with a microcontroller and wifi to interact between each other. A device communicates with another device via a wikkiot server using the wikkiot protocol. The protocol has been defined generic enough to allow any type of interaction.

The framework addresses the following issue:
When building inter object communication systems , there are 2 types of implementations.
1.	Building the objects i.e. devices.
2.	Implement the interactions between the objects.

The framework has been designed in such a way so as to allow the object builder have complete abstraction on the interaction builder and vice versa.
To achieve this, we use a device definition.



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



###Tutorial 1 

In this example we will create a simple remote controlled Lamp.

We need 2 devices: A lamp and a remote control with 2 buttons. On and Off

The remote control publishes 2 types of event : OnSwitchOn and OnSwitchOff

The lamp captures 2 events : SwitchOn and SwitchOff

Now that we have made the design, let us build the system.


####1.   The Lamp

Since we do not have a device embedded with linux yet, we will create a simple java swing interface to emulate the lamp.

The full source code: [Lamp.java](https://github.com/eucleed/wikkiot/blob/master/iot.driver.java/src/main/java/org/castafiore/iot/sample/Lamp.java)

The lamp need to be able to receive 2 events. i.e. SwitchOn and SwitchOff

#####Create an instance of device

```
final Device lamp = new Device("Lamp", "Lamp","Lamp" ,"img/lamp.png");
```

#####Register the 2 functions

```
 lamp.registerFunction("SwitchOn", "Function exposed to server");
 lamp.registerFunction("SwitchOff", "Function exposed to server");
````
		
#####Set the type of communication layer. 

In this case it is a java implementation of websocket.

Depending the environment the driver is running, other implementation of the websocket layer can be injected

```
 lamp.setWebsocketLayer(new JavaWebsocketLayer(lamp));
```
	
	
#####Add function handlers to execute the captured events

```
		lamp.addFunctionHandler(new FunctionHandler() {
			
			@Override
			public void execute(String name, Map<String, String> input) {
				...
			}
		});
```
		

#####Add on ready event to continue.

```
lamp.onReady(new OnReady() {			
	public void ready() {
		...
	}
});
```

		
#####Connect the device to the wikkiot server

```
  lamp.connect("ws://localhost:8080/wikkiot/websockets/iot");

```
		

####The remote control.

The remote control need to publish 2 events i.e OnSwitchOn and OnSwitchOff

The full source code :[RemoteControl.java](https://github.com/eucleed/wikkiot/blob/master/iot.driver.java/src/main/java/org/castafiore/iot/sample/RemoteControl.java)


##### Register events for a device

```		
  remotecontrol.registerEvent("OnSwitchOn", "Switch on");
  remotecontrol.registerEvent("OnSwitchOff", "Switch off");
```

##### Propagate an event to wikkiot server

```
remotecontrol.propagateEvent("OnSwitchOn");

```


#### The server

Up to now, we have created our devices and connected them to the server.

To check if the devices are working properly:

go to [http://localhost:8080/wikkiot](http://localhost:8080/wikkiot)

Click refresh.

You should see the devices and their status.



We need to create an iot application which will do the following

Capture the OnSwitchOn event from the remote control and execute the SwitchOn function in the lamp.

Capture the OnSwitchOff event.....

Good news an IOTApplet is in fact a spring managed bean.

##### Create a new IOTApplet

```
public class RemoteControlApplet extends GenericIOTApplet {
....
}
```


#####Add devices to be managed

```
addRequiredDeviceName("Lamp");
addRequiredDeviceName("Remote");

```


##### Add listeners to remote device events

```
device.addEvent(new OnSwitchOn(), "OnSwitchOn");
device.addEvent(new OnSwitchOff(), "OnSwitchOff");

```


##### Create an event listener

```
class OnSwitchOn implements EventListener{

		@Override
		public void execute(Device source, String type, Map<String, String> parameters) {
			....
		}
		
	}
```


#### Configure the server to run the IOTApplet

Simply create a spring application context file with the following convention

The file name should be xxx-config.xml, where xxx could by anything

The file should be in folder WEB-INF/configs in the iot directory.

```
<bean class="org.castafiore.iot.RemoteControlApplet">
   <property name="registry" ref="deviceRegistry"></property>
</bean>
```


Restart the server.

The remote controlled switch is ready for use.

Start the 2 devices.

click on the switch on and switch off button and see if it reacts on the lamp device.
