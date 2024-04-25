from flask import Flask, request
import hashlib
import hmac
import json
import time
from datetime import datetime
from http.client import HTTPSConnection
from flask_sqlalchemy import SQLAlchemy
from flask_login import UserMixin

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:123@localhost/EchoLingua'
db = SQLAlchemy(app)


class User(UserMixin, db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(100), unique=True, nullable=False)
    email = db.Column(db.String(100), unique=True, nullable=False)
    password = db.Column(db.String(100), nullable=False)
    # 历史记录对应的表
    translation_history = db.relationship('TranslationHistory', backref='user', lazy=True)


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
