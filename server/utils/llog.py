# Going one step below Mariano Guerra's mlog
# https://github.com/marianoguerra/me/tree/master/code/python/mlog
# thanks to him for the idea and basic implementation

import inspect
import logging
import logging.handlers

SECOND   = SECONDS   = 'S'
MINUTE   = MINUTES   = 'M'
HOUR     = HOURS     = 'H'
DAY      = MIDNIGHT  = MIDNIGHTS = 'midnight'

DEFAULT_FORMAT = '[%(asctime)s] %(name)s %(levelname)s: %(message)s'

def every(interval, when):
    return (interval, when)

def _configure_handler(handler, level, format):
    if format is None:
        format = DEFAULT_FORMAT

    formatter = logging.Formatter(format)

    handler.setLevel(level)
    handler.setFormatter(formatter)

    return handler

def create_logger(name=None, level = logging.INFO):
    """
    Returns a logger that does not handle messages. You should add handlers yourself
    """
    if name is None:
        name = get_caller_module(3)
    logger = logging.getLogger(name)
    logger.setLevel(level)
    logger.addHandler(logging.NullHandler())
    return logger

def console(level=logging.INFO, format=None):
    """
    Returns a preconfigured handler that outputs to console
    """
    handler = logging.StreamHandler()
    return _configure_handler(handler, level, format)

def file(path, when=None, level=logging.DEBUG, format=None):
    """
    Returns a preconfigured handler that outputs to a file
    """
    if when is None:
        handler = logging.FileHandler(path)
    else:
        interval, when_type = when
        handler = logging.handlers.TimedRotatingFileHandler(path, when_type, interval)

    return _configure_handler(handler, level, format)

def get_caller_module(level=2):
    '''
    return the name of the module that called the function that called this
    function, by default it assumes that the function that called this function
    was called from another module, to change this set level to the value
    of nested calls
    '''
    return inspect.getmoduleinfo(inspect.stack()[level][1]).name
