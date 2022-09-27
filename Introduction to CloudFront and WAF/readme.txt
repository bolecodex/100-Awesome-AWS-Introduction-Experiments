https://test.bolecodex.com/?<script>alert(0);</script>

https://test.bolecodex.com/?SELECT+1+From+BobbyTables


https://bolecodex.com/?SELECT+1+From+BobbyTables

{
    "Version": "2008-10-17",
    "Id": "PolicyForCloudFrontPrivateContent",
    "Statement": [
        {
            "Sid": "1",
            "Effect": "Allow",
            "Principal": {
                "AWS": "arn:aws:iam::cloudfront:user/CloudFront Origin Access Identity EF65QMYHOMNOW"
            },
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::test.bolecodex.com/*"
        }
    ]
}