работает через cmd и git bash:
curl -X GET --noproxy "*" http://localhost:8080/topjava/rest/meal/100005

curl -X GET --noproxy "*" "http://localhost:8080/topjava/rest/meal/filter?startDate=2015-05-30&startTime=00:00&endDate=2015-05-30&endTime=23:10"

curl -X GET --noproxy "*" "http://localhost:8080/topjava/rest/meal/filter?startDate=2015-05-30&endDate=2015-05-30"

curl -X GET --noproxy "*" "http://localhost:8080/topjava/rest/meal"

curl -X DELETE --noproxy "*" "http://localhost:8080/topjava/rest/meal/100004"


работает через git bash:
curl -X PUT -H "Content-Type: application/json" -d '{"id":"100005","dateTime":"2015-05-31T10:00:00","description":"Updated","calories":"300"}' http://localhost:8080/topjava/rest/meal/100005 --noproxy "*"

curl --noproxy "*" -X POST -H "Content-Type: application/json" -d '{"dateTime":"2019-05-31T10:00:00","description":"Created","calories":"300"}' http://localhost:8080/topjava/rest/meal

