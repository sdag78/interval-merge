mkdir classes
javac -d classes src/com/sdag/*.java
jar cfe Interval.jar com.sdag.DoIntervalMerge -C classes .