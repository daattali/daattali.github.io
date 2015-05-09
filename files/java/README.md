```
<applet code="MAINCLASS.class" width="400" height="450" archive="FILE.jar">
</applet>
```

https://support.comodo.com/index.php?/Default/Knowledgebase/Article/View/531/7/signing-jar-files  
https://weblogs.java.net/blog/cayhorstmann/archive/2014/01/16/still-using-applets-sign-them-or-else    
https://java.com/en/download/faq/chrome.xml#npapichrome  (doesn't work on chrome since april 2015 by default)  


```
# make jar file
jar cvf test\Connect4.jar test\Connect4.class   
# find out my alias (deancert.p12 was exported from firefox)
keytool -list -v -storetype pkcs12 -keystore deancert.p12
# sign jar
jarsigner -keystore deancert.p12 -storetype pkcs12 -tsa http://timestamp.comodoca.com/rfc3161 test\Connect4.jar "dean attali's comodo ca limited id"
# get warning message: The signer's certificate chain is not validated. (browser gives message that the jar is self-signed so won't display it)


# attempt 2: double click on exported certification from firefox to install it on windows, then go to IE and export from there
# find out my alias
keytool -list -v -storetype pkcs12 -keystore deancert2.pfx
# sign the jar... same error


# attempt 3: uninstall java, install version 6u30 http://stackoverflow.com/questions/8387983/jarsigner-this-jar-contains-entries-whose-certificate-chain-is-not-validated
# now when I sign the jar and verify, I don't see a warning! but browsers still say the application is not safe, because of "the application contains both signed and unsigned code" - what does that mean? I signed the jar, all the jar has inside is the class file, so what's the unsigned code i have? If I say "don't block" then it tells me my security settings have blocked the application

# attempt 4: add permissiosn to jar file
add `<param name="permissions" value="all-permissions" />` to the applet  
add `Permissions: all-permissions\n` to a new text file
# create jar with added permissions in manifest
jar cfmv test\Connect4.jar test\man-add.txt test\Connect4.class
# sign jar
jarsigner -keystore deancert.p12 -storetype pkcs12 -tsa http://timestamp.comodoca.com/rfc3161 test\Connect4.jar "dean attali's comodo ca limited id"
# verify
jarsigner -verify test\Connect4.jar
# looks good. but now in browser i get error "user has denied privileges to the code"

```


```
try adding the intermediate certificates
http://stackoverflow.com/questions/20793254/signing-a-jar-the-signers-certificate-chain-is-not-validated
```


comodo technical help said to try importing into IE then exporting and make sure it includes all intermediate certifications.
