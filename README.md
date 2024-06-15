# Spring Cache + Redis

#### Redis CLI Docker
docker run -it --network spring-cache_default --link spring-cache-redis-1:redis --rm redis redis-cli -h redis -p 6379
