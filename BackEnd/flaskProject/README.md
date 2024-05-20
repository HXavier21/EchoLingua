# 可供调用的API
## /register
### 接口描述
POST方法，用于注册的接口，接受唯一的邮箱和密码，成功将返回成功信息
### 请求参数
| 参数名 | 参数类型 | 描述 | 是否必填 |
| - | - | :-: | - |
| email | String | 唯一的邮箱，不可重复 | 是 |
| password| String | 用户密码 | 是 |
### 返回
一个包含了成功或失败信息的json文件，可能失败的原因：重复邮箱
## /login
### 接口描述
POST方法，用于验证登陆信息的接口，接受邮箱和密码
| 参数名 | 参数类型 | 描述 | 是否必填 |
| :-: | :-: | :-: | :-: |
| email | String | 用户邮箱 | 是 |
| password | String | 用户密码 | 是 |
### 返回信息
一个包含了成功或失败信息的json文件  
可能失败的原因：未知邮箱；错误密码
## /translate
### 接口描述
POST方法，接受email字段和翻译请求信息，返回包含翻译内容的json，并保存翻译记录
| 字段 | 字段类型 | 描述 | 是否必填 |
| - | - | - | - |
| email | String | 用户邮箱 | 是 |
  
| 参数名 | 参数类型 | 描述 | 是否必填 |
| - | - | :-: | - |
| SourceText | String | 需要翻译的文本 | 是 |
| Source | String | 源文本的语种，缺省将自动识别为auto | 否 |
| Target | String | 目标文本的语种 | 是 |
### 返回信息
将返回一个包含response信息的json文件，成功返回response中将包含TargetText文本
## /get_tts_service
### 接口描述
GET或者POST方法，接受需求信息并返回音频文件
#### GET方法
| 字段 | 字段类型 | 描述 | 是否必填 |
| - | - | :-: | - |
| models | String | 需要的ai语音模型，如不填则为默认 | 否 |
| text | String | 需要合成的文本 | 是 |
| text_language | String | 需要合成的文本语言类型 | 是 |
#### POST方法
| 参数名 | 参数类型 | 描述 | 是否必填 |
| - | - | :-: | - |
| models | String | 需要的ai语音模型，如不填则为默认 | 否 |
| text | String | 需要合成的文本 | 是  |
|  text_language | String | 需要合成的文本语言类型 | 是  |
### 返回信息
如成功则返回一个wav类型的音频文件，失败则返回失败信息
## /get_user_history
### 接口描述
GET方法，用以获取用户的翻译历史记录
| 字段 | 字段类型 | 描述 | 是否必填 |
| - | - | :-: | - |
| email | String | 需要返回历史记录的用户邮箱 | 是 |
### 返回信息
如成功将返回一个包含‘history’的json文件，可能的失败原因有不存在的用户
## /merge_history
### 接口描述
PUT方法，用以向后端发送客户端的本地翻译历史记录以同步到云端
| 字段 | 字段类型 | 描述 | 是否必填 |
| - | - | :-: | - |
| email | String | 上传本地历史记录的用户 | 是 |
    
| 参数名 | 参数类型 | 描述 | 是否必填 |
| - | - | :-: | - |
| history | json | 用户序列化后的本地历史记录 | 是 |
#### history内容
| 参数名 | 描述 |
| - | :-: |
| source_language | 源文本语言类型 |
| source_text | 源文本 |
| target_language | 目标文本语言类型 |
| target_text | 目标文本 |
| timestamp | 时间戳 |