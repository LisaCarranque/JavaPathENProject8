FROM java:8
VOLUME /tmp
EXPOSE 8080
ADD /build/libs/tourGuide-1.0.0.jar tourGuide-1.0.0.jar
ENTRYPOINT ["java","-jar","tourGuide-1.0.0.jar"]