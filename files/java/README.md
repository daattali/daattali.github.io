Make sure there's a `manifest.txt` file with this text (including blank line at the end):

```
Permissions: all-permissions
Codebase: *
Application-Name: CHANGEME
```

Make and sign JAR:

```
set curjar=CHANGEME
jar cvf %curjar%.jar *.class
jar ufm %curjar%.jar manifest.txt
jarsigner -tsa http://timestamp.comodoca.com/rfc3161 -keystore ..\deancert.p12 -storetype pkcs12 %curjar%.jar "dean attali's comodo ca limited id"
```

Make html file containing:

```
<html>
<head><title>awesome java applet</title></head>
<body>
<center>
	<applet id="CHANGEME"
			code = "CHANGEME.class"
			archive="CHANGEME.jar"
			width="400"
			height="450"
			style="border: 3px solid black;">
	</applet>
</center>
</body>
</html>
```