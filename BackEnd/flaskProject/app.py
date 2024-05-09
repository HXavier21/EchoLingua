from flask import Flask, request, jsonify
import hashlib
import hmac
import json
import time
from datetime import datetime
from http.client import HTTPSConnection
from flask_sqlalchemy import SQLAlchemy
from flask_login import UserMixin
from sqlalchemy import text
from sqlalchemy.exc import IntegrityError

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:123@localhost/EchoLingua'
db = SQLAlchemy(app)


# 以下为用户模型以及对应的历史记录模型
# 用户模型
class User(UserMixin, db.Model):
    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(100), unique=True, nullable=False)
    password = db.Column(db.String(100), nullable=False)
    translation_history = db.relationship('TranslationHistory', backref='user', lazy=True)


# 历史记录模型
class TranslationHistory(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    source_text = db.Column(db.Text, nullable=False)
    target_text = db.Column(db.Text, nullable=False)
    source_language = db.Column(db.String(10), nullable=False)
    target_language = db.Column(db.String(10), nullable=False)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    timestamp = db.Column(db.DateTime, nullable=False, default=datetime.utcnow)


def sign(key, msg):
    return hmac.new(key, msg.encode("utf-8"), hashlib.sha256).digest()


@app.route('/')
def hello():
    return "EchoLingua,gogogo!!!"


@app.route('/0.o')
def vivo50():
    return "hinanawi tenshi，vivo50"


@app.route('/register', methods=['POST'])
def register():
    data = request.get_json()
    email = data['email']
    password = data['password']
    if not email or not password:
        return jsonify({'message': 'Missing data'}), 400

    # 检查是否存在对应的用户邮箱
    existing_user = User.query.filter_by(email=email).first()
    if existing_user:
        return jsonify({'message': 'Email already registered'}), 400

    # 创建新用户并保存到数据库
    user = User(email=email, password=password)
    db.session.add(user)
    db.session.commit()

    return jsonify({'message': 'User registered successfully', 'user_id': user.id})


@app.route('/login', methods=['POST'])
def login():
    data = request.json
    email = data.get('email')
    password = data.get('password')

    # 查询用户
    user = User.query.filter_by(email=email).first()

    if user:
        # 检查密码是否匹配
        if user.password == password:
            return jsonify({'message': 'Login successful'})
        else:
            return jsonify({'error': 'Invalid password'}), 401
    else:
        return jsonify({'error': 'User not found'}), 404


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
    if not email:
        return jsonify({'error': 'Email is required'}), 400

    # 查询用户
    user = User.query.filter_by(email=email).first()
    if not user:
        return jsonify({'error': 'User not found'}), 404
    secret_id = "AKIDIUSHtw00tffXTH0b5TvKYZMG1ZvLixBI"
    secret_key = "1tkV9rYGnDXmureGfrvtyymVRL9zpiXE"
    token = ""

    service = "tmt"
    host = "tmt.tencentcloudapi.com"
    region = "ap-chongqing"
    version = "2018-03-21"
    action = "TextTranslate"
    source = data["Source"]
    if source == '':
        source = 'auto'
    payload = {"SourceText": data["SourceText"],
               "Source": source,
               "Target": data["Target"],
               "ProjectId": 0}
    params = json.dumps(payload)
    endpoint = "https://tmt.tencentcloudapi.com"
    algorithm = "TC3-HMAC-SHA256"
    timestamp = int(time.time())
    date = datetime.utcfromtimestamp(timestamp).strftime("%Y-%m-%d")

    # ************* 步骤 1：拼接规范请求串 *************
    http_request_method = "POST"
    canonical_uri = "/"
    canonical_querystring = ""
    ct = "application/json; charset=utf-8"
    canonical_headers = "content-type:%s\nhost:%s\nx-tc-action:%s\n" % (ct, host, action.lower())
    signed_headers = "content-type;host;x-tc-action"
    hashed_request_payload = hashlib.sha256(params.encode("utf-8")).hexdigest()
    canonical_request = (http_request_method + "\n" +
                         canonical_uri + "\n" +
                         canonical_querystring + "\n" +
                         canonical_headers + "\n" +
                         signed_headers + "\n" +
                         hashed_request_payload)

    # ************* 步骤 2：拼接待签名字符串 *************
    credential_scope = date + "/" + service + "/" + "tc3_request"
    hashed_canonical_request = hashlib.sha256(canonical_request.encode("utf-8")).hexdigest()
    string_to_sign = (algorithm + "\n" +
                      str(timestamp) + "\n" +
                      credential_scope + "\n" +
                      hashed_canonical_request)

    # ************* 步骤 3：计算签名 *************
    secret_date = sign(("TC3" + secret_key).encode("utf-8"), date)
    secret_service = sign(secret_date, service)
    secret_signing = sign(secret_service, "tc3_request")
    signature = hmac.new(secret_signing, string_to_sign.encode("utf-8"), hashlib.sha256).hexdigest()

    # ************* 步骤 4：拼接 Authorization *************
    authorization = (algorithm + " " +
                     "Credential=" + secret_id + "/" + credential_scope + ", " +
                     "SignedHeaders=" + signed_headers + ", " +
                     "Signature=" + signature)

    # ************* 步骤 5：构造并发起请求 *************
    headers = {
        "Authorization": authorization,
        "Content-Type": "application/json; charset=utf-8",
        "Host": host,
        "X-TC-Action": action,
        "X-TC-Timestamp": timestamp,
        "X-TC-Version": version
    }
    if region:
        headers["X-TC-Region"] = region
    if token:
        headers["X-TC-Token"] = token

    try:
        req = HTTPSConnection(host)
        req.request("POST", "/", headers=headers, body=params.encode("utf-8"))
        resp = req.getresponse()
        response_data = resp.read().decode("utf-8")
        json_data = json.loads(response_data)["Response"]
        error_message = json_data.get("Error")
        if error_message:
            return jsonify({'error': 'Translation service error ' + error_message["Message"]}), 500

        # 提取翻译后的目标文本
        translated_text = json_data.get("TargetText", "")

        # 获取当前时间作为历史记录的时间戳
        current_timestamp = datetime.utcnow()

        # 将翻译后的文本添加到历史记录中
        history_entry = TranslationHistory(
            user_id=user.id,
            source_text=data["SourceText"],
            target_text=translated_text,
            source_language=source,
            target_language=data["Target"],
            timestamp=current_timestamp
        )

        # 将历史记录添加到用户对应的历史记录表中
        db.session.add(history_entry)
        db.session.commit()

        # 返回翻译后的目标文本
        return response_data
    except Exception as err:
        return str(err)


if __name__ == '__main__':
    app.run()
