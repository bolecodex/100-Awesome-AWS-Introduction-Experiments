aws dynamodb create-table --cli-input-json file://notestable.json --region us-east-1

aws dynamodb put-item --table-name Notes --item '{"UserId":{"S":"StudentD"}, "NoteId":{"N":"42"}, "Notes":{"S":"Test note"}}'

aws dynamodb put-item --table-name Notes --item '{"UserId":{"S":"StudentB"}, "NoteId":{"N":"12"}, "Notes":{"S":"Thanks"}, "Favorite":{"S":"Yes"}}'

aws dynamodb get-item --table-name Notes --key '{"UserId": {"S": "StudentD"}, "NoteId": {"N": "42"}}'

aws dynamodb query --table-name Notes --key-condition-expression "UserId = :userid" --expression-attribute-values '{":userid":{"S":"StudentD"}}'

aws dynamodb scan --table-name Notes --filter-expression "UserId = :userid" --expression-attribute-values '{":userid":{"S":"StudentD"}}'


aws dynamodb update-item --table-name Notes --key '{"UserId": {"S": "StudentD"}, "NoteId": {"N": "42"}}' --update-expression "SET Notes = :newnote" --expression-attribute-values '{":newnote":{"S":"Amazon DynamoDB is a …"}}' --return-values ALL_NEW 

aws dynamodb update-item --table-name Notes --key '{"UserId": {"S": "StudentB"}, "NoteId": {"N": "12"}}' --update-expression "SET Notes = :newnote" --condition-expression "NOT attribute_exists(Favorite)" --expression-attribute-values file://expression-attribute-values.json

aws dynamodb delete-item --table-name Notes --key '{"UserId": {"S": "StudentD"}, "NoteId": {"N": "42"}}' --return-values ALL_OLD
