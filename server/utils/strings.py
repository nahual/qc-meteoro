import unicodedata

def normalize(words):
    return unicodedata.normalize('NFKD', words.decode("UTF-8")).encode('ascii', 'ignore')
