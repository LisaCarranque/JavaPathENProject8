cd GpsUtil
docker build -t gpsutil .
cd ..
cd RewardCentral
docker build -t rewardcentral .
cd ..
cd TripPricer
docker build -t trippricer .
cd ..
cd TourGuide
docker build -t tourguide .
docker-compose --env-file .env  up