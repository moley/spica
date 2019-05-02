# SEE https://docs.oracle.com/javase/8/docs/technotes/tools/unix/javapackager.html

JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-12.jdk/Contents/Home
APP_DIR_NAME=Spica-Devclient.app

#-deploy -Bruntime=/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home \
$JAVA_HOME/bin/jpackager create-image --input build/libs --output build/release --name Spica-DevClient --class org.spica.devclient.DevApplication
