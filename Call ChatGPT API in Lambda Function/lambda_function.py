import openai
import os
def lambda_handler(event, context):
	openai.api_key = os.getenv('API_KEY')
	location = "New York, NY"
	messages =[
	{"role":"system", "content": "You are a helpful assistant."},
	{"role":"user", "content": f"Tell me an interesting fact about {location} in less than 200 characters"}
	]

	chat = openai.ChatCompletion.create(model="gpt-3.5-turbo", messages=messages)
	gpt_response = chat.choices[0].message.content
	html_output = f"""
	<h1>Property for sale in {location}</h1>
	<p>{gpt_response}<p>
	"""
	print("gpt_response:", gpt_response)
	return {
		"statusCode": 200,
		"body": html_output,
		"headers": {
			"Content-Type": 'text/html',
			}
		}
lambda_handler("","")