Présentation :  
Ce projet constitue l'application TourGuide développée pour l'entreprise TripMaster

Lancement :  
Ouvrir le projet et lier les modules  GpsUtil, RewardCentral, 
TourGuide, TripPricer en tant que gradle projet.  
Utiliser l'instruction bootJar de gradle pour construire le jar de chaque projet,
utilisé dans la constitution des images docker.  
Lancer le script avec l'instruction :  
bash launch.sh  
ou suivre les instructions contenues dans celui-ci pour la
constitution des images docker et le lancement du projet.  

Test de performance :   
Les tests de performance évaluent le fonctionnement de RewardCentral et 
GpsUtil pour un nombre croissant d'utilisateurs.  
Pour faire varier celui-ci, on utilise la classe InternalTestHelper du 
module TourGuide.
Les tests correspondants sont les tests d'intégration du package IT du module TourGuide.  
Concernant les performances, les tests sont localisés au path :
tourGuide/IT/TestPerformanceIT.java
 
Test des endpoints :  
Sur Postman, on peut tester les différents endpoints avec les urls GET suivantes :  
localhost:8080/
localhost:8080/getLocation?userName=internalUser0
localhost:8080/getNearbyAttractions/internalUser0
localhost:8080/getRewards?userName=internalUser0
localhost:8080/getAllCurrentLocations
localhost:8080/getTripDeals?userName=internalUser0  
localhost:8080/getLocations?userNames=internalUser0&userNames=internalUser1&userNames=internalUser2  
et le endpoint :
localhost:8080/setUserPreferences/internalUser0
avec le body :  
{  
"tripDuration" : 1,  
"ticketQuantity" : 1,  
"numberOfAdults" : 1,  
"numberOfChildren" : 1  
} 