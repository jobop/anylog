@echo off
cd /d %~dp0
"%JAVA_HOME%"\bin\java -Djava.ext.dirs=lib\;providers\ -Djava.library.path="%JAVA_HOME%"\jre\bin  -DappHome="conf"  -jar lib\${project.artifactId}-${project.version}.jar 52808
pause