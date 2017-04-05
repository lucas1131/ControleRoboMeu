AccelerationExplorer
====================

![Alt text](http://www.kircherelectronics.com/resources/images/accelerationExplorer/acceleration_explorer_home.png "Android Acceleration Explorer Screenshot")

# Introduction

Acceleration Explorer is an open source Android application with two different purposes. 

The first purpose is to help developers write better code by providing examples of how to implement the Android acceleration sensor and apply various smoothing and linear acceleration filters. Choosing to use the sensor values provided by Android directly will prove to be a poor choice in most applications and this is not immediately obvious to most developers. Furthermore, finding the acceleration measurements for your project requires a great deal of understanding and knowledge to do correctly. Blindly throwing a couple of filters at the acceleration sensor will likely result in a disapointed developer. Acceleration Explorer seeks to prevent the disapointed developer by providing theory, code examples and an application to test and experiment with.

The second purpose is a functioning application allowing teachers, students and hobbyists (who may not be interested in the code) to visualize the acceleration sensor's outputs and how different filters effect the outputs.

[Download Acceleration Explorer from Google Play](https://play.google.com/store/apps/details?id=com.kircherelectronics.accelerationexplorer&hl=en)

## Wiki 

Please see the [Wiki](https://github.com/KalebKE/AccelerationExplorer/wiki) for a detailed explanation of the filters.

## Overiew of Features

Acceleration Explorer has five main Activities. A logger view, a vector view, a tilt view, a noise view and a diagnostic view.  Each Activity provides a different visualization of some aspect of the acceleration sensor. 

Acceleration Explorer Features:

* Plots the output of all of the sensors axes in real-time
* Log the output of all of the sensors axes to a .CSV file
* Visualize the magnitude and direction of the acceleration
* Smoothing filters include low-pass, mean and median filters
* Linear acceleration filters include low-pass as well as sensor fusion complimentary (rotation matrix and quaternion) and Kalman (quaternion) filters
* Visualize the tilt of the device
* Measure the acceleration sensors frequency, offset and noise
* Compare the performance of multiple devices

### The Logger

![Alt text](http://www.kircherelectronics.com/resources/images/accelerationExplorer/acceleration_explorer_logger.png "Android Acceleration Explorer Screenshot")

The logger provides a real-time plot of the X, Y and Z axis of the acceleration sensor. You can also opt to log the sensor data to an external .CSV file. All of the smoothing filters and linear acceleration filters can be applied to the acceleration sensor data.

### The Vector

![Alt text](http://www.kircherelectronics.com/resources/images/accelerationExplorer/acceleration_explorer_vector.png "Android Acceleration Explorer Screenshot")

The vector plots the acceleration as a vector (a line with a direction and magnitude) in the x and y axis. The maximum magnitude (length) of the vector is limited to 1g, or 9.8 meters/sec^2. All of the smoothing filters and linear acceleration filters can be applied to the acceleration sensor data.

### The Gauges

![Alt text](http://www.kircherelectronics.com/resources/images/accelerationExplorer/acceleration_explorer_gauges.png "Android Acceleration Explorer Screenshot")

The gauges plot the acceleration of the x and y axis in terms of tilt and acceleration relative to 1g, or 9.8 meters/sec^2. One of the key limitations of acceleration sensors is the inability to differentiate tilt from linear acceleration.

### The Noise

![Alt text](http://www.kircherelectronics.com/resources/images/accelerationExplorer/acceleration_explorer_noise.png "Android Acceleration Explorer Screenshot")

The noise allows the performance of different smoothing filters relative to the raw data can be compared simultaneously. A low-pass filter, a mean filter and a median filter are available and can be configured in terms of a time constant. The performance metric is the root-mean-squared (RMS) of the outputs. It is essentially the average variance from the mean of the data.

### The Diagnostic

![Alt text](http://www.kircherelectronics.com/resources/images/accelerationExplorer/acceleration_explorer_diagnostic.png "Android Acceleration Explorer Screenshot")

Acceleration Explorer has a diagnostic mode intended to help the user discover the noise, offset and skew associated with the acceleration sensor. After a quick calibration process, Acceleration Explorer will calculate the magnitude, accuracy and noise of each axis of the sensor. Acceleration Explorer also determines the minimum and maximum amplitudes of each axis along with the update frequency of the acceleration sensor. This can be very useful for developers and other interested parties that need to compare the performance of acceleration devices equipped on different devices. It can also be useful in determining what digital filters and other calibrations might help the acceleration sensor to improve the sensors performance and accuracy.

For more information on sensor offset and skew, see [here](http://www.kircherelectronics.com/blog/index.php/11-android/sensors/7-android-accelerometer). For more information on how to compenstate for sensor offset and skew, see [here](http://www.kircherelectronics.com/blog/index.php/11-android/sensors/14-sensor-skew-and-offset-ellipsoidfit).

## Smoothing filters

Acceleration Explorer implements three of the most common smoothing filters, low-pass, mean and meadian filters. All the filters are user configurable based on the time constant in units of seconds. The larger the time constant, the smoother the signal. However, latency also increases with the time constant. Because the filter coefficient is in the time domain, differences in sensor output frequencies have little effect on the performance of the filter. These filters should perform about the same across all devices regardless of the sensor frequency.

### Low-Pass Filter

Acceleration Explorer use an IIR single-pole implementation of a low-pass filter. The coefficient, a (alpha), can be adjusted based on the sample period of the sensor to produce the desired time constant that the filter will act on. It takes a simple form of output[0] = alpha * output[0] + (1 - alpha) * input[0]. Alpha is defined as alpha = timeConstant / (timeConstant + dt) where the time constant is the length of signals the filter should act on and dt is the sample period (1/frequency) of the sensor. For more information on low-pass filters, see the [Acceleration Explorer Wiki](https://github.com/KalebKE/AccelerationExplorer/wiki).

### Mean Filter

Acceleration Explorer implements a mean filter designed to smooth the data points based on a time constant in units of seconds. The mean filter will average the samples that occur over a period defined by the time constant... the number of samples that are averaged is known as the filter window. The approach allows the filter window to be defined over a period of time, instead of a fixed number of samples.

### Median Filter

Acceleration Explorer usees a median filter designed to smooth the data points based on a timeconstant in units of seconds. The median filter will take the median of the samples that occur over a period defined by the time constant... the number of samples that are considered is known as the filter window. The approach allows the filter window to be defined over a period of time, instead of a fixed number of samples.

### Comparing Smoothing Filter Performance

The Noise Activity within Acceleration Explorer will compare the root-mean-sqaured (RMS) average of all three filters and the raw sensor output in real-time. THe RMS is the average variance from the mean of the sensor output. This allows users to quickly configure and compare the performance of different smoothing filters simultaneously.

## Linear Acceleration

Acceleration Explorer offers a number of different linear acceleration filters. Linear acceleration is defined as linearAcceleration = (acceleration - gravity). An acceleration sensor is not capable of determining the differnce between gravity/tilt and true linear acceleration. There is one standalone approach, a low-pass filter, and many sensor fusion based approaches. Acceleration Explorer offers implementations of all the common linear acceleration filters as well as the Android API implementation.

### Android Linear Acceleration

Android offers its own implementation of linear acceleration with Sensor.TYPE_LINEAR_ACCELERATION, which is supported by Acceleration Explorer. Most of the time the device must have a gyroscope for this sensor type to be supported. However, some devices implement Sensor.TYPE_LINEAR_ACCELERATION without a gyroscope, presumably with a low-pass filter. Regaurdless of the underlying impelementation, I have found that Sensor.TYPE_LINEAR_ACCELERATION works well for short periods of linear acceleration, but not for long periods (more than a few seconds).

To illustrate the shortcomings of Sensor.TYPE_LINEAR_ACCELERATION, I mounted a Nexus 5 in a vehicle such that the measured axis was parallel and level to the vehicle. This allowed me to measure the actual acceleration of the vechicle as accuratly as possible. I measured the acceleration of a car with Sensor.TYPE_ACCELERATION and Sensor.TYPE_LINEAR_ACCELERATION simultaneously. You can see that the linear acceleration estimation begins to deviate heavily from the actual acceleration after a short peroid of time (or our best estimation of it). I presume this is because deep under the hood of the linear acceleration algorithm, a gyroscope is used to estimatate the orientation of the device, which then calculates the gravity vector which is then subtracted from the acceleration to produce linear acceleration. I suspect the deviation occurs because the acceleration sensor is also used to compenstate the drift of the gyroscope and under sustained periods of linear acceleration, the gyroscope begins to compensate for what it thinks is a long term gravity signal, but is really sustained linear acceleration. While using the API sensors is very convenient, you may come accross a situation where it doesn't work well under the conditional requirements and you need to roll your own, more specialized implementation.

![Alt text](http://www.kircherelectronics.com/resources/images/accelerationExplorer/android_linear_acceleration_comparison.png "Android Linear Acceleration vs Raw Acceleration")

### Low-Pass Linear Acceleration

The most simple linear acceleration filter is based on a low-pass filter. It has the advantage that no other sensors are requied to estimate linear acceleration. A low-pass filter is implemented in such a way that only very long term (low-frequency) signals (i.e, gravity) are allow to pass through. Anything short term (high-frequency) is filtered out. The gravity estimation is then subtracted from the current acceleration sensor measurement, providing an estimation of linear acceleration. The low-pass filter is an IIR single-pole implementation. The coefficient, a (alpha), can be adjusted based on the sample period of the sensor to produce the desired time constant that the filter will act on. It is essentially the same as the Wikipedia LPF. It takes a simple form of gravity[0] = alpha * gravity[0] + (1 - alpha) * acceleration[0]. Alpha is defined as alpha = timeConstant / (timeConstant + dt) where the time constant is the length of signals the filter should act on and dt is the sample period (1/frequency) of the sensor. Linear acceleration can then be calculated as linearAcceleration = (acceleration - gravity). This implementation can work very well assuming the accleration sensor is mounted in a relativly fixed position and the periods of linear acceleration is relavitly short. For more information on low-pass filters, see [here](http://www.kircherelectronics.com/blog/index.php/11-android/sensors/8-low-pass-filter-the-basics) and [here](http://www.kircherelectronics.com/blog/index.php/11-android/sensors/9-low-pass-filter-optimizing-alpha).

### Sensor Fusion Complimentary Filter

Acceleration Explorer offers a number of different estimations of linear acceleraiton using sensor fusion complimentary filters. The complementary filter is a frequency domain filter. In its strictest sense, the definition of a complementary filter refers to the use of two or more transfer functions, which are mathematical complements of one another. Thus, if the data from one sensor is operated on by G(s), then the data from the other sensor is operated on by I-G(s), and the sum of the transfer functions is I, the identity matrix. In practice, it looks nearly identical to a low-pass filter, but uses two different sets of sensor measurements to produce what can be thought of as a weighted estimation. 

In most cases, the gyroscope is used to measure the devices orientation, which can then be used to produce a gravity vector, which can then be subracted from the acceleration vector to produce the linear acceleration vector. However, the gyrocope tends to drift due to round off errors and other factors. Most gyroscopes work by measuring very small vibrations in the earth's rotation, which means they really do not like external vibrations. Because of drift and external vibrations, the gyroscope has to be compensated with a second estimation of the devices orientation, which comes from the acceleration sensor and magnetic sensor. The acceleration sensor provides the pitch and roll estimations while the magnetic sensor provides the azimuth. A complimentary filter is used to fuse the two orienations together. It takes the form of gyro[0] = alpha * gyro[0] + (1 - alpha) * accel/magnetic[0]. Alpha is defined as alpha = timeConstant / (timeConstant + dt) where the time constant is the length of signals the filter should act on and dt is the sample period (1/frequency) of the sensor.

For more information on integrating the gyroscope to obtain a quaternion, rotation matrix or orientation, see [here](http://www.kircherelectronics.com/blog/index.php/11-android/sensors/15-android-gyroscope-basics).

### Orientation Euler Angles Complimentary Filter (ImuLaCfOrientation)

You can obtain an orientation comprised of Euler Angles (azimuth, pitch and roll) two ways in Android. The first method involves integrating the gyroscope measurements. The second is obtained by using the acceleration and magnetic sensors. These two measurements are ideal for a complimentary filter fusion. Paul Lawitzki wrote such an algorithm (you can find his writeup [here](http://www.thousand-thoughts.com/2012/03/android-sensor-fusion-tutorial/). I have modified it slightly and included it in Acceleration Explorer. While it is not what I consider an elegant, or particularly fast algorithm, it is highly intutive and easier to grasp than most approaches. Most importantly, it works well.

### Rotation Matrix Complimentary Filter (ImuLaCfRotationMatrix)

Rotation matrices for the gyroscope and acceleration/magnetic sensor can be obtained in much the same way as the orientation Euler angles... in fact you need them to obtain the orientation. Rotation matrices can be scaled with a scalar matrix just like any other matrix, which allows them to be used in a complementary filter. Instead of using the orientation, the complementary filter is applied to the rotation matrices which is slightly more efficient and significantly more elegant than using the orientations. Rotation matrices suffer from many singularites including gimbal lock, so they are not ideal. However, many people are familiar with the concept of rotation matrices so this approach may be more simple to understand.

### Quaternions Complimentary Filter (ImuLaCfQuaternion)

Quaternions offer an angle-axis solution to rotations which do not suffer from many of the singularies, including gimbal lock, that you will find with rotation matrices. Quaternions can also be scaled and applied to a complimentary filter. The quaternion complimentary filter is probably the most elegant, robust and accurate of the filters, although it can also be the most difficult to implement.

### Quaternion Kalman Filter (ImuLaKfQuaternion)

Kalman filtering, also known as linear quadratic estimation (LQE), is an algorithm that uses a series of measurements observed over time, containing noise (random variations) and other inaccuracies, and produces estimates of unknown variables that tend to be more precise than those based on a single measurement alone. More formally, the Kalman filter operates recursively on streams of noisy input data to produce a statistically optimal estimate of the underlying system state. Much like complimentary filters, Kalman filters require two sets of estimations, which we have from the gyroscope and acceleration/magnetic senor. The Acceleration Explorer implementation of the Kalman filter relies on quaternions. 



