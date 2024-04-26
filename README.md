
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">office-spring-boot-starter</h1>
<h4 align="center">一个Office文档集成工具</h4>


---

## 前言： <!-- {docsify-ignore} -->
- [在线文档：https://www.xenosp.cn/docs/](https://www.xenosp.cn/docs/)

- 注：该工具和业务紧密相关。需要自己对接工具

-  如果解决了你的问题，点个 star 鼓励一下吧！
-  [office-spring-boot-starter 源码](https://gitee.com/th8664484/office-spring-boot-starter) 


> office-spring-boot-starter 介绍 <!-- {docsify-ignore} -->

快速集成onlyOffice文档服务与wps文档服务

-----------------------------------

## 在 SpringBoot 环境集成

### 添加依赖
> 在项目中添加依赖：
```xml
<dependency>
    <groupId>cn.xenosp</groupId>
    <artifactId>office-spring-boot-starter</artifactId>
    <version>1.0.6</version>
</dependency>
```

### 设置配置文件
> 在 `application.yml` 中增加如下配置
```yaml
office:
  type:              # wps / oo:onlyoffice
  dowload-file:      #http://ip:prot(域名)/xxxx/download/{id}，{id}在程序中动态拼接上的
  localhost-address: #本应用的地址
  hist-num:          #历史文件数量超出 则进行删除 有需要则填写
  max-size:          #单位 MB 限制单个文件打开时的大小 默认20MB
  timeout: 
  wps:
    ak: 
    sk: 
    domain-name:     #wps中台地址
    secret-key:      #密钥
  oo:
    secret:        #onlyoffice服务 开启了jwt校验 填写
    doc-service:   #onlyoffice服务的地址
    call-back-url: # oo回调接口
```

!> **wps** 和 **oo （不要忘了 onlyOffice.yml文件）** 配置二选一即可 

## 更新记录

- [x] 添加缓存自定义实现 2024/04/26
- [x] 修复报错，优化代码 2024/04/11
- [x] wps对接完成 2024/03/30
- [x] 优化为spring-boot-starter调用，并发布 2024/03/01
- [x] 修复关闭文件后快速再次打开该文件后，保存时报错的bug 2023/11/10
- [x] 修复内存文件信息在保存后没有修改的bug 2023/10/23
- [x] [实现和编辑器的数据通信 2023/9/9 (更新链接)](https://blog.xenosp.cn/posts/87468caf/)
- [x] 修复文件转pdf时只有第一页的bug 2023/8/13