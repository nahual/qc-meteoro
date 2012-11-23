import os
import json
from yahoo import YahooWeatherProvider
from mock import MockWeatherProvider

os.sys.path.append(os.path.abspath(os.path.dirname(__file__)))

from flask import Flask, request, render_template

PORT=int(os.environ.get('PORT', 5000))
HOST='localhost'
LOG_HANDLERS=[]

app = Flask(__name__)
app.config.from_object(__name__)
app.config.from_envvar('CONF', silent=True)

@app.route('/')
def index():
    return render_template('index.html', node=app.config['NODE_NAME'], environment=app.config['ENV'], provider=weather_provider.__class__.__name__)

@app.route('/get_cities', methods=['GET'])
def get_cities():
    contains = request.args.get('contains')
    return json.dumps(weather_provider.get_cities(contains))

@app.route('/get_forecast', methods=['GET'])
def get_forecast():
    if not working:
        return json.dumps({'error':'Service down'}), 503
    city = request.args.get('city')
    if not city:
        return json.dumps({'error':'City is mandatory'}), 400
    result = weather_provider.get_forecast(city)
    return json.dumps(result), ("error" in result and 400) or 200

@app.route('/change_provider', methods=['GET'])
def chage_provider():
    name = request.args.get('name')
    if name not in providers:
        return json.dumps({'error':"%s is not a provider" % name }), 400
    global weather_provider
    weather_provider = providers[name]
    return json.dumps({'message':'ok'}) 

@app.route('/toggle', methods=['GET'])
def toggle_out_of_service():
    global working
    working = not working
    return json.dumps({'message':(working and 'Now working') or 'Not working anymore'})


if __name__ == '__main__':
    providers = {
        'yahoo': YahooWeatherProvider(log_handlers=app.config['LOG_HANDLERS']),
        'mock': MockWeatherProvider(log_handlers=app.config['LOG_HANDLERS']),
    }
    weather_provider = providers['yahoo'] 
    working = True
    app.run(host=app.config['HOST'], port=app.config['PORT'], debug=app.config['DEBUG'])

