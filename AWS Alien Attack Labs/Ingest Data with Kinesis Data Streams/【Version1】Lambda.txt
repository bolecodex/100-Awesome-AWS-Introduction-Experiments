# index.js
exports.handler = async (event) => {
    // let's log the incoming event
    let payloadAsString = JSON.stringify(event);
    console.log(payloadAsString);
    const response = {
        statusCode: 200,
        body: `we received ${payloadAsString}`
    };
    return response;
};
