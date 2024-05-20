from flask import jsonify
from sqlalchemy.exc import IntegrityError

from get_service.mysql_database import db, User, TranslationHistory


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
    return result, 200


def merge_history(email, data):
    user = User.query.filter_by(email=email).first()
    serialized_history_from_client = data.get('history', [])
    if not serialized_history_from_client:
        return jsonify({'error': 'No history data provided'}), 400

    success_records = []
    error_records = []

    # 遍历客户端序列化的历史记录列表
    for record_dict in serialized_history_from_client:
        # 判断是否重复
        is_user_history = TranslationHistory.query.filter_by(source_text=record_dict.get('source_text', ''),
                                                             target_text=record_dict.get('target_text', ''),
                                                             timestamp=record_dict.get('timestamp', '')).first()
        if is_user_history:
            error_records.append(record_dict)
            continue

        record = TranslationHistory(
            user_id=user.id,
            source_language=record_dict.get('source_language', ''),
            source_text=record_dict.get('source_text', ''),
            target_language=record_dict.get('target_language', ''),
            target_text=record_dict.get('target_text', ''),
            timestamp=record_dict.get('timestamp')
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
