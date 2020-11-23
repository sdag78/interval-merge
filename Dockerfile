FROM openjdk:8
COPY . /app
WORKDIR /app
RUN mkdir classes
RUN javac -d classes src/com/sdag/*.java
RUN jar cfe Interval.jar com.sdag.DoIntervalMerge -C classes .
CMD ["java", "-jar", "Interval.jar"]