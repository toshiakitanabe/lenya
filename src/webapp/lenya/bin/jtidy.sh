#!/bin/sh

/usr/local/jdk1.3.1/bin/java -cp build/wyona-cms/classes:src/cocoon/WEB-INF/lib/jtidy-04aug2000r7-dev.jar:src/cocoon/WEB-INF/lib/xml-apis.jar org.wyona.util.TidyCommandLine file:/home/michiii/oscom-layout.html oscom-layout.xhtml error.log
