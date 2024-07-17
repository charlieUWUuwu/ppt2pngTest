package org.example;

import com.liumapp.workable.converter.WorkableConverter;
import com.liumapp.workable.converter.core.ConvertPattern;
import com.liumapp.workable.converter.exceptions.ConvertFailedException;
import com.liumapp.workable.converter.factory.CommonConverterManager;
import com.liumapp.workable.converter.factory.ConvertPatternManager;
import com.liumapp.workable.converter.factory.PdfBoxConverterManager;
import org.jodconverter.document.DefaultDocumentFormatRegistry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Main {
    public static void main(String[] args) throws ConvertFailedException, FileNotFoundException {
        WorkableConverter converter = new WorkableConverter();
        ConvertPattern pattern = ConvertPatternManager.getInstance();

        // 設置 pdf 到 png 的轉換
        pattern.setSrcFilePrefix(DefaultDocumentFormatRegistry.PDF);
        pattern.setDestFilePrefix(DefaultDocumentFormatRegistry.PNG);

        pattern.fileToFiles("./data/pdf/result.pdf", "./data/png/");

        converter.setConverterType(PdfBoxConverterManager.getInstance());

        long startTime = System.currentTimeMillis(); // 記錄開始時間
        boolean pdfToPngResult = converter.convert(pattern.getParameter());
        long endTime = System.currentTimeMillis(); // 記錄結束時間

        long pngConvertDuration = endTime - startTime; // 計算 pdf 到 png 轉換時間
        System.out.println("PDF 轉 PNG 耗時: " + pngConvertDuration / 1000.0 + " 秒");

        if (pdfToPngResult) {
            System.out.println("PDF 轉 PNG 成功");

            // 檢查生成的 PNG 檔案是否存在
            File pngDir = new File("./data/png/");
            File[] pngFiles = pngDir.listFiles();
            if (pngFiles != null) {
                for (File pngFile : pngFiles) {
                    System.out.println(pngFile.getName() + " 存在");
                }
            }
        } else {
            System.out.println("PDF 轉 PNG 失敗");
        }
    }
}