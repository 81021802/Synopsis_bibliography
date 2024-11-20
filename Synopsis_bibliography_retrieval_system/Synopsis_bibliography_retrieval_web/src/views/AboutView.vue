<template>
  <div class="about" >
    <h1 class="title">提要式书目检索系统</h1>
    <p class="description">
      提要目录是古典目录体制下的一种，也称解题、题识、读书志等。传统古籍书目仅著录一书的题名、卷次、著者和版本等基本信息，提要则尽可能详细地将全书内涵、作者生平、学术源流、个人评论、书籍流传等信息全部钩稽出来， 这对学者快速定位所需古籍、了解古籍内容概要和获取线索大有裨益，是古代目录学“辨章学术，考镜源流”的重要体现。
      本项目依照全面性、权威性、版权保护、版本选择四大原则，选取了47本提要书目进行数字化和字段信息抽取，构建起包含题名、卷数、部类、版本、责任者等24个字段，共计59,624条记录的提要目录数据集。并且基于微调后的GujiBERT模型对提要文本进行实体抽取，得到1,669,117条提要实体记录。
      同时部署上线了提要目录检索系统，提供提要全文的一站式可视化检索和数据集下载。
    </p>
    <a href="https://github.com/81021802/Synopsis_bibliography/" class="button" target="_blank">访问项目</a>
  </div>
</template>

<style scoped>
.about {
  max-width: 800px;
  margin: 60px auto;
  padding: 40px 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.title {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.description {
  font-size: 16px;
  line-height: 1.8;
  color: #666;
  text-align: justify;
  margin-bottom: 30px;
}

.button {
  display: inline-block;
  padding: 12px 24px;
  font-size: 16px;
  color: #fff;
  background-color: #409eff;
  border: none;
  border-radius: 5px;
  text-decoration: none;
  transition: background-color 0.3s;
}

.button:hover {
  background-color: #337ab7;
}
</style>