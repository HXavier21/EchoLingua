from flask import jsonify
from models import db, User, TranslationHistory
import models


def register(data):
    email = data['email']
    password = data['password']
    if not email or not password:
        return jsonify({'message': 'Missing data'}), 400

    # 检查是否存在对应的用户邮箱
    existing_user = User.query.filter_by(email=email).first()
    if existing_user:
        return jsonify({'message': 'Email already registered'}), 400
    # 创建新用户并保存到数据库
    user_id = models.create_user(email, password)
    return jsonify({'message': 'User registered successfully', 'user_id': user_id})