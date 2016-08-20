nohup java -Djava.ext.dirs="lib/:providers/" -DappHome="conf" -jar lib/${project.artifactId}-${project.version}.jar 52808 $@ >>logs/anylog.log 
