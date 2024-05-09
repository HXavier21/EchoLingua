from flask import Flask, request, jsonify
from sqlalchemy.exc import IntegrityError

import getTencentService
import models
import user_operate.register,user_operate.login
from models import db, User, TranslationHistory

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
    message = user_operate.register.register(data)
    return message


@app.route('/login', methods=['POST'])
def login():
    data = request.json
    message = user_operate.login.login(data)
    return message


@app.route('/get_user_history', methods=['GET'])
def get_user_history():
    email = request.args.get('email')  # 获取用户邮箱
    if not email:
        return jsonify({'error': 'Email is required'}), 400

    # 查询用户
    user = User.query.filter_by(email=email).first()
    if not user:
        return jsonify({'error': 'User not found'}), 404

    # 查询用户的历史记录
    user_history = TranslationHistory.query.filter_by(user_id=user.id).all()

    # 将历史记录序列化为字典列表
    serialized_history = []
    for record in user_history:
        serialized_record = {
            'source_text': record.source_text,
            'target_text': record.target_text,
            'source_language': record.source_language,
            'target_language': record.target_language,
            'timestamp': record.timestamp.strftime('%Y-%m-%d %H:%M:%S')
        }
        serialized_history.append(serialized_record)

    return jsonify(serialized_history)


# API 路由，接收客户端序列化的本地历史记录列表并合并到数据库
@app.route('/merge_history', methods=['POST'])
def merge_history():
    data = request.get_json()
    serialized_history_from_client = data.get('history', [])
    if not serialized_history_from_client:
        return jsonify({'error': 'No history data provided'}), 400

    success_records = []
    error_records = []

    # 遍历客户端序列化的历史记录列表
    for record_list in serialized_history_from_client:
        if not isinstance(record_list, list):
            error_records.append({'error': 'Invalid history data format'})
            continue

        for record_dict in record_list:
            record = TranslationHistory(
                source_text=record_dict.get('source_text', ''),
                target_text=record_dict.get('target_text', ''),
                user_id=record_dict.get('user_id', 0)
            )
            try:
                db.session.add(record)
                db.session.commit()
                success_records.append(record_dict)
            except IntegrityError:
                db.session.rollback()
                error_records.append(record_dict)

    return jsonify({
        'success': success_records,
        'error': error_records
    })


@app.route('/translate', methods=['POST'])
def translate():
    data = request.get_json()
    email = data.get('email')
    message = getTencentService.get_tencent_service(data, email)
    return message


if __name__ == '__main__':
    app.run()
