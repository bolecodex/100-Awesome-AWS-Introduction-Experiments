var awsIot = require('aws-iot-device-sdk');
var colors = require('colors');
var config = require('./device.json');

/**
 * Initialize the IoT device with a random clientID.
 * This will use the configuration variables stored
 * in the device.json file.
 */
var device = awsIot.thingShadow({
  keyPath: config.certs.keyPath,
  certPath: config.certs.certPath,
  caPath: config.certs.caPath,
  clientId: config.thing,
  host: config.host
});

// Configuration variables.
var airConditioningIsOn = false;
var temp = 45;
var DELTA = config.app.delta;
var PUBLISH_INTERVAL = config.app.publishInterval;
var STATUS_INTERVAL = config.app.statusInterval;

/**
 * This function will increase the temperature by the DELTA
 * value set. It also rounds the number to the nearest
 * 2 decimal places.
 */
function increaseTemperature() {
  temp = Math.round((temp + DELTA) * 100) / 100;
}

/**
 * This function will decrease the temperature by the DELTA
 * value set. It also rounds the number to the nearest
 * 2 decimal places.
 */
function decreaseTemperature() {
  temp = Math.round((temp - DELTA) * 100) / 100;
}

/**
 * This function will return a colored text string
 * based on the temperature value.
 * @param val The temperature value.
 * @param text The text to colorize.
 */
function getColoredTemperatureText(val, text) {
  if (val < 50) {
    return text.blue + ' (COLD!)'.gray;
  } else if (val < 65) {
    return text.cyan + ' (BRISK)'.gray;
  } else if (val < 80) {
    return text.yellow + ' (WARM)'.gray;
  } else {
    return text.red + ' (HOT!!)'.gray;
  }
}

/**
 * This function operates on a timer, simulates a change
 * in temperature and publishes the current temperature
 * value to AWS IoT
 */
function run() {
  if (airConditioningIsOn) {
    decreaseTemperature();
  } else {
    increaseTemperature();
  }
  var payload = JSON.stringify({
    temperature: temp
  });
  device.publish(config.topic, payload);
  console.log('Published '.gray + getColoredTemperatureText(temp, payload) + 'to AWS IoT.'.gray);
}

/**
 * This function reports the state of the system to the
 * AWS IoT Device Shadow.
 */
function reportState() {
  try {
    var stateObject = { "state": { "reported": { "airConditioningIsOn": airConditioningIsOn } } }
    var clientTokenUpdate = device.update(config.thing, stateObject);
    if (clientTokenUpdate === null) {
      console.log('ERROR: Reporting state failed, operation still in progress'.red);
    }
  } catch (err) {
    console.log('ERROR: Unknown error reporting state.'.red);
  }
}

// Connect to AWS IoT.
console.log('Connecting to AWS IoT...'.blue);
device.on('connect', function () {
  console.log('Connected to AWS IoT.'.blue);
  device.register(config.thing);
  setInterval(run, PUBLISH_INTERVAL);
  setInterval(reportState, STATUS_INTERVAL);
});

/**
 * on(status) runs whenever a change in status is noticed.
 * In particular, it will run when the device.get(),
 * device.update() methods are invoked.
 */
device.on('status',
  function (thingName, stat, clientToken, stateObject) {
    console.log("Reported current state: ".gray + JSON.stringify(stateObject.state.reported).gray);
  });

/**
 * on(delta) runs when after an update operation, there is
 * a different state noticed on the server than on the
 * device. In this case, if we change the condition
 * airConditioningIsOn to true, and locally it is false,
 * a change in state will be detected.
 */
device.on('delta',
  function (thingName, stateObject) {
    try {
      console.log("Reported state different from remote state.".gray)
      if (stateObject.state.airConditioningIsOn && stateObject.state.airConditioningIsOn != airConditioningIsOn) {
        airConditioningIsOn = stateObject.state.airConditioningIsOn;
        if (airConditioningIsOn) {
          console.log('Air conditioning is now on.'.green);
        } else {
          console.log('Air conditioning is now off.'.red);
        }
      } else {
        console.log('No new desired state found.'.gray);
      }
    } catch (err) {
      console.log("ERROR setting air conditioner.".red);
    }
  });