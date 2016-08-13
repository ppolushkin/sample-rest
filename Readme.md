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

+ 50 users
+ 20 seconds for ramp-up
+ 4 loops
+ 8! external REST calls

|Label|# Samples|Average|Median|90% Line|95% Line|99% Line|Min|Max|Error %|Throughput|KB/sec|
|---|---|---|---|---|---|---|---|---|---|---|---|
|async-persons|440|734|294|3200|3392|3704|112|4677|4.09%|.9|1.6|
|blocking-persons|440|1471|1077|3790|4101|4488|570|7105|5.68%|1.0|1.6|
