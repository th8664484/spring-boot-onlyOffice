
# Spring-Boot-onlyOffice（集成onlyOffice服务）

springboot集成onlyOffice的实现。在参考网络上的资料结合自身实际情况总结，让集成oo方便快捷。 封装了oo服务的API和统一了配置。
- 在线编辑，查看，转换


* onlyOffice.yml配置文件
* 如果解决了你地方问题，请给个 **star** 
-----------------------------------
# 对外使用的java类 —— OnlyServiceAPI
## 核心方法

<table>
    <thead>
        <tr>
            <th>方法名称</th>
            <th>参数</th>
            <th>参数说明</th>
            <th>返回值</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td rowspan="3">openDocument()</td>
            <td> Map&lt;String,Object&gt; map</td>
            <td> 文件数据信息<br>
                {<br>
                    必填 fileId<br>
                    必填 fileName<br>
                    必填 fileType<br>
                    必填 fileSize<br>
                    可用携带其它值<br>
                }
            </td>
            <td rowspan="3">
                <pre>
{
"editorConfig": {
    "mode": "edit",
    "customization": {
        "feedback": {
            "visible": false
        },
        "help": false,
        "goback": {
            "blank": false
        },
        "macros": false,
        "autosave": false,
        "comments": false,
        "review": {},
        "hideRightMenu": true,
        "anonymous": {
            "request": false
        },
        "forcesave": true,
        "logo": {},
        "hideNotes": true
    },
    "plugins": {
        "pluginsData": [],
        "autostart": []
    },
    "callbackUrl": "http://172.31.240.1:9090/onlyOffice/save",
    "lang": "zh-CN",
    "user": {
        "name": "TongHuic7bba5",
        "id": "c7bba5"
    }
  },
"docServiceApiUrl": "http://172.31.240.1:8886/web-apps/apps/api/documents/api.js",
"documentType": "word",
"document": {
    "permissions": {
        "edit": true,
        "chat": false,
        "review": false
    },
    "title": "fdfs.docx",
    "fileType": "docx",
    "key": "63f560ec03a94654b10cd4fdeebec05a",
    "url": "http://172.31.240.1:9090/download/09cee8767dd3476280fa865bacfaf213",
    "info": {
        "sharingSettings": [{
            "isLink": true,
            "permissions": ["Full Access"],
            "user": "TongHuic7bba5"
        }],
        "created": "2023-08-05 21:38:25"
    }
},
"type": "desktop",
"token": ""
}</pre>
            </td>
        </tr>
        <tr>
            <td>String mode</td>
            <td>打开方式  <br>edit<br>view</td>
        </tr>
        <tr>
            <td>boolean collaborativeEditing</td>
            <td>是否协同编辑</td>
        </tr>
        <tr>
            <td>handlerStatus()</td>
            <td>JSONObject jsonObject</td>
            <td>onlyOffice 回调传来的值。<br>https://api.onlyoffice.com/editors/callback</td>
            <td></td>
        </tr>
        <tr>
            <td rowspan="2">save()：触发保存回调。</td>
            <td>String key</td>
            <td>是openDocument()返回值中的key</td>
            <td rowspan="2">String : 提示信息</td>
        </tr>
        <tr>   
            <td>String userId</td>
            <td>用户Id</td>
        </tr>
        <tr>
            <td rowspan="5">converted()</td>
            <td>String filetype</td>
            <td>文件类型</td>
            <td rowspan="5">转换后的文件下载地址</td>
        </tr>
        <tr>
            <td>String fileId</td>
            <td>文件Id</td>
        </tr>
        <tr>
            <td>String outputtype</td>
            <td>转化类型</td>
        </tr>
        <tr>
            <td>String title</td>
            <td>转换后的文件名称</td>
        </tr>
        <tr>
            <td>String password</td>
            <td>文档密码</td>
        </tr>
    </tbody>
</table>

**注意：**
 * 外部触发保存操作，save()方法。
 * `autosave`/`forcesave` 参数为默认值时有效
 * 修改文件后，没有执行回调方法。在点击保存后执行回调

![](jpg/f.jpg)


快速使用
-----------------------------------

- 必须的依赖

``` pom
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>2.0.25</version>
    </dependency>
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
        <version>5.8.16</version>
    </dependency>
    <dependency>
        <groupId>com.inversoft</groupId>
        <artifactId>prime-jwt</artifactId>
        <version>1.3.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-pool2</artifactId>
    </dependency>
``` 

- 定义关键接口（oo服务回调使用） `重要`
  
    1.保存接口
    ```
        参考：demo/controller/IndexController.saveFile()
        对应配置文件参数：oo.call-back-url
        回调状态
          0 - 找不到具有密钥标识符的文档
          1 - 正在编辑文档（打开文件回调）
          2 - 文档已准备好保存
          3 - 发生文档保存错误
          4 - 不作任何更改就关闭文档（关闭回调）
          6 - 正在编辑文档，但保存当前文档状态（保存回调）
          7 - 强制保存文档时发生错误
    ```

    2.下载文件地址
    ```
        参考：demo/controller/IndexController.download()
        对应配置文件参数：oo.download-file
        对应openDocument()方法返回值中的 document.url
        打开文件后oo服务要下载对应的文件
    ```
- 实现SaveFileProcessor类
    ```
    参考：demo.service.DemoService
    ```
    <table>
        <thead>
            <tr>   
                <th> 方法名称 </th>
                <th> 方法说明 </th>
                <th> 参数 </th>
                <th> 参数说明 </th>
                <th> 返回值 </th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td rowspan="3">saveBeforeInitialization()</td>
                <td rowspan="3">保存前置方法</td>
                <td>Map&lt;String, Object&gt; map</td>
                <td>在openDocument()方法中传入的文件数据信息</td>
                <td rowspan="3"></td>
            </tr>
            <tr>
                <td>byte[] bytes</td>
                <td>文件二进制数据</td>
            </tr>
            <tr>
                <td>String fileExtension</td>
                <td>文件后缀</td>
            </tr>
            <tr>
                <td rowspan="4">save()</td>
                <td rowspan="4">保存方法，实现自己的保存逻辑</td>
                <td>Map&lt;String, Object&gt; map</td>
                <td>在openDocument()方法中传入的文件数据信息</td>
                <td rowspan="4">Map&lt;String, Object&gt; 更新内存中的信息</td>
            </tr>
            <tr>
                <td>byte[] bytes</td>
                <td>文件二进制数据</td>
            </tr>
            <tr>
                <td>byte[] changes</td>
                <td>当前文件和保存之前文件的区别。changes.zip</td>
            </tr>
            <tr>
                <td>String key</td>
                <td>是openDocument()返回值中的key</td>
            </tr>
            <tr>
                <td rowspan="3">saveAfterInitialization()</td>
                <td rowspan="3">保存后置方法</td>
                <td>Map&lt;String, Object&gt; map</td>
                <td>在openDocument()方法中传入的文件数据信息</td>
                <td rowspan="3"></td>
            </tr>
            <tr>
                <td>byte[] bytes</td>
                <td>文件二进制数据</td>
            </tr>
            <tr>
                <td>String fileExtension</td>
                <td>文件后缀</td>
            </tr>
        </tbody>
    </table>


## 更新记录

- [x] 修复关闭文件后快速再次打开该文件后，保存时报错的bug 2023/11/10
- [x] 修复内存文件信息在保存后没有修改的bug 2023/10/23
- [x] 实现和编辑器的数据通信 2023/9/9 (更新链接)
  https://blog.xenosp.cn/posts/87468caf/
- [x] 修复文件转pdf时只有第一页的bug 2023/8/13







-----------------------------------


# 页面集成
### html
```
参考 onlyOffice.html
style.js 中的openDocument()
```
### vue
```
参考 onlyOffice.vue
```
-----------------------------------

![](jpg/QQ截图20230805221213.jpg)
### 编辑

![](jpg/edit.jpg)
![](jpg/editxlsx.jpg)
![](jpg/editppt.jpg)
### 查看

![](jpg/view.jpg)
![](jpg/viewxls.jpg)
![](jpg/viewppt.jpg)
### 文件转换
![](jpg/docx-pdf.jpg)
![](jpg/xlsx2pdf.jpg)












