'use strict';

const AWS = require('aws-sdk');
const DynamoDB = new AWS.DynamoDB.DocumentClient();

/**
* This function formats the response in accordance to what is
* expected with Lambda Integration. You may need to change it
* if you move the code out of Lambda
*/
const formatResponse = function (err, data, callback) {
    let response = {
        isBase64Encoded: false,
        statusCode: null,
        body: null,
        headers: {
            'Content-Type': null
        }
    };
    if (err) {
        console.log('ERROR');
        console.log(err);
        response.statusCode = err.statusCode;
        response.body = err.message;
        response.headers['Content-Type'] = 'text/plain';
    } else {
        console.log('SUCCESS');
        console.log(data);
        if (data == null) {
            response.statusCode = 404;
            response.headers['Content-Type'] = 'text/plain';
            response.body = 'Inexistent session';
            // If you want to test the base64 encoding, comment the line above, and uncomment those two below
            //response.isBase64Encoded = true;
            //response.body = 'SW5leGlzdGVudCBzZXNzaW9u';
        } else {
            response.statusCode = 200;
            response.headers['Content-Type'] = 'application/json';
            response.body = JSON.stringify(data);
        }
    }
    return response;
};

/**
* This function reads from the database. 
* You can replace this with the code depending on your database
*/
const readTopxDataFromDatabase = function (sessionId, callback) {
    console.log('PROVIDED SESSION ID FOR DATABASE QUERY IS:', sessionId);
    let params = {
        "TableName": process.env.TABLENAME,
        "Key": {
            "SessionId": sessionId
        }
    };
    DynamoDB.get(params, (err, data) => {
        if (err) {
            callback(err, null);
        }
        else {
            if (data && data.Item) callback(null, data.Item);
            else callback(null, {});
        }
    });
};

/**
* This function does the required computation.
*/
const computeStatisticsForSession = function (sessionId, callback) {
    // let's start by reading the session data from the database
    // retrieving the record attached to 'sessionId'
    readTopxDataFromDatabase(sessionId, (err, topXSessionData) => {
        if (err) callback(err);
        else {
            if (!topXSessionData.TopX)
                // Table is empty. No data found.
                callback(null, null);
            else {
                let statistics = [];
                let position = 1;
                // Make the computations
                topXSessionData.TopX.forEach((item) => {
                    let itemStatistics = {};
                    itemStatistics['Nickname'] = item.Nickname;
                    itemStatistics['Position'] = position++;
                    if (item.Shots != 0) {
                        itemStatistics['Performance'] = item.Score / item.Shots;
                    } else {
                        if (item.Score != 0) itemStatistics['Performance'] = -1;
                        else itemStatistics['Performance'] = 0;
                    }
                    statistics.push(itemStatistics);
                });
                callback(null, statistics);
            }
        }
    });
};

/**
* This function validates the event, and must
* be rewritten if you move this out of Lambda
*/
const validateEvent = function (event) {
    let sessionId = null;
    let isProxyIntegration = null;
    try {
        // if this structure is present, we have a direct integration
        sessionId = event.params.querystring.sessionId;
        isProxyIntegration = false;
    } catch (_) {
        try {
            // if this structure is present, we have a Proxy integration
            sessionId = event.queryStringParameters.sessionId;
            isProxyIntegration = true;
        } catch (_) {
            sessionId = null;
            isProxyIntegration = null;
        }
    }
    let result = {
        "sessionId": sessionId,
        "isProxyIntegration": isProxyIntegration
    };
    console.log(result);
    return result;
};

/**
* This is the entry-point for the Lambda function
*/
exports.handler = (event, context, callback) => {
    // Let's show the received event
    console.log(event);
    // Let's validate the event, to extract the sessionId (if we have one)
    // eventValidation will have the form { "sessionId" : sessionIdValue, "isProxyIntegration" : [ true | false ] }
    // isProxyIntegration tells us about the API Gateway integration (if any) to this lambda function
    let eventValidation = validateEvent(event);
    let sessionId = eventValidation.sessionId;
    // call the function that computes the statistics
    computeStatisticsForSession(sessionId, (err, data) => {
        // let's format the response to what is expected from an API Gateway integration
        let response = formatResponse(err, data);
        callback(null, response);
    });
};
