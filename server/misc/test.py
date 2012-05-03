from yaml import load
import unicodedata
with open('cities.yaml', 'r') as f:
    cities = load(f)
    cities_list = cities.values()

listita = map(lambda c: len(unicodedata.normalize('NFKD', c.decode("UTF-8")).encode('ascii', 'ignore')), cities_list)  
print max(listita)
