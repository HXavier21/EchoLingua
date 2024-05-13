import json

from flask import jsonify
from sqlalchemy.exc import IntegrityError

from models import db, User, TranslationHistory
import models


def get_user_history(email):
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
            'id': record.id,
            'source_text': record.source_text,
            'target_text': record.target_text,
            'source_language': record.source_language,
            'target_language': record.target_language,
            'timestamp': record.timestamp.strftime('%Y-%m-%d %H:%M:%S')
        }
        serialized_history.append(serialized_record)
    result = {
        'history': serialized_history
    }
    return result,200


def merge_history(data):
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