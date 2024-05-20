import requests
from flask import Flask, request, jsonify, Response

import get_service.tts_service as tts
from get_service import get_tencent_service
import user_operation.history_operation as history
import user_operation.login as lo
import user_operation.register as re
from get_service.mysql_database import db

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:123@localhost/EchoLingua'
db.init_app(app)


@app.route('/')
def hello():
    return "EchoLingua,gogogo!!!"


@app.route('/0.o')
def vivo50():
    return "hinanawi tenshi，vivo50"


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
@app.route('/merge_history', methods=['POST'])
def merge_history():
    email = request.args.get('email')
    data = request.get_json()
    message = history.merge_history(email, data)
    return message


@app.route('/translate', methods=['POST'])
def translate():
    data = request.get_json()
    email = data.get('email')
    message = get_tencent_service.get_tencent_service(data, email)
    return message


@app.route('/get_tts_service', methods=['GET', 'POST'])
def get_tts_service():
    if request.method == 'GET':
        params = request.args.to_dict()
        url = tts.get_url(params.get('models'))
        response = requests.get(url=url, params=params)
        return Response(response, mimetype="audio/wav")
    if request.method == 'POST':
        json_data = request.get_json()
        url = tts.get_url(json_data['models'])
        response = requests.post(url=url, data=json_data)
        return Response(response, mimetype="audio/wav")


if __name__ == '__main__':
    app.run()
