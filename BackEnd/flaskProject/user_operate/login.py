from flask import jsonify

from models import User


def login(data):
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
