# Ingest Data with Kinesis Data Streams


The requests are going to come in this format:

{
    "User" : <String uniquely identifying a user in the client platform>,
    "Client" : <String uniquely indentifying an application/platform>,
    "Timestamp" : <UTC timestamp in the format YYYY-MM-DDTHH:mm:ss.mmmZ",
    "Order" : {
        "Symbol" : <String identifying a symbol>,
        "Volume" : <Number, positive for buying, negative for selling>,
        "Price" : <current price>
    }
}
As an example, maybe we can have something like this:

{
    "User" : "theuser@amazon.com",
    "Client" : "13522bac-89fb-4f14-ac37-92642eec2b06",
    "Timestamp" : "2021-02-01T18:42:35.903Z",
    "Order" : {
        "Symbol" : "USDJPY",
        "Volume" : 200000,
        "BuyingPrice" : 104.987
    }
}