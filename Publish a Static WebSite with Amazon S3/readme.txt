git clone https://github.com/aurbac/static-website.git

aws s3 cp static-website/ s3://bolecodex-website/ --recursive --exclude ".git/*" --acl public-read
