# -*- coding: utf-8 -*-
import os
os.sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__),"..")))
from utils import llog
import logging

ENV = 'Development'
DEBUG = True
HOST = '0.0.0.0'

NODE_NAME=u'local'

LOG_HANDLERS=[
                llog.file("%s.log" % NODE_NAME, when=llog.every(1, llog.DAY)),
                llog.console(level=logging.DEBUG)
             ]

