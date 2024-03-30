<template>
  <div class="report-onlyoffice">
    <div id="editorDiv" style="z-index: -1"></div>
  </div>
</template>

<script>

import { Loading } from "element-ui";

import {
  download,
  openDocument,
  getFileInfo,
  getSensitiveWord,
} from '@/components/onlyoffice/onlyoffice'
import Base64 from "@/utils/base64";
import { convertedPdf } from "@/components/fileList/fileList";


export default {
  components: {
  },
  data() {
    return {
      docServiceApiUrl: null,
      id: null,
      token: null,
      documentType: null,
      document: null,
      editorConfig: null,
      fileExt: null,
      fileName: '',
      docEditor: {},
      platform: 'desktop',
      fileSaveFlag: '',
    }
  },
  computed: {},
  create() {
    console.log("页面初始化")
  },
  mounted() {

  },
  beforeDestroy() {

  },
  methods: {

    /**
     * 初始化文件页面信息
     */
    init() {
      let params = this.$route.query.params;
      this.id = params.id;
      this.judgePlatform();
      openDocument(this.id, { mode: 'edit', c: 1 }).then(res => {
        if (res.code === 200) {
          this.docServiceApiUrl = res.result.docServiceApiUrl;
          this.token = res.result.token;
          this.documentType = res.result.documentType;
          this.document = res.result.document;
          this.editorConfig = res.result.editorConfig;
          this.fileExt = res.result.document.fileType;
          this.fileName = this.document.title;

          let script = document.createElement('script');
          script.type = 'text/javascript';
          script.src = this.docServiceApiUrl;
          document.getElementsByTagName('head')[0].appendChild(script);
          this.setEditor()
        } else {
          this.$messagePrompt('error', res.message)
        }
      })
    },
    async setEditor() {
      let config = {
        width: '100%',
        height: '100%',
        type: this.platform,
        token: this.token,
        documentType: this.documentType,
        document: this.document,
        editorConfig: this.editorConfig,
        events: {
          "onAppReady": this.onReady,//-将应用程序加载到浏览器时调用的函数。
          "onDocumentStateChange": this.onDocumentStateChange,//文档改变后的回调
          "onError": this.onError,//发生错误或其他一些特定事件。
          "onOutdatedVersion": this.onOutdatedVersion,
          // "onRequestSaveAs": this.onRequestSaveAs,
          "onRequestClose": this.onRequestClose,//当必须结束编辑器的工作并且必须关闭编辑器时调用的函数。
          // "onDownloadAs": this.onDownloadAs
        }
      }
      //console.log(`setEditor ==> config:`, config)
      if (this.docEditor.length > 0) {
        this.docEditor.destroyEditor();
      }
      let that = this
      this.$nextTick(() => {
        setTimeout(function () {
          that.docEditor = new DocsAPI.DocEditor('editorDiv', config)
        }, 1500);
      })
    },

    judgePlatform() {
      var platform = 'desktop'// 浏览平台
      if (/AppleWebKit.*Mobile/i.test(navigator.userAgent) || (/MIDP|SymbianOS|NOKIA|SAMSUNG|LG|NEC|TCL|Alcatel|BIRD|DBTEL|Dopod|PHILIPS|HAIER|LENOVO|MOT-|Nokia|SonyEricsson|SIE-|Amoi|ZTE/.test(navigator.userAgent))) {
        if (window.location.href.indexOf('?mobile') < 0) {
          try {
            if (/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
              platform = 'mobile'
            }
          } catch (e) {
            console.log(e)
          }
        }
      }
      this.platform = platform
    },
    /**
     * 文档改变后的回调 onDocumentStateChange:{"target":{"frameOrigin":"http://192.168.31.201:9001"},"data":true}
     * @param event
     */
    onDocumentStateChange: function (event) {
      const title = document.title.replace(/\*$/g, "");
      document.title = title + (event.data ? "*" : "");
      this.documentChangeFlag = event.data;
    },
    onReady: function () {
      console.log("文件加载完成....")
    },
    /**
     * 发生错误或其他一些特定事件。
     * @param event
     */
    onError: function (event) {
      if (event) this.$messagePrompt('error', event.data);
    },
    /**
     * 显示错误后调用的函数，当使用旧文档打开文档进行编辑时.key值，该值用于编辑以前的文档版本并已成功保存。
     * 调用此事件时，必须使用新文档重新初始化编辑器.key。
     * @param event
     */
    onOutdatedVersion: function (event) {
      location.reload(true);
    },
    onRequestClose: function () {
      console.log("编辑器关闭");
    },

    load(text, e) {
      this.loading = Loading.service({
        target: e,
        lock: true,
        text: text,
        background: 'rgba(0,0,0,0.65)',
        customClass: 'custom-loading'
      })
    },

  },
  watch: {
    $route: {
      handler(n) {
        this.init()
      },
      immediate: true,
      deep: true,
    }
  }
}

</script>

<style lang="less" scoped>

/deep/iframe {
  min-height: 800px;
}

.report-onlyoffice {
  width: 100%;
  min-width: 1366px;
  position: relative;
  overflow-x: hidden;
  // overflow-y: hidden;
}

</style>
