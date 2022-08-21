import requests

def lambda_handler(event, context):
    res = requests.get("https://ip.gs")  # 获取Lambda的IP
    print(res.content)