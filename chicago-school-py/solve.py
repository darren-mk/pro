import requests

url = "https://data.cityofchicago.org/resource/2m8w-izji.json"

resp = requests.get(url)

content = resp.json()

def doesDictHaveTheField(d):
    return 'cps_performance_policy_level' in d.keys()

sampleD = {'a': 123, 'b': 456, 'cps_performance_policy_level': 234}

contentThatHaveTheField = list(filter(doesDictHaveTheField, content))

def extractTheField(d):
    return d['cps_performance_policy_level']

contentOfTheField = list(map(extractTheField, contentThatHaveTheField))

lv1, lv2, lv3 = 0, 0, 0

for item in contentOfTheField:
    if item == 'LEVEL 1':
        lv1 = lv1 + 1
    elif item == 'LEVEL 2':
        lv2 = lv2 + 1
    elif item == 'LEVEL 3':
        lv3 = lv3 + 1
    else: pass

print(lv1)
print(lv2)
print(lv3)
