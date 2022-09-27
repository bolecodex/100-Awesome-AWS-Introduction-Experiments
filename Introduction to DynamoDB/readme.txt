aws dynamodb list-tables

aws dynamodb get-item --table-name Employees --key '{"id" : { "S":"1"}}'

{
 "id": "1",
 "name": "Weibo",
 "email": "bolecodex@gmail.com",
 "location": "Germany",
 "photo": "employee-1.png"
}