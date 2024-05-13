from flask import Flask, request, jsonify
from sqlalchemy.exc import IntegrityError

import getTencentService
import user_operation.login as login
import user_operation.register as register
from models import db, TranslationHistory
import user_operation.history_operation as history

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
    message = register.register(data)
    return message


@app.route('/login', methods=['POST'])
def login():
    data = request.json
    message = login.login(data)
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
    data = request.get_json()
    message = history.merge_history(data)
    return message


@app.route('/translate', methods=['POST'])
def translate():
    data = request.get_json()
    email = data.get('email')
    message = getTencentService.get_tencent_service(data, email)
    return message


if __name__ == '__main__':
    app.run()
