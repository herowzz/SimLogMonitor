# SimLogMonitor
SimLogMonitor is a simple log realtime monitor base logback and websocket.
Your project can config the logback.xml to collect the log info by socket to the SimLogMonitor Server. Then you can see the Real-time log in the SimLogMonitor Server.

## Getting Started
(1)  Check out the source code and start the com.github.herowzz.simlogmonitor.LogServer Main class
   SimLogMonitor open the collectPort(9988) to collect the logback log info and open the watcherPort(8088) to show the  Real-time log

(2) Set up the logback.xml config like this:
```java
<appender name="SOCKET" class="ch.qos.logback.classic.net.SocketAppender">
    	<RemoteHost>{SimLogMonitor-IP}</RemoteHost>
		<Port>{SimLogMonitor-Port}</Port>
		<ReconnectionDelay>10000</ReconnectionDelay>
</appender>
```
and run your application to record the log info

(3) Open your Browser by url: http://localhost:8088/ then you can see the realtime log info

(4) In the com.github.herowzz.simlogmonitor.LogServer you can change the collectPort and watcherPort
