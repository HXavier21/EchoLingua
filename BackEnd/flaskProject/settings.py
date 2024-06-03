import os
# please downloads ffmpeg and
os.environ['PATH'] += os.pathsep + 'D:\\code\\SoftwareEngineering\\TTS\\GPT-SoVITS-beta0217\\GPT-SoVITS-beta0217fix\\ffmpeg.exe'
all_model_name = ['Asta', 'Keqing', 'March7th', 'Palm', 'Wanderer', 'Yuuka', 'Zhongli']
mysql_path = 'mysql://root:123@localhost/EchoLingua'
localhost = 'http://127.0.0.1:'
json_path = "./get_service/models_information.json"
download_pretrained_model_path = '.\\GPT_SoVITS'
bert_path = 'D:\\code\\SoftwareEngineering\\EchoLingua\\BackEnd\\flaskProject\\get_service\\pretrained_models\\chinese-roberta-wwm-ext-large'
base_path = 'D:\\code\\SoftwareEngineering\\EchoLingua\\BackEnd\\flaskProject\\get_service\\pretrained_models\\chinese-hubert-base'