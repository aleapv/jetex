Использование ssl в jetty embedded


OpenSSL vs. Keytool -> Keytool т.к. программа на Java

Пример получения пароля:
http://www.eclipse.org/jetty/documentation/current/configuring-security-secure-passwords.html
java -cp C://Users/alex/.ivy2/cache/org.eclipse.jetty/jetty-util/jars/jetty-util-9.3.7.v20160115.jar org.eclipse.jetty.util.security.Password me 1234

Полный список настроек jetty:
http://download.eclipse.org/jetty/stable-9/xref/org/eclipse/jetty/embedded/LikeJettyXml.html

в руководстве криптопро ссылка на настройку ssl jetty 7, 8
в jetty 9 настройки ssl задаются в start.ini, а не в jetty.xml как jetty 7, 8

C:\Java\jdk1.8.0_73_jetty_test\jre\bin\java -Dcom.sun.security.enableCRLDP=true -jar jetex-assembly-0.1-SNAPSHOT.jar
csptest -tlsc -server localhost -port 8443 -file index.html -nosave -nocheck -verbose
