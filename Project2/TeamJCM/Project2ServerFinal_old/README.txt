TO CONFIGURE ON A NEW PLATFORM:

1. Must create SQL database called projtwo.db in some directory.
2. Use the following command to create a new table:
create table DataTable1 (time double, longitude double, latitude double, bearing double, speed double, accelX double, accelY double, accelZ double, orientationA double, orientationP double, orientationR double, rotVecX double, rotVecY double, rotVecZ double, rotVecC double, linAccX double, linAccY double, linAccZ double, gravityX double, gravityY double, gravityZ double, gyroX double, gyroY double, gyroZ double);
3. Must change the hardcoded paths in DBHandler.java and DataThreader.java
4. The various algorithms for path generation are functions in DataHandler, but they are called from DBTest on the datahandler.xxxx line. You can choose different ones to see the various paths.
