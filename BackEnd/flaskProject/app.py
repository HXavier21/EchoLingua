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

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:123@localhost/EchoLingua'
db = SQLAlchemy(app)


# 以下为用户模型以及对应的历史记录模型
class User(UserMixin, db.Model):
    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(100), unique=True, nullable=False)
    password = db.Column(db.String(100), nullable=False)
    # 历史记录对应的表关系
    translation_history = db.relationship('TranslationHistory', backref='user', lazy=True)


class TranslationHistory(db.Model):
    __tablename__ = 'translation_history_{}'  # 动态表名
    id = db.Column(db.Integer, primary_key=True)
    source_text = db.Column(db.Text, nullable=False)
    target_text = db.Column(db.Text, nullable=False)
    source_language = db.Column(db.String(10), nullable=False)
    target_language = db.Column(db.String(10), nullable=False)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    timestamp = db.Column(db.DateTime, nullable=False, default=datetime.utcnow)


def sign(key, msg):
    return hmac.new(key, msg.encode("utf-8"), hashlib.sha256).digest()

def create_user(email, password):
    user = User(email=email, password=password)
    try:
        # 将用户对象添加到数据库
        db.session.add(user)
        db.session.commit()

        # 创建该用户的历史记录表
        translation_history_table_name = TranslationHistory.__tablename__.format(user.id)
        sql_query = text(f"""
                        CREATE TABLE IF NOT EXISTS {translation_history_table_name} (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            source_text TEXT NOT NULL,
                            target_text TEXT NOT NULL,
                            source_language VARCHAR(10) NOT NULL,
                            target_language VARCHAR(10) NOT NULL,
                            user_id INT NOT NULL,
                            timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (user_id) REFERENCES User(id)
                        );
                    """)
        db.session.execute(sql_query)
        db.session.commit()
        return True, user.id
    except Exception as e:
        db.session.rollback()
        return False, str(e)


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
    success, user_id = create_user(email, password)
    if success:
        return jsonify({'message': 'User registered successfully', 'user_id': user_id})
    else:
        return jsonify({'error': 'User registration failed', 'reason': user_id}), 500


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

@app.route('/translate', methods=['POST'])
def translate():
    data = request.get_json()
    secret_id = "AKIDIUSHtw00tffXTH0b5TvKYZMG1ZvLixBI"
    secret_key = "1tkV9rYGnDXmureGfrvtyymVRL9zpiXE"
    token = ""

    service = "tmt"
    host = "tmt.tencentcloudapi.com"
    region = "ap-chongqing"
    version = "2018-03-21"
    action = "TextTranslate"
    payload = {"SourceText": data["SourceText"],
               "Source": data["Source"],
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

        # 返回结果
        return response_data
    except Exception as err:
        return str(err)


if __name__ == '__main__':
    app.run()
