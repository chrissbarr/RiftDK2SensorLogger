# RiftDK2SensorLogger

Basic tool for logging sensor data from Oculus Rift DK2 HMD. 

Written to support my final year project (3D telepresence with the Oculus Rift), in order to get a feel for what rates the user is likely to move their head while looking around (during different tasks / in different environments).

## Usage
While running, the program will read the position, orientation, angular velocity and angular acceleration from the Oculus Rift HMD using functions provided by the Oculus SDK. These values will be printed both to the terminal window and to a time-stamped text file in the /data/ directory in the following format:

posX posY posZ quatX quatY quatZ quatW accelX accelY accelZ velocX velocY velocZ

Note that the data is tab-delimited. 

posX/Y/Z are the X/Y/Z coordinates of the HMD relative to the origin point.
quatX/Y/Z/W describe the orientation of the HMD via a quaternion.
accelX/Y/Z describes the angular acceleration for each axis in radians / s^2.
velocX/Y/Z describes the angular velocity for each axis in radians / s.

Both acceleration and velocity values can be converted to values in degrees and logged in that format - setting the boolean outputInDegrees to true will enable this behaviour.





