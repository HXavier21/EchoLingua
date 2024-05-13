import hashlib
import hmac
import json
import time
import numpy as np
from datetime import datetime
from http.client import HTTPSConnection

from flask import jsonify

from models import db, User, TranslationHistory


def sign(key, msg):
    return hmac.new(key, msg.encode("utf-8"), hashlib.sha256).digest()


def get_tencent_service(data, email):
    if not email:
        return jsonify({'error': 'Email is required'}), 400

    # 查询用户
    user = User.query.filter_by(email=email).first()
    if not user:
        return jsonify({'error': 'User not found'}), 404
    with open("D:\code\SoftwareEngineering\secret_id.txt",'r') as file:
        secret_id = file.read()
    with open("D:\code\SoftwareEngineering\secret_key.txt",'r') as file:
        secret_key = file.read()
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
