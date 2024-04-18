from flask import Flask, request
import requests
import json

app = Flask(__name__)


@app.route('/')
def hello_world():
    # 默认返回内容
    return_dict = {'return_code': '5004', 'return_info': '请求参数为空', 'result': False}
    # 判断传入的json数据是否为空
    if request.get_data() is None:
        return json.dumps(return_dict, ensure_ascii=False)
    # 获取传入的参数
    get_data = request.get_data()
    # 传入的参数为bytes类型，需要转化成json
    get_data = json.loads(get_data)
    if 'SourceText' not in get_data:
        return json.dumps(return_dict, ensure_ascii=False)
    if 'Target' not in get_data:
        return json.dumps(return_dict, ensure_ascii=False)

    url = "tmt.tencentcloudapi.com"
    params = {'Action': 'TextTranslate', 'Version': '2018-03-21', 'Region': 'ap-chongqing',
              'SourceText': get_data['source_text'], 'Source': 'auto', 'Target': get_data['target'],
              'Projected': '0'}


if __name__ == '__main__':
    app.run()
