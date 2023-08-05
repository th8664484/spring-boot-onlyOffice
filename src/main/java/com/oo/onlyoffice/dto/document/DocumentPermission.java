package com.oo.onlyoffice.dto.document;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * file 权限
 */
@Data
@NoArgsConstructor
public class DocumentPermission implements Serializable {

    /**
     * 定义是否在文档中启用聊天功能。 如果聊天权限设置为true，将显示聊天菜单按钮。 默认值为true。
     * */
    private Boolean chat;

    /**
     * 定义文档是否可以被注释。
     * 在注释权限设置为“true”的情况下，文档侧栏将包含“注释”菜单选项；
     * 如果该模式参数设置为“编辑”，则文档注释仅可用于文档编辑器。
     * 默认值与编辑参数的值一致。
     * 默认为true
     */
    private Boolean comment;
    /***
     "edit": ["Group2", ""],
     "remove": [""],
     "view": "" ,
     定义用户可以编辑、删除和/或查看其注释的组。 该对象具有以下参数：
         编辑 - 用户可以编辑其他用户的评论，类型：列表，例如：[“Group2”，“”];
         删除 - 用户可以删除其他用户的评论，类型：列表，
         示例：[];
         查看 - 用户可以查看其他用户的评论，类型：列表，
     [“”]值表示用户可以编辑/删除/查看不属于这些组的人员所做的评论（例如，如果在第三方编辑器中审阅文档）。
     如果值为 []，则用户无法编辑/删除/查看任何组所做的注释。
     如果编辑、删除和查看参数为“”或未指定，则用户可以查看/编辑/删除任何用户所做的评论。
     */
    private Object commentGroups;

    /**
     * 定义是否可以将内容复制到剪贴板。
     * 如果参数设置为false，则粘贴内容将仅在当前文档编辑器中可用。 默认值为true。
     */
    private Boolean copy;

    /**
     * 定义用户是否只能删除其注释。 默认值为false。
     */
    private Boolean deleteCommentAuthorOnly;
    /**
     * 是否允许下载 默认为true
     */
    private Boolean download;
    /**
     * 是否允许编辑 默认true
     */
    private Boolean edit;

    /**
     * 定义用户是否只能编辑其注释。 默认值为false。
     */
    private Boolean editCommentAuthorOnly;

    /**
     * 是否可以填写表单 默认true
     * 如果编辑设置为“true”或审阅设置为“true”，则不考虑fillForms值，并且可以填写表单。
     * 如果编辑设置为“false”，审阅设置为“false”，并且fillForms也设置为“true”，则用户只能在文档中填写表单。
     * 如果编辑设置为“false”并且审阅设置为“false”并且fillForms设置为“true”则不考虑注释值，并且注释不可用。
     * 仅表单填写模式目前仅适用于文档编辑器。
     */
    private Boolean fillForms;
    /**
     * 定义是否可以更改内容控件设置。 仅当mode参数设置为编辑时，内容控件修改才可用于文档编辑器。 默认值为true。
     */
    private Boolean modifyContentControl;
    /**
     * 定义筛选器是可以全局应用（true）影响所有其他用户，还是本地应用（false），即仅适用于当前用户。
     * 仅当模式参数设置为编辑时，过滤器修改才可用于电子表格编辑器。 默认值为true。
     */
    private Boolean modifyFilter;
    /**
     * 定义文档是否可以打印。 如果打印权限设置为“false”打印菜单选项将不在“文件”菜单中。 默认值为true。
     */
    private Boolean print;
    /**
     * 定义是显示工具栏上的“保护”选项卡和左侧菜单中的“保护”按钮（真）还是隐藏（假）。 默认值为true。
     */
    private Boolean protect;
    /**
     * 定义文档是否可以被审核。
     * 如果审阅权限设置为“true”，则文档状态栏将包含审阅菜单选项；
     * 如果模式参数设置为编辑，则文档审阅将仅对文档编辑器可用。
     * 默认值与编辑参数的值一致。
     * 如果编辑设置为“true”并且审阅也设置为“true”，则用户将能够编辑文档，接受/拒绝所做的更改并自己切换到审阅模式。
     * 如果编辑设置为“true”并且审阅设置为“false”，则用户将只能进行编辑。 如果编辑设置为“false”并且审阅设置为“true”，则文档将仅在审阅模式下可用。
     * 默认：true
     */
    private Boolean review;
    /**
     * 定义用户可以接受/拒绝其更改的组。 [“”]值表示用户可以审阅不属于这些组的人员所做的更改（例如，如果在第三方编辑器中审阅文档）。
     * 如果值为 []，则用户无法查看任何组所做的更改。 如果值为“”或未指定，则用户可以查看任何用户所做的更改。
     */
    private String[] reviewGroups;
    /**
     * 定义其信息显示在编辑器中的用户组：
     * 用户名显示在编辑器标题的编辑用户列表中，
     * 键入文本时，将显示用户光标和工具提示及其名称，
     * 在严格协同编辑模式下锁定对象时，将显示用户名。
     * [“组 1”， “”] 表示显示有关组 1 中的用户和不属于任何组的用户的信息。 [] 表示根本不显示任何用户信息。 未定义的或 “” 值表示显示有关所有用户的信息。
     */
    private String[] userInfoGroups;

}
