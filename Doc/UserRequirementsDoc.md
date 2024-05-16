# 用户需求文档

## 项目名称

 ***EchoLingua***

## 概述

***EchoLingua*** 旨在为用户提供便捷的翻译服务，支持多种输入方式和输出方式，并提供灵活的存储选项。

## 用户目标

- 快速准确地进行文本、语音和图像翻译。
- 轻松切换输入语言和输出语言。
- 便捷地保存和管理翻译结果。

## 用户需求描述

### 功能需求

#### 文本翻译：用户可通过键盘输入待翻译的文本

用户通过输入法将待翻译文本输入在输入框中，在语言选择框中分别选择好输入输出语种后（用户也可以在选择语种时点击旁边的下载模型按钮下载离线翻译模型，下载过程中会有一个进度条，下载完成后会弹窗提醒用户）点击翻译按钮，没有下载对应模型时默认在线翻译，若存在离线模型则自动转换为离线翻译，翻译结果将显示在屏幕下方。

#### 语音输入翻译：支持实时语音输入，用户可直接讲话并实时翻译

用户先在下拉式选择框中选择好输入输出的语种，点击录音按钮后（第一次点击时将请求录音权限）程序开始录音，再次点击录音按钮程序停止录音并开始转录音频，将用户声音转为对应输入语种并显示在屏幕上方，然后翻译组件再进行翻译操作，翻译过程中屏幕下方会有一个持续旋转的进度条，翻译完成后结果将代替进度条显示在屏幕下方。

#### 拍照翻译：用户可通过拍照获取图片，并进行翻译，支持从图库选择照片，或者实时拍照翻译

用户进入拍照翻译界面后，可以点击拍照按钮进行拍照（屏幕上是摄像预览），然后程序将全屏加载刚刚拍摄的照片并进行文本识别，识别后的文本显示在屏幕下方，同时根据用户选择的输入输出语种进行翻译，点击图片上对应的文本可以显示对应的翻译。

用户也可以选择图库中的图片，程序将全屏加载刚刚拍摄的照片并进行文本识别，识别后的文本显示在屏幕下方，同时根据用户选择的输入输出语种进行翻译，点击图片上对应的文本可以显示对应的翻译。

对于实时拍照翻译，程序将频繁获取相机图片并持续进行文本识别与翻译，结果实时与摄像预览整合并显示在屏幕上。

#### 快捷翻译：用户可从其他应用处唤起 ***EchoLingua***，更方便快捷地使用翻译功能。

用户在任何位置长按选中文本后可以在选项栏中选择 EchoLingua，程序将浮现在当前应用上方并翻译用户选中的文本，在此期间用户可以随时更改输入输出语种，也可以在输入框中输入任何文本进行翻译。

#### 语音播放：用户可点击翻译结果，听取翻译后的语音

所有翻译结果旁均有一个喇叭按钮，用户点击后程序将播放翻译结果的语音。

#### 复制功能：用户可复制翻译结果，方便粘贴到其他应用程序中使用

所有翻译结果均支持选中复制，输入框也支持粘贴。

#### 存储功能

用户进入历史记录界面可以看见所有翻译记录，文本、语音和图片翻译结果将有明显区分，用户甚至可以根据类型或内容进行历史记录检索，用户还可以查看、编辑和删除已存储的翻译结果。

#### 反馈与改进

用户可以通过反馈渠道，提出意见和建议。

### 非功能需求

#### 翻译设置

- 选择语言：用户可灵活选择输入语言和输出语言，支持多种常见语言的翻译。
- 翻译方式：用户可选择普通翻译或对话翻译，以满足不同场景下的需求。

#### 用户界面

- 简洁直观的界面设计，便于用户操作。
- 分类清晰的功能模块，包括输入、输出、设置和存储等。
- 提供个性化设置选项，让用户根据自己的喜好进行调整。

#### 安全性

- 保护用户隐私和数据安全，对用户输入的敏感信息进行严格保密。
- 提供账户登录功能，确保用户数据的安全访问和管理。

### 附加功能

- 提供离线翻译服务，确保在无网络环境下也能进行翻译。
- 支持翻译历史记录查看和导出，方便用户追溯和分享翻译历史。

## 应用架构

### 界面层

![在典型架构中，界面层的界面元素依赖于状态容器，而状态容器又依赖于来自数据层或可选网域层的类。](https://developer.android.com/static/topic/libraries/architecture/images/mad-arch-overview-ui.png?hl=zh-cn)

界面层（或呈现层）的作用是在屏幕上显示应用数据。每当数据发生变化时，无论是因为用户互动（例如按了某个按钮），还是因为外部输入（例如网络响应），界面都应随之更新，以反映这些变化。

界面层由以下两部分组成：

- UI层主要负责应用与用户的交互、界面动效的绘制管理、界面UI的更新与跳转等。在 ***EchoLingua*** 中，主要有主翻译界面、对话翻译界面、拍照翻译界面、历史记录界面、星标记录界面、用户界面、应用设置界面等界面与各式各样的组件和动画。
- 状态容器层是以MVVM架构为核心的ViewModel类集合，负责管理界面元素与更新界面元素状态，并担任与网域层或是数据层进行交互的任务。在 ***EchoLingua*** 中，主要有TranslatePageViewModel、AudioTranslatePageViewModel、CameratranslatePageViewModel、UserPageViewModel、SettingPageViewModel等ViewModel继承类以及ModelDownloadStateHolder、LanguageSelectStateHolder等状态容器。

### 网域层（待定）

![如果添加了此层，则该可选网域层会向界面层提供依赖项，而它自身依赖于数据层。](https://developer.android.com/static/topic/libraries/architecture/images/mad-arch-overview-domain.png?hl=zh-cn)

网域层负责封装复杂的业务逻辑，或者由多个 ViewModel 重复使用的简单业务逻辑。在 ***EchoLingua*** 中，网域层主要是负责整合和序列化数据库数据的DataProcess类。

### 数据层

![在典型架构中，数据层的仓库会向应用的其余部分提供数据，而这些仓库则依赖于数据源。](https://developer.android.com/static/topic/libraries/architecture/images/mad-arch-overview-data.png?hl=zh-cn)

应用的数据层包含*业务逻辑*。业务逻辑决定应用的价值，它包含决定应用如何创建、存储和更改数据的规则。

数据层由多个仓库组成，其中每个仓库都可以包含零到多个数据源。应用中处理的每种不同类型的数据都需要分别创建一个存储库类。 在 ***EchoLingua*** 中，主要有本地历史记录ROOM数据库和远端服务器数据库。

存储库类负责以下任务：

- 向应用的其余部分公开数据。
- 集中处理数据变化。
- 解决多个数据源之间的冲突。
- 对应用其余部分的数据源进行抽象化处理。
- 包含业务逻辑。

## 接口设计

### 文本翻译接口

#### Translator

```kotlin
fun translateWithAutoDetect(text: String, onSuccessCallback: (String) -> Unit = {})
```

### 拍照识别接口

#### Text Recognizer

```kotlin
fun processImage(imageFile: Uri, language: String, refreshRecognizedText: (Text) -> Unit = {})
```

### 语音识别接口

#### LibWhisper

```kotlin
suspend fun transcribeData(
    data: FloatArray,
    progressCallback: (Int) -> Unit = { }
): String
external fun initContext(modelPath: String): Long
external fun freeContext(contextPtr: Long)
external fun fullTranscribe(
	contextPtr: Long,
	language: String,
	progressCallback: ProgressCallback? = null,
	audioData: FloatArray
)
external fun getTextSegmentCount(contextPtr: Long): Int
external fun getTextSegment(contextPtr: Long, index: Int): String
```

### 数据库接口

#### DataRepository

```kotlin
fun insert(translateHistoryItem: TranslateHistoryItem): Long = dao.insert(translateHistoryItem)
fun getAll(): List<TranslateHistoryItem> = dao.getAll()
fun checkIfTranslated(
    sourceLanguage: String,
    targetLanguage: String,
    sourceText: String,
    targetText: String
): List<TranslateHistoryItem> =
    dao.checkIfTranslated(sourceLanguage, targetLanguage, sourceText, targetText)
fun update(translateHistoryItem: TranslateHistoryItem) = dao.update(translateHistoryItem)
fun delete(translateHistoryItem: TranslateHistoryItem) = dao.delete(translateHistoryItem)
```

### 音频处理接口

#### FFmpegUtil

```kotlin
fun audioToWav(inputPath: String, outputPath: String)
```
