# Search Adserver
本项目为搜索广告系统中的AdServer模块

## 搜索广告基本业务流程
![process](https://github.com/dwt0317/Search_Adserver/blob/master/img_folder/process.png)

## 系统架构
![architecture](https://github.com/dwt0317/Search_Adserver/blob/master/img_folder/architecture.png)

## 工程架构
![framework](https://github.com/dwt0317/Search_Adserver/blob/master/img_folder/framework.png)

* 灰色部分为待完成部分。

## 模块介绍  

### 查询改写
  * 在搜索广告中，用户的搜索请求是多种多样，然而广告主所购买的关键词是有局限性的。如果按照搜索关键词与广告主购买关键词完全的一致的方式去检索广告，会造成广告召回数量的不足，影响搜索引擎与广告主的收益。因此，搜索引擎需求将用户的搜索请求进行改写，可以改写为语义相近的关键词，或者是直接改写为广告相关的属性，如ID，借此增加广告召回。
  * 本系统中的查询改写属于Broad match的范畴，采用的初步方案是利用word embedding进行语义相关的改写。
  * 当服务器接受到用户的搜索请求后，首先使用jieba分词对于请求分词。然后将切开的词交给word2vec模型寻找近义词。
  * 模块使用搜狗实验室公开的新闻资料作为语料库，训练word2vec模型。

### 广告检索
  * 广告检索目前的实现较为简单。模块使用Elasticsearch。在初次构建索引时，首先在数据库中检索所有广告并封装为JSON的形式。然后以JSON结果为数据源，通过设置对keyword的mapping,将每个keyword识别为一个词，构建倒排索引。


### 广告排序
### 日志收集
  * 日志收集采用了业界比较常用的Flume+Hadoop。搜索、投放、点击日志按天存储到hdfs中。
