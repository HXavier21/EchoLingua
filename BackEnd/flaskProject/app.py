import json
import os
import subprocess
import sys
import requests
from flask import Flask, request, jsonify, Response
from starlette.responses import StreamingResponse

from get_service import get_tencent_service
import user_operation.history_operation as history
import user_operation.login as lo
import user_operation.register as re
from get_service.mysql_database import db

os.environ[
    'PATH'] += os.pathsep + 'D:\\code\\SoftwareEngineering\\TTS\\GPT-SoVITS-beta0217\\GPT-SoVITS-beta0217fix\\ffmpeg.exe'
root_dir = os.getcwd()
sys.path.append(root_dir)
sys.path.append(".\\get_service\\")
sys.path.append(".\\get_service\\GPT_SoVITS\\")

model_name = ['Asta', "Wanderer"]
model_port = {}
port = 9880
for name in model_name:
    model_port[name] = port
    port += 10

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:123@localhost/EchoLingua'
db.init_app(app)


def start_tts(model):
    with open("./get_service/models_information.json", "r", encoding="utf-8") as f:
        models_information = json.load(f)
        model_information = models_information.get(model)
        if model_information is None:
            raise ValueError(f"No information found for model: {model}")

    sovits_path = model_information["sovits_path"]
    gpt_path = model_information["gpt_path"]
    refer_wav_path = model_information["refer_wav_path"]
    prompt_text = model_information["prompt_text"]
    prompt_language = model_information["prompt_language"]
    command = f'python.exe D:/code/SoftwareEngineering/EchoLingua/BackEnd/flaskProject/get_service/tts_api.py -s {sovits_path} -g {gpt_path} -dr {refer_wav_path} -dt {prompt_text} -dl {prompt_language} -p {model_port.get(model)}'
    subprocess.Popen(command, shell=True, cwd='./get_service')


for model in model_name:
    start_tts(model)
    print('will start:' + model)


@app.route('/')
def hello():
    return "EchoLingua,gogogo!!!"


@app.route('/register', methods=['POST'])
def register():
    data = request.get_json()
    message = re.register(data)
    return message


@app.route('/login', methods=['POST'])
def login():
    data = request.json
    message = lo.login(data)
    return message


@app.route('/get_user_history', methods=['GET'])
def get_user_history():
    email = request.args.get('email')  # 获取用户邮箱
    if not email:
        return jsonify({'error': 'Email is required'}), 400

    user_history = history.get_user_history(email)
    return jsonify(user_history)


# API 路由，接收客户端序列化的本地历史记录列表并合并到数据库
@app.route('/merge_history', methods=['PUT'])
def merge_history():
    email = request.args.get('email')
    data = request.get_json()
    message = history.merge_history(email, data)
    return message


@app.route('/translate', methods=['POST'])
def translate():
    data = request.get_json()
    email = request.args.get('email')
    message = get_tencent_service.get_tencent_service(data, email)
    response = json.loads(message).get('Response')
    print(response)
    return response['TargetText']


@app.route('/get_tts_service', methods=['GET', 'POST'])
def get_tts_service():
    if request.method == 'GET':
        params = request.args.to_dict()
        url = 'http://127.0.0.1:' + str(model_port.get(params['model']))
        response = requests.get(url=url, params=params)
        return Response(response, mimetype="audio/wav")
    if request.method == 'POST':
        json_data = request.get_json()
        url = 'http://127.0.0.1:' + str(model_port.get(json_data['model']))
        data = json.dumps(json_data)
        response = requests.post(url=url, data=data)
        return Response(response, mimetype="audio/wav")


if __name__ == '__main__':
    app.run()
