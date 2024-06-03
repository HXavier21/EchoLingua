"""
route:
    @app.route('/')
    @app.route('/register', methods=['POST'])
    @app.route('/login', methods=['POST'])
    @app.route('/get_user_history', methods=['GET'])
    @app.route('/merge_history', methods=['PUT'])
    @app.route('/translate', methods=['POST'])
    @app.route('/get_tts_service', methods=['GET', 'POST'])
"""


# import
import json
import os
import subprocess
import sys
import time
import requests
from flask import Flask, request, jsonify, Response

# personal import
from settings import mysql_path, localhost, json_path
from get_service import get_tencent_service
import user_operation.history_operation as history
import user_operation.login as lo
import user_operation.register as re
from get_service.mysql_database import db

# set path
parent_pid = os.getpid()
root_dir = os.getcwd()
sys.path.append(root_dir)
sys.path.append(".\\get_service\\")
sys.path.append(".\\get_service\\GPT_SoVITS\\")

# set tts model
model_name = ['Asta', 'Keqing']
model_port = {}
model_pid = {}
port = 9880
for name in model_name:
    model_port[name] = port
    port += 10

# set application
app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = mysql_path

# set the database
db.init_app(app)


def start_tts(model):
    with open(json_path, "r", encoding="utf-8") as f:
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
    p = subprocess.Popen(command, shell=True, cwd='./get_service')
    return p.pid


# start the tts service
for model in model_name:
    pid = start_tts(model)
    time.sleep(20)
    print(pid)
    model_pid[model] = pid
    print('will start:' + model)


# all route
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
        name = request.args.get('model')
        url = localhost + str(model_port.get(name))
        response = requests.get(url=url, params=params)
        return Response(response, mimetype="audio/wav")
    if request.method == 'POST':
        json_data = request.get_json()
        name = json_data.get('model')
        url = localhost + str(model_port.get(name))
        data = json.dumps(json_data)
        response = requests.post(url=url, data=data)
        return Response(response, mimetype="audio/wav")


# main
if __name__ == '__main__':
    if os.getpid() == parent_pid:
        app.run()
