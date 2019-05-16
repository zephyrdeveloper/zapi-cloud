# zapi_py
zephyr for jira cloud api 


# Instructions
1. Download this folder

2. Make sure you have Python + pip installed, if not download these. (and make sure they are on your PATH :) )

3. Use pip to install the following two libraries:
  pip install PyJWT
  pip install requests

4. Change the values in CycleTest.py
  ACCOUNT_ID -> your Atlassian Account ID, go to your profile page in Jira and the last part of the URL will be your ID
                        e.g. https://yourcompany.atlassian.net/people/4d0bca695f7d9121cd2ac29f -> your id is 4d0bca695f7d9121cd2ac29f
  ACCESS_KEY -> from navigation >> Tests >> API KEYS
  SECRET_KEY -> from navigation >> Tests >> API KEYS
  BASE_URL -> https://prod-api.zephyr4jiracloud.com/connect
  
  Note: if you want to test using another request, make sure you set the right HTTP method on both line 35 & line 71 (by calling the correct method: requests.get, requests.post etc...) 
  
5. Run CycleTest.py using the following command:
  python CycleTest.py
