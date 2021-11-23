FROM java:8
VOLUME /tmp
EXPOSE 9003
ADD /build/libs/gpsUtil-1.0.0.jar gpsUtil-1.0.0.jar
ENTRYPOINT ["java","-jar","gpsUtil-1.0.0.jar"]