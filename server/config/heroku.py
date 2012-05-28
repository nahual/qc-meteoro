import os
os.sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__),"..")))
from utils import llog
import logging

ENV = 'Heroku'
DEBUG = False
HOST = '0.0.0.0'

NODE_NAME=u'Heroku'

LOG_HANDLERS=[
                llog.console(level=logging.INFO, format='%(message)s')
             ]

