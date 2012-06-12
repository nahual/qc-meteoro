# -*- coding: utf-8 -*-
from weather_provider import WeatherProvider
import urllib2
import json
from datetime import datetime, timedelta

def safe_get(data, keys):
    try:
        for key in keys:
            data = data[key]
        return data
    except KeyError:
        return ''

class YahooWeatherProvider(WeatherProvider):
    def __init__(self, log_handlers):
        WeatherProvider.__init__(self, "MockWeahterProvider", log_handlers)
        self.url = 'http://weather.yahooapis.com/forecastjson?w=%s&d=3&u=c'
        self._cities = [
            {'code': u'468739', 'name': u'Buenos Aires, Buenos Aires'},
			{'code': u'466861', 'name': u'C\xf3rdoba, C\xf3rdoba'},
			{'code': u'466862', 'name': u'Rosario, Santa Fe'},
			{'code': u'332471', 'name': u'Mendoza, Mendoza'},
			{'code': u'332469', 'name': u'La Plata, Buenos Aires'},
			{'code': u'466865', 'name': u'San Miguel de Tucum\xe1n, Tucum\xe1n'},
			{'code': u'466863', 'name': u'Mar del Plata, Buenos Aires'},
			{'code': u'466864', 'name': u'Salta, Salta'},
			{'code': u'332470', 'name': u'Santa Fe, Santa Fe'},
			{'code': u'466870', 'name': u'Resistencia, Chaco'},
			{'code': u'466868', 'name': u'Neuqu\xe9n, Neuqu\xe9n'},
			{'code': u'332474', 'name': u'Santiago del Estero, Santiago del Estero'},
			{'code': u'466867', 'name': u'Corrientes, Corrientes'},
			{'code': u'464712', 'name': u'Avellaneda, Buenos Aires'},
			{'code': u'466866', 'name': u'Bah\xeda Blanca, Buenos Aires'},
			{'code': u'466315', 'name': u'Quilmes, Buenos Aires'},
			{'code': u'466212', 'name': u'Moreno, Buenos Aires'},
			{'code': u'332475', 'name': u'Concordia, Entre R\xedos'},
			{'code': u'332477', 'name': u'La Rioja, La Rioja'},
			{'code': u'466871', 'name': u'R\xedo Cuarto, C\xf3rdoba'},
			{'code': u'419824', 'name': u'San Fernando del Valle de Catamarca, Catamarca'},
			{'code': u'466874', 'name': u'Comodoro Rivadavia, Chubut'},
			{'code': u'465596', 'name': u'Isidro Casanova, Buenos Aires'},
			{'code': u'465609', 'name': u'Ituzaingo, Buenos Aires'},
			{'code': u'332026', 'name': u'San Nicol\xe1s de los Arroyos, Buenos Aires'},
			{'code': u'466961', 'name': u'Florencio Varela, Buenos Aires'},
			{'code': u'465703', 'name': u'Lomas de Zamora, Buenos Aires'},
			{'code': u'466588', 'name': u'Temperley, Buenos Aires'},
			{'code': u'332471', 'name': u'Mendoza, Mendoza'},
			{'code': u'465870', 'name': u'Monte Grande, Buenos Aires'},
			{'code': u'464812', 'name': u'Bernal, Buenos Aires'},
			{'code': u'331983', 'name': u'San Justo, Buenos Aires'},
			{'code': u'464979', 'name': u'Castelar, Buenos Aires'},
			{'code': u'332481', 'name': u'San Rafael, Mendoza'},
			{'code': u'466322', 'name': u'Rafael Castillo, Buenos Aires'},
			{'code': u'466888', 'name': u'Trelew, Chubut'},
			{'code': u'332914', 'name': u'Santa Rosa, La Pampa'},
			{'code': u'466887', 'name': u'Tandil, Buenos Aires'},
			{'code': u'465663', 'name': u'Lan\xfas, Buenos Aires'},
			{'code': u'466330', 'name': u'Ramos Mej\xeda, Buenos Aires'},
			{'code': u'468697', 'name': u'Villa Mercedes, San Luis'},
			{'code': u'465896', 'name': u'Mor\xf3n, Buenos Aires'},
			{'code': u'22529976', 'name': u'Virrey del Pino, Buenos Aires'},
			{'code': u'330096', 'name': u'Caseros, Buenos Aires'},
			{'code': u'332478', 'name': u'San Carlos de Bariloche, R\xedo Negro'},
			{'code': u'467000', 'name': u'Maip\xfa, Mendoza'},
			{'code': u'466890', 'name': u'Z\xe1rate, Buenos Aires'},
			{'code': u'464852', 'name': u'Burzaco, Buenos Aires'},
			{'code': u'466880', 'name': u'Pergamino, Buenos Aires'},
			{'code': u'465489', 'name': u'Grand Bourg, Buenos Aires'},
			{'code': u'465867', 'name': u'Monte Chingolo, Buenos Aires'},
			{'code': u'466879', 'name': u'Olavarr\xeda, Buenos Aires'},
			{'code': u'467039', 'name': u'Rawson, San Juan'},
			{'code': u'466885', 'name': u'Rafaela, Santa Fe'},
			{'code': u'332777', 'name': u'Jun\xedn, Buenos Aires'},
			{'code': u'464933', 'name': u'Remedios de Escalada, Buenos Aires'},
			{'code': u'468916', 'name': u'La Tablada, Buenos Aires'},
			{'code': u'467042', 'name': u'R\xedo Gallegos, Santa Cruz'},
			{'code': u'466913', 'name': u'Campana, Buenos Aires'},
			{'code': u'466883', 'name': u'Presidencia Roque S\xe1enz Pe\xf1a, Chaco'},
			{'code': u'467046', 'name': u'Rivadavia, San Juan'},
			{'code': u'332398', 'name': u'Villa Madero, Buenos Aires'},
			{'code': u'465977', 'name': u'Olivos, Buenos Aires'},
			{'code': u'332486', 'name': u'Gualeguaych\xfa, Entre R\xedos'},
			{'code': u'468744', 'name': u'Villa Gobernador G\xe1lvez, Santa Fe'},
			{'code': u'465736', 'name': u'Villa Luzuriaga, Buenos Aires'},
			{'code': u'465049', 'name': u'Chimbas, San Juan'},
			{'code': u'465079', 'name': u'Ciudadela, Buenos Aires'},
			{'code': u'465726', 'name': u'Luj\xe1n de Cuyo, Mendoza'},
			{'code': u'465376', 'name': u'Ezpeleta, Buenos Aires'},
			{'code': u'466889', 'name': u'Villa Mar\xeda, C\xf3rdoba'},
			{'code': u'466974', 'name': u'General Roca, R\xedo Negro'},
			{'code': u'332656', 'name': u'San Fernando, Buenos Aires'},
			{'code': u'332484', 'name': u'Ciudad Evita, Buenos Aires'},
			{'code': u'467074', 'name': u'Venado Tuerto, Santa Fe'},
			{'code': u'466908', 'name': u'Bella Vista, Buenos Aires'},
			{'code': u'466996', 'name': u'Luj\xe1n, Buenos Aires'},
			{'code': u'468746', 'name': u'San Ram\xf3n de la Nueva Or\xe1n, Salta'},
			{'code': u'467187', 'name': u'Cipolletti, R\xedo Negro'},
			{'code': u'466978', 'name': u'Goya, Corrientes'},
			{'code': u'466821', 'name': u'Wilde, Buenos Aires'},
			{'code': u'465801', 'name': u'Mart\xednez, Buenos Aires'},
			{'code': u'466878', 'name': u'Necochea, Buenos Aires'},
			{'code': u'465311', 'name': u'Don Torcuato, Buenos Aires'},
			{'code': u'466900', 'name': u'Banda del R\xedo Sal\xed, Tucum\xe1n'},
			{'code': u'332485', 'name': u'Concepci\xf3n del Uruguay, Entre R\xedos'},
			{'code': u'466975', 'name': u'General Rodr\xedguez, Buenos Aires'},
			{'code': u'332345', 'name': u'Villa Carlos Paz, C\xf3rdoba'},
			{'code': u'466455', 'name': u'Sarand\xed, Buenos Aires'},
			{'code': u'467953', 'name': u'Villa Elvira, Buenos Aires'},
			{'code': u'466737', 'name': u'Villa Dom\xednico, Buenos Aires'},
			{'code': u'464784', 'name': u'B\xe9ccar, Buenos Aires'},
			{'code': u'467248', 'name': u'Glew, Buenos Aires'},
			{'code': u'467032', 'name': u'Puerto Madryn, Chubut'},
			{'code': u'466884', 'name': u'Punta Alta, Buenos Aires'},
			{'code': u'330474', 'name': u'El Palomar, Buenos Aires'},
			{'code': u'466321', 'name': u'Rafael Calzada, Buenos Aires'},
			{'code': u'467062', 'name': u'Tartagal, Salta'},
			{'code': u'332520', 'name': u'San Pedro de Jujuy, Jujuy'},
			{'code': u'330009', 'name': u'Bel\xe9n de Escobar, Buenos Aires'},
			{'code': u'22528491', 'name': u'Los Hornos, Buenos Aires'},
			{'code': u'467312', 'name': u'Mariano Acosta, Buenos Aires'},
			{'code': u'464505', 'name': u'Los Polvorines, Buenos Aires'},
			{'code': u'466897', 'name': u'Azul, Buenos Aires'},
			{'code': u'466930', 'name': u'Chivilcoy, Buenos Aires'},
			{'code': u'465704', 'name': u'Lomas del Mirador, Buenos Aires'},
			{'code': u'467043', 'name': u'R\xedo Grande, Tierra del Fuego'},
			{'code': u'22528584', 'name': u'Guernica, Buenos Aires'},
			{'code': u'466973', 'name': u'General Pico, La Pampa'},
			{'code': u'464833', 'name': u'Bosques, Buenos Aires'},
			{'code': u'467017', 'name': u'Ober\xe1, Misiones'},
			{'code': u'466903', 'name': u'Barranqueras, Chaco'},
			{'code': u'331457', 'name': u'Yerba Buena/Marcos Paz, Tucum\xe1n'},
			{'code': u'331997', 'name': u'San Mart\xedn, Mendoza'},
			{'code': u'56040448', 'name': u'El Jag\xfcel, Buenos Aires'},
			{'code': u'22529956', 'name': u'Villa Mariano Moreno/El Colmenar, Tucum\xe1n'},
			{'code': u'466953', 'name': u'Eldorado, Misiones'},
			{'code': u'467300', 'name': u'Longchamps, Buenos Aires'},
			{'code': u'466933', 'name': u'Clorinda, Formosa'},
			{'code': u'467077', 'name': u'Viedma, R\xedo Negro'},
			{'code': u'467068', 'name': u'Tres Arroyos, Buenos Aires'},
			{'code': u'467070', 'name': u'Ushuaia, Tierra del Fuego'},
			{'code': u'331928', 'name': u'San Isidro, Buenos Aires'},
			{'code': u'467021', 'name': u'Palpala, Jujuy'}
		]
        self._statuses = {
            '11': 'showers', '12': 'showers', '8': 'showers',
            '40': 'scattered showers', '9': 'scattered showers',
            '26': 'cloudy', '27': 'cloudy', '28': 'cloudy',
            '29': 'partly cloudy', '30': 'partly cloudy', '44': 'partly cloudy',
            '24': 'windy', '23': 'windy', '2': 'windy', '1': 'windy', '0': 'windy',
            '32': 'clear', '31': 'clear', '33': 'clear', '34': 'clear', '25': 'clear', '36': 'clear',
            '16': 'snow', '41': 'snow', '43': 'snow', '42': 'snow', '46': 'snow', '13': 'snow', '14': 'snow', '15': 'snow',
            '3': 'storm', '4': 'storm', '37': 'storm', '38': 'storm', '39': 'storm', '45': 'storm', '47': 'storm',
            '5': 'snow rain', '6': 'snow rain', '10': 'snow rain', '7': 'snow rain', '18': 'snow rain', '35': 'snow rain', '17': 'snow rain',
            '20': 'fog', '21': 'fog', '22': 'fog', '19': 'fog',
		}
        self._text_statuses = {
            'cloudy': 'cloudy', 'clouds': 'cloudy',
            'partly cloudy': 'partly cloudy',
            'clear': 'clear', 'sunny': 'clear',
            'rain': 'showers', 'showers': 'showers',
            'light rain': 'scattered showers', 'few showers': 'scattered showers', 'scattered showers': 'scattered showers',
            'mist': 'fog',
            'isolated thunderstorms': 'storm', 'thunderstorms': 'storm', 'scattered thunderstorms': 'storm',
            'snow showers': 'snow'
        }

    def get_forecast(self, city):
        try:
            data = urllib2.urlopen(self.url % city)
            response = json.loads(data.read())
            if 'Message' in response:
                raise Exception(response['Message'])
            return self._build_meteoro_result(response)
        except Exception as e:
            self._log.error("Error while getting weather from Yahoo", exc_info = True)
            return {'error':'Problem with Yahoo!: %s' % e}

    def _build_meteoro_result(self, response):
        today = datetime.now()
        rv = []
        rv.append({
            'date': today.strftime(self.date_format),
            'temperature': "%s°C" % safe_get(response, ['condition', 'temperature']),
            'pressure': "%shPa" % safe_get(response, ['atmosphere','pressure']),
            'humidity': "%s%%" % safe_get(response, ['atmosphere','humidity']),
            'status': self._statuses.get(safe_get(response, ['condition','code']), safe_get(response, ['condition','text']).lower()),
            'min': '', 'max': ''
        })
        forecast = safe_get(response, ['forecast'])
        if forecast:
            for i, day in enumerate(forecast):
                rv.append({
                    'date': (today+timedelta(days=i)).strftime(self.date_format),
                    'min': "%s°C" % safe_get(day, ['low_temperature']),
                    'max': "%s°C" % safe_get(day, ['high_temperature']),
                    'status': self._text_statuses.get(YahooWeatherProvider._normalize_status(safe_get(day, ['condition'])), YahooWeatherProvider._normalize_status(safe_get(day, ['condition']))),
                    'temperature': '', 'pressure': '', 'humidity': ''
                })
        return rv

    @staticmethod
    def _normalize_status(status):
        return status.lower().split("/")[0].replace("am","").replace("pm","").replace("early","").replace("late","").replace("mostly","").strip()

