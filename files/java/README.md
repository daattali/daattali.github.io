<applet code="MAINCLASS.class" width="400" height="450" archive="FILE.jar">
</applet>

jar cvf FILE.jar *
jarsigner -keystore "C:\Program Files\Java\jdk1.8.0_40\jre\lib\security\cacerts" -storeType JKS FILE.jar daattali -tsa http://tsa.safecreative.org


https://www.digicert.com/code-signing/java-code-signing-guide.htm
http://codesigning.ksoftware.net/
https://weblogs.java.net/blog/cayhorstmann/archive/2014/01/16/still-using-applets-sign-them-or-else