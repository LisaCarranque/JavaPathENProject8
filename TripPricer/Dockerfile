FROM java:8
VOLUME /tmp
EXPOSE 9001
ADD /build/libs/tripPricer-1.0.0.jar tripPricer-1.0.0.jar
ENTRYPOINT ["java","-jar","tripPricer-1.0.0.jar"]