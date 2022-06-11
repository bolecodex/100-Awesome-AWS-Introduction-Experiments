## example request
getSystemSettings(systemName : "system01")

## example response
{ 
    "SystemName" : "system01",
    "Parameters : [
        { 
            "Name" : "/systems/system01/parameter1",
            "Value": "value of parameter1"
        },
        { 
            "Name" : "/systems/system01/parameter2",
            "Value": "value of parameter2"
        },
        ...
    ]
}

aws ssm put-parameter --name /systems/system10/clientid --description "The application client ID" --value abcd01 --type String
aws ssm put-parameter --name /systems/system10/url --description "The system URL" --value "system01.mycompany.com" --type String
aws ssm put-parameter --name /systems/system10/latestReview --description "Date of the latest well-architected review" --value "null" --type String
aws ssm put-parameter --name /systems/system10/reviewPeriodicityInDays --description "Periodicity of WARs" --value "90" --type String


## Schema
type SystemSettings @model {
    SystemName: String!
    Parameters: [Parameter]
}

type Parameter @model {
    Name: String!
    Value: String!
}

type Query {
    getSystemSettings(systemName: String): SystemSettings
}

schema {
    query: Query
}

## ssmconfig.json

{
        "endpoint" : "https://ssm.<region>.amazonaws.com/",
        "authorizationConfig" : {
                "authorizationType" : "AWS_IAM",
                "awsIamConfig" : {
                        "signingRegion" : "<region>",
                        "signingServiceName" : "ssm"
                }
        }
}


aws appsync create-data-source  --api-id <api-id> \
                                --name ssm_iam \
                                --type HTTP \
                                --http-config file://<file-path> \
                                --service-role-arn <role-arn>

aws appsync list-data-sources --api-id <api-id> 


## REQUEST
#**
Given a SystemId, this returns all the parameters related to that system
*#
#set( $ssmRequestBody = 
    {
    "Path":  "/systems/$context.args.systemName",
    "Recursive" : true
    }
)
{
    "version": "2018-05-29",
    "method": "POST",
    "resourcePath": "/",
    "params":{
        "headers": {
            "X-Amz-Target" : "AmazonSSM.GetParametersByPath",
            "Content-Type" :     "application/x-amz-json-1.1"
        },
        "body" : $util.toJson($ssmRequestBody)
    }
}


# RESPONSE
#if($ctx.error)
    $util.error($ctx.error.message, $ctx.error.type)
#end
#if($ctx.result.statusCode == 200)
    #set( $body = $util.parseJson($ctx.result.body) )
    #set($arrayOfParameters = [])
    #foreach( $item in $body.Parameters )
        $util.qr( $arrayOfParameters.add( { "Name" : $item.Name, "Value" : $item.Value } ) )
    #end
    $util.toJson( { "SystemName" : $ctx.arguments.systemName , "Parameters" : $arrayOfParameters }  )
#else
    $util.toJson($ctx.error)
    $utils.appendError($ctx.result.body, "$ctx.result.statusCode")
#end


query MyQuery {
    getSystemConfiguration(systemName: "system10") {
        SystemName
        Parameters {
            Name
            Value
        }
    }
}

curl -H "content-Type:application/graphql" -H "x-api-key:<API-KEY>" -X POST -d '{ "query": "query MyQuery { getSystemSettings( systemName : \"system10\" ) { Parameters { Name Value } SystemName } }" }' <API-URL>
