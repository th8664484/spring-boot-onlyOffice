package com.oo.onlyoffice.tools;


import com.oo.onlyoffice.core.enums.DocumentTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class OnlyOfficeUtil {


    /**
     * 通过文件名获取文件类型
     *
     * @param fileName 文件名
     * @return 结果
     */
    public static String getDocumentType(String fileName) {
        // 获取文件的扩展名
        String ext = "." + FileUtil.getFileExtension(fileName);
        if (ExtsDocument.contains(ext)) {
            return DocumentTypeEnum.Word.toString().toLowerCase();
        } else if (ExtsSpreadsheet.contains(ext)) {
            return DocumentTypeEnum.Cell.toString().toLowerCase();
        } else if (ExtsPresentation.contains(ext)) {
            return DocumentTypeEnum.Slide.toString().toLowerCase();
        }
        return DocumentTypeEnum.Word.toString().toLowerCase();
    }

    private static List<String> ExtsDocument = Arrays.asList(
            ".doc", ".docx", ".docm",
            ".dot", ".dotx", ".dotm",
            ".odt", ".fodt", ".ott", ".rtf", ".txt",
            ".html", ".htm", ".mht",
            ".pdf", ".djvu", ".fb2", ".epub", ".xps");

    private static List<String> ExtsSpreadsheet = Arrays.asList(
            ".xls", ".xlsx", ".xlsm",
            ".xlt", ".xltx", ".xltm",
            ".ods", ".fods", ".ots", ".csv");

    private static List<String> ExtsPresentation = Arrays.asList(
            ".pps", ".ppsx", ".ppsm",
            ".ppt", ".pptx", ".pptm",
            ".pot", ".potx", ".potm",
            ".odp", ".fodp", ".otp");
}
