Présentation :  
Ce projet constitue l'application TourGuide développée pour l'entreprise TripMaster

Lancement :  
Ouvrir le projet et lier les modules  GpsUtil, RewardCentral, 
TourGuide, TripPricer en tant que gradle projet.  
Utiliser l'instruction bootJar de gradle pour construire le jar de chaque projet,
utilisé dans la constitution des images docker.  
Lancer le script launch.sh ou suivre les instructions contenues dans celui-ci pour la
constitution des images docker et le lancement du projet.  

Test de performance :   
Les tests de performance évaluent le fonctionnement de RewardCentral et 
GpsUtil pour un nombre croissant d'utilisateurs.  
Pour faire varier celui-ci, on utilise la classe InternalTestHelper du 
module TourGuide.
Les tests correspondants sont les tests d'intégration du package IT du module TourGuide.  
Concernant les performances, les tests sont localisés au path :
tourGuide/IT/TestPerformanceIT.java
 