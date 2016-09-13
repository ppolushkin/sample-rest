# Sample REST application for testing non-blocking (async) approach

## Performance tests results:

### Plain database call
Testing of usual scenario REST call goes to database and return result

+ 100 users
+ 10 seconds for ramp-up
+ 10 loops
+ 2 REST calls (get all users| get user by id)

#### Async call to database

|Label|# Samples|Average|Median|90% Line|95% Line|99% Line|Min|Max|Error %|Throughput|KB/sec|
|---|---|---|---|---|---|---|---|---|---|---|---|
|TOTAL|2000|571|491|1139|1306|2029|13|2902|0.00%|100.8|23.9|

#### Blocking call to database

|Label|# Samples|Average|Median|90% Line|95% Line|99% Line|Min|Max|Error %|Throughput|KB/sec|
|---|---|---|---|---|---|---|---|---|---|---|---|
|TOTAL|2000|6|4|12|19|40|2|134|0.00%|193.8|45.9|


#### Blocking call to db and Sync vs Async external REST call

+ 100 users
+ 20 seconds for ramp-up
+ 6 loops
+ 8 external REST calls (each call = 500ms)
+ database connection pool = 150

|Label|# Samples|Average|Median|90% Line|95% Line|99% Line|Min|Max|Error %|Throughput|KB/sec|
|---|---|---|---|---|---|---|---|---|---|---|---|
|blocking-persons|600|4951|4744|6015|6494|7119|4079|7342|0.00%|6.5|12.0|
|async-persons FJP|600|625|618|687|699|727|539|836|0.00%|7.9|14.5|
|async-persons cached_thread_pool|600|855|667|1350|1643|2372|547|13134|0.00%|7.0|12.8|



curl -H "Content-Type: application/json" -X POST -d '{"firstName":"Pavel", "vkId" : "clumsyBear"}' http://localhost:8080/blocking-persons
curl -H "Content-Type: application/json" -X POST -d '{"firstName":"Anton", "vkId" : "jikan"}' http://localhost:8080/blocking-persons
curl -H "Content-Type: application/json" -X POST -d '{"firstName":"Elena", "vkId" : "koshkin_hvost"}' http://localhost:8080/blocking-persons
curl -H "Content-Type: application/json" -X POST -d '{"firstName":"Efim", "vkId" : "id60320309"}' http://localhost:8080/blocking-persons
curl -H "Content-Type: application/json" -X POST -d '{"firstName":"Viktor", "vkId" : "id14070"}' http://localhost:8080/blocking-persons
curl -H "Content-Type: application/json" -X POST -d '{"firstName":"Alex", "vkId" : "d.lyosha"}' http://localhost:8080/blocking-persons
curl -H "Content-Type: application/json" -X POST -d '{"firstName":"Joel", "vkId" : "id275089593"}' http://localhost:8080/blocking-persons
curl -H "Content-Type: application/json" -X POST -d '{"firstName":"Slava", "vkId" : "jasonrammoray"}' http://localhost:8080/blocking-persons
curl -H "Content-Type: application/json" -X POST -d '{"firstName":"Katya", "vkId" : "going_slightly_mad"}' http://localhost:8080/blocking-persons
curl -H "Content-Type: application/json" -X POST -d '{"firstName":"Viktor", "vkId" : "viktor_lapin"}' http://localhost:8080/blocking-persons
