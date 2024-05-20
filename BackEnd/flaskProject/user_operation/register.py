from flask import jsonify
from get_service.mysql_database import User
from get_service import mysql_database


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
    user_id = mysql_database.create_user(email, password)
    return jsonify({'message': 'User registered successfully', 'user_id': user_id})