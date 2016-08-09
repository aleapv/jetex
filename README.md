# winrun4jTest

source project:

- OpenSSL vs. Keytool -> Keytool т.к. программа на Java

- Пример получения пароля:
 -! http://www.eclipse.org/jetty/documentation/current/configuring-security-secure-passwords.html
 -! java -cp C://Users/alex/.ivy2/cache/org.eclipse.jetty/jetty-util/jars/jetty-util-9.3.7.v20160115.jar org.eclipse.jetty.util.security.Password me 1234

- Полный список настроек jetty:
 -! http://download.eclipse.org/jetty/stable-9/xref/org/eclipse/jetty/embedded/LikeJettyXml.html

- в руководстве криптопро ссылка на настройку ssl jetty 7, 8
 -! в jetty 9 настройки ssl задаются в start.ini, а не в jetty.xml как jetty 7, 8

- C:\Java\jdk1.8.0_73_jetty_test\jre\bin\java -Dcom.sun.security.enableCRLDP=true -jar jetex-assembly-0.1-SNAPSHOT.jar
 -!csptest -tlsc -server localhost -port 8443 -file index.html -nosave -nocheck -verbose


pitfalls:

- [err] Service control dispatcher error: 1063
 -! you are calling directly,
    use --WinRun4J:RegisterService to register service and service panel or net start to start service.

- java.lang.NoSuchMethodError: serviceRequest
 -! приложение по шаблону http://winrun4j.sourceforge.net/ с необходимыми зависимостями

- Could not access service manager: 5
 -! run the command window as an adminstrator(right click)

- ERROR: Could not load library: 
 -! установить jre (в которой существует необходимый путь jre/bin/client) в директорию проекта

- [err] No main class specified
 -! при запуске в виде консольного приложения указать в ini-файле параметр main.class

- [SC] DeleteService: ошибка: 1072: Указанная служба была отмечена для удаления.
 -! вероятно возникает, если переустановить неудаленную службу - перезагрузить компьютер

- Ошибка 1053 : Служба не ответила на запрос своевременно
 -! попробовать удалить службу из реестра(HKLM\SYSTEM\CurrentControlSet\Services) в безопасном режиме(F8) перед переустановкой
    проверить доступность jre
    отжата ли галочка не сервис
	попробовать удалить ресурсы
