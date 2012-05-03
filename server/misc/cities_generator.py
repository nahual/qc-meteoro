from yaml import load, dump
import strings

cities = {}

def get_data(line):
    data = city.split("\t")
    location = data[-2].split("/")[1:]
    location.reverse()
    location = data[1] + ", " + ", ".join(location).replace("_", " ")
    if "Argentina" in location:
        return strings.normalize(location), location
    return None, None 

with open('cities15000.txt', 'r') as f:
    for city in f:
        name, location = get_data(city)
        if name:
            cities[name] = location

with open('cities5000.txt', 'r') as f:
    for city in f:
        name, location = get_data(city)
        if name:
            cities[name] = location

with open('cities1000.txt', 'r') as f:
    for city in f:
        name, location = get_data(city)
        if name:
            cities[name] = location

print len(cities)
with open("cities.yaml","w") as f:
    f.write(dump(cities))
