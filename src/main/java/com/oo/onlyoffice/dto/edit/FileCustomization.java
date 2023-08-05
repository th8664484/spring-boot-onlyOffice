package com.oo.onlyoffice.dto.edit;

import lombok.Data;

import java.io.Serializable;

/**
*
*@Author TongHui
*@Date 2022/11/16
*@ClassName FileCustomization
*@Version 1.0
*/
@Data
public class FileCustomization implements Serializable {

    /**
     * {
     * "request": true,
     * "label": "Guest"
     * },
     * 添加对匿名名称的请求：
     * 请求 - 定义请求是否发送。 默认值为 true，类型：布尔值，
     * 标签 - 添加到用户名的后缀。 默认值为来宾，类型：字符串，
     */
    private Object anonymous;

    /**
     * 定义是否启用或禁用“自动保存”菜单选项。
     * 如果设置为false，则只能选择Strict协同编辑模式，因为Fast在没有自动保存的情况下无法工作。
     * 请注意，如果您在菜单中更改此选项，它将保存到您的浏览器localStorage。默认值为true
     */
    private Boolean autosave;

    /**
    定义是显示还是隐藏“注释”菜单按钮。
     请注意，如果您隐藏了“评论”按钮，则相应的评论功能将仅供查看，添加和编辑评论将不可用。 默认值为true。
     */
    private Boolean comments;
    /**
     * 定义其他操作按钮是显示在编辑器窗口标题的上部徽标旁边 （false） 还是显示在工具栏 （true） 中，从而使标题更紧凑。 默认值为false。
     */
    private Boolean compactHeader;
    /**
     * 定义显示的顶部工具栏类型是完整 （假） 还是紧凑 （真）。 默认值为false。
     */
    private Boolean compactToolbar;
    /**
     * 定义仅与 OOXML 格式兼容的功能的使用。 例如，不要对整个文档使用注释。 默认值为false。
     */
    private Boolean compatibleFeatures;
    /**
     * "address": "My City, 123a-45",有权访问编辑或编辑作者的公司或个人的邮政地址
     * "info": "Some additional information",有关您希望其他人认识的公司或个人的一些其他信息，
     * "logo": "https://example.com/logo-big.png",图片徽标的路径
     * "logoDark": "https://example.com/dark-logo-big.png",深色主题的图像徽标的路径
     * "mail": "john@example.com",有权访问编辑者或编辑者的公司或个人的电子邮件地址
     * "name": "John Smith and Co.",名称
     * "phone": "123456789",电话
     * "www": "example.com"以上公司或个人的家庭网站地址
     */
    private Object customer;
    /**
     * {"spellcheck":  true}
     * 定义用户可以禁用或自定义的参数（如果可能）：
     * 拼写检查 - 定义在加载编辑器时是自动打开还是关闭拼写检查器。 如果此参数是布尔值，则将其设置为初始拼写检查器值，并且不会隐藏拼写检查器设置。
     * 默认值为 true，类型：对象或布尔值，
     *  {"spellcheck": {"mode": true}}
     * 拼写检查模式 - 定义在加载编辑器时拼写检查器是自动打开还是关闭。
     * 此参数仅适用于文档编辑器和演示文稿编辑器，类型：布尔值，
     * 示例：true;
     */
    private Object features;
    /**
     * {"url": "https://example.com",
     * "visible": true}
     * 定义“反馈和支持”菜单按钮的设置。
     * 可以是布尔值（仅显示或隐藏“反馈和支持”菜单按钮）或对象。
     */
    private Object feedback;
    /**在文档编辑服务中保存文档时（例如，单击“保存”按钮等），
     * 将文件强制保存的请求添加到回调处理程序中。 默认值为false。*/
    private Boolean forcesave;

    /**
     * {
     * "blank": true,
     * "requestClose": false,
     * "text": "Open file location",
     * "url": "https://example.com"
     * }
     * 定义“打开文件位置”菜单按钮和右上角按钮的设置。 该对象具有以下参数：
     * 空白 - 单击“打开文件位置”按钮时，在新的浏览器选项卡/窗口中打开网站（如果值设置为true）或当前选项卡（如果值设置为false）。
     * 默认值为 true，类型：布尔值，

     * requestClose- 定义如果单击“打开文件位置”按钮，则会调用 events.onRequestClose事件，而不是打开浏览器选项卡或窗口。
     * 默认值为假，类型：布尔值，
     *

     * text-将为“打开文件位置”菜单按钮和右上角按钮（即而不是“转到文档”）显示的文本，键入：字符串，

     * url-单击“打开文件位置”菜单按钮时将打开的网站地址的绝对URL，键入：字符串，
     *
     */
    private Object goback;
    /**
     * 定义是显示还是隐藏“帮助”菜单按钮。 默认值为true。
     */
    private Boolean help;
    /**
     * 定义在首次加载时是显示还是隐藏注释面板。 默认值为false。此参数仅适用于演示文稿编辑器ppt。
     */
    private Boolean hideNotes;
    /**
     * 定义首次加载时是显示还是隐藏右侧菜单。 默认值为false。
     */
    private Boolean hideRightMenu;
    /**
     * 定义是显示还是隐藏编辑器标尺。 此参数可用于文档和演示文稿编辑器。文档编辑器的默认值为false，演示文稿的默认值为true。
     */
    private Boolean hideRulers;

    /**
     * 定义将编辑器嵌入网页的模式。 嵌入值在加载编辑器框架时禁用滚动到编辑器框架，因为未捕获焦点。
     */
    private String integrationMode;

    /**
     * "image": "https://example.com/logo.png",
     * "imageDark": "https://example.com/dark-logo.png",
     * "url": "https://www.onlyoffice.com"
     * 更改编辑器标题左上角的图像文件。 建议的图像高度为 20 像素。 该对象具有以下参数：
     * image- 用于在通用工作模式（即所有编辑器的查看和编辑模式下）或嵌入模式下显示的图像文件的路径（请参阅配置部分以了解如何定义嵌入式文档类型）。
     * 图像必须具有以下大小：172x40，类型：字符串，
     *
     * imageDark - 用于深色主题的图像文件的路径。 图像必须具有以下大小：172x40，类型：字符串，
     *
     * url - 当有人点击徽标图像时将使用的绝对URL（可用于访问您的网站等）。
     * 保留为空字符串或null以使徽标不可点击，键入：字符串，
     */
    private Object logo;

    /**
     * 定义在编辑器打开时是否自动运行文档宏。 默认值为true。false值对用户隐藏宏设置。
     */
    private Boolean macros;
    /**
     * disable
     * enable
     * warn 默认
     */
    private String macrosMode;
    /**
     *定义在注释中提及后描述事件的提示。
     * 如果为 true，则提示指示用户将收到通知并访问文档。
     * 如果为 false，则提示指示用户将仅收到提及通知。 默认值为true。
     * 请注意，只有在设置了onRequestSendNotify事件的情况下，它才可用于注释。
     */
    private Boolean mentionShare;
    /**
     * 定义插件是否启动并可用。 默认值为true。
     */
    private Boolean plugins;

    /**
     * "hideReviewDisplay": false, 定义在“协作”选项卡上显示还是隐藏“显示模式”按钮。 默认值为假，类型：布尔值，
     * "hoverMode": false         定义审阅显示模式：通过悬停更改在工具提示中显示审阅 （true） 或通过单击更改（假）在气球中。 默认值为false。
     */
    private Object review;

    /**
     * 定义文档标题是在顶部工具栏上可见 （假） 还是隐藏 （真）。 默认值为false。
     */
    private Boolean toolbarHideFileName;

    /**
     * 定义顶部工具栏选项卡是清晰显示 （false） 还是仅突出显示以查看选择了哪个选项卡 （true）。 默认值为false。
     */
    private Boolean toolbarNoTabs;

    /**
     *定义编辑器主题设置。 可以通过两种方式进行设置：
     * 主题 ID - 用户通过其 ID 设置主题参数（主题-浅色、主题-经典-浅色、主题-深色、主题-对比度-深），
     * 默认主题 - 将设置默认的深色或浅色主题值（默认深色，默认浅色）。 默认浅色主题为主题经典浅色。
     * 第一个选项具有更高的优先级。除了可用的编辑器主题外，用户还可以为应用程序界面自定义自己的颜色主题。
     * 颜色可以以十六进制或 RGBA 格式呈现。
     */
    private String uiTheme;

    /**
     * 定义标尺和对话框中使用的度量单位。 可以采用以下值：
     * 厘米，
     * pt-点，
     * 英寸 - 英寸。
     * 默认值为厘米 （cm）。
     */
    private String unit;

    /**
     * 定义以百分比度量的文档显示缩放值。 可以采用大于0 的值。
     * 对于文本文档和演示文稿，可以将此参数设置为 -1（使文档适合页面选项）或 to-2（使文档页面宽度适合编辑器页面）。
     * 默认值为100。
     */
    private Integer zoom;



}
