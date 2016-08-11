# Sample REST application for testing non-blocking (async) approach

## Performance tests results:

### Plain database call
Testing of usual scenario REST call goes to database and return result

+ 100 users
+ 10 seconds for ramp-up
+ 10 loops
+ 2 REST calls (get all users| get user by id)

#### Async call to database see [AsyncPersonController](https://github.com/ppolushkin/sample-rest/blob/master/src/main/java/hello/AsyncPersonController.java)

|Label|# Samples|Average|Median|90% Line|95% Line|99% Line|Min|Max|Error %|Throughput|KB/sec|
|Thread Group:HTTP Request|2000|571|491|1139|1306|2029|13|2902|0.00%|100.8|23.9|
|TOTAL|2000|571|491|1139|1306|2029|13|2902|0.00%|100.8|23.9|

#### Blocking call to database see [BlockingPersonController](https://github.com/ppolushkin/sample-rest/blob/master/src/main/java/hello/BlockingPersonController.java)

|Label|# Samples|Average|Median|90% Line|95% Line|99% Line|Min|Max|Error %|Throughput|KB/sec|
|Thread Group:HTTP Request|2000|6|4|12|19|40|2|134|0.00%|193.8|45.9|
|TOTAL|2000|6|4|12|19|40|2|134|0.00%|193.8|45.9|
