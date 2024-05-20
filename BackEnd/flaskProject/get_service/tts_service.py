import os

Asta = "D:/code/SoftwareEngineering/EchoLingua/BackEnd/flaskProject/tts_inference/Asta.bat"
Keqing = "D:/code/SoftwareEngineering/EchoLingua/BackEnd/flaskProject/tts_inference/Keqing.bat"
March7th = "D:/code/SoftwareEngineering/EchoLingua/BackEnd/flaskProject/tts_inference/Mrach7th.bat"
Wanderer = "D:/code/SoftwareEngineering/EchoLingua/BackEnd/flaskProject/tts_inference/Wanderer.bat"
Yuuka = "D:/code/SoftwareEngineering/EchoLingua/BackEnd/flaskProject/tts_inference/Yuuka.bat"


def start_tts_service():
    os.system(Asta)
    os.system(Keqing)
    os.system(March7th)
    os.system(Wanderer)
    os.system(Yuuka)
    return


def get_url(models):
    if models == 'Asta':
        return 'http://127.0.0.1:9880'
    if models == 'Keqing':
        return 'http://127.0.0.1:9890'
    if models == 'Mrach7th':
        return 'http://127.0.0.1:9900'
    if models == 'Wanderer':
        return 'http://127.0.0.1:9910'
    if models == 'Yuuka':
        return 'http://127.0.0.1:9920'
    if models == 'Palm':
        return 'http://127.0.0.1:9930'
    if models == 'Zhongli':
        return 'http://127.0.0.1:9940'
    else:
        return 'http://127.0.0.1:9880'
