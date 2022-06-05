#set($inputPath = $input.path('$'))
{
"PartitionKey" : "$inputPath.User#$inputPath.Client#$inputPath.Order.Symbol#$inputPath.Order.Volume#$inputPath.Order.Price#$inputPath.Timestamp",
"Data" : "$util.base64Encode("$input.json('$')")",
"StreamName" : "TradingStream"
}