package com.oo.onlyoffice.dto.convert;

import cn.hutool.json.JSONObject;
import com.oo.onlyoffice.dto.convert.document.DocumentLayout;
import com.oo.onlyoffice.dto.convert.document.DocumentRenderer;
import com.oo.onlyoffice.dto.convert.sheet.SpreadsheetLayout;
import com.oo.onlyoffice.dto.convert.thumbnail.Thumbnail;
import com.oo.onlyoffice.tools.LoadConfigUtil;
import lombok.Data;

/**
 * @BelongsProject: leaf-onlyoffice
 * @BelongsPackage: com.ideayp.leaf.onlyoffice.dto.convert
 * @Author: TongHui
 * @CreateTime: 2022-11-23 09:41
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class ConvertBody {
    /**
     * 定义转换请求类型：异步或非异步。
     *  默认值为false。
     */
    private Boolean async;
    /**
     * 定义从csv或txt格式转换时的文件编码。
     * 支持的主要值：
     * 932- 日语 （移位 JIS），
     * 950-繁体中文（大5），
     * 1250- 中欧 （视窗），
     * 1251-西里尔文（视窗），
     * 65001- 统一码 （UTF-8）。
     */
    private Integer codePage;
    /**
     * 定义从csv格式转换时用于分隔值的分隔符。
     * 0- 无分隔符，
     * 1-标签，
     * 2-分号，
     * 3-冒号，
     * 4-逗号，
     * 5-空间。
     */
    private Integer delimiter;

    /** 必填 文件类型*/
    private String filetype;
    /** 必填 文件key*/
    private String key;
    /** 必填 输出类型*/
    private String outputtype;
    /**密码*/
    private String password;
    /**地区 默认值为en-US*/
    private String region;

    /**定义转换后的文件名。*/
    private String title;

    /**必填 定义要转换的文档的绝对 URL。*/
    private String url;

    /**token*/
    private String token;

    /**文档布局*/
    private DocumentLayout documentLayout;
    /**文档渲染器*/
    private DocumentRenderer documentRenderer;
    /**表格布局*/
    private SpreadsheetLayout spreadsheetLayout;

    private Thumbnail thumbnail;

    public ConvertBody(String filetype,String key,String outputtype,String url,String title,String password){
        this.filetype =filetype;
        this.key =key;
        this.outputtype = outputtype;
        this.password = password;
        this.url = url;
        this.title = title;
        this.region = "zh-CN";

        JSONObject convert = LoadConfigUtil.getConvert();
        setAsync(convert.getBool("async"));
        setCodePage(convert.getInt("codePage"));
        setDelimiter(convert.getInt("delimiter"));
        setRegion(convert.getStr("region"));

        //判断以下属性是否启用
        JSONObject documentLayout = convert.getJSONObject("documentLayout");
        if (documentLayout.getBool("enabled")){
            setDocumentLayout(convert.get("documentLayout", DocumentLayout.class));
        }

        JSONObject documentRenderer = convert.getJSONObject("documentRenderer");
        if (documentRenderer.getBool("enabled")){
            setDocumentRenderer(convert.get("documentRenderer", DocumentRenderer.class));
        }

        JSONObject spreadsheetLayout = convert.getJSONObject("spreadsheetLayout");
        if (spreadsheetLayout.getBool("enabled")){
            setSpreadsheetLayout(convert.get("spreadsheetLayout", SpreadsheetLayout.class));
        }

        JSONObject thumbnail = convert.getJSONObject("thumbnail");
        if (thumbnail.getBool("enabled")){
            setThumbnail(convert.get("thumbnail",Thumbnail.class));
        }
    }

    public void setAsync(Boolean async) {
        this.async = async;
    }

    public void setCodePage(Integer codePage) {
        this.codePage = codePage;
    }

    public void setDelimiter(Integer delimiter) {
        this.delimiter = delimiter;
    }

    public void setRegion(String region) {
        if (null != region){
            this.region = region;
        }
    }

    public void setDocumentLayout(DocumentLayout documentLayout) {
        this.documentLayout = documentLayout;
    }

    public void setDocumentRenderer(DocumentRenderer documentRenderer) {
        this.documentRenderer = documentRenderer;
    }

    public void setSpreadsheetLayout(SpreadsheetLayout spreadsheetLayout) {
        this.spreadsheetLayout = spreadsheetLayout;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }
}
