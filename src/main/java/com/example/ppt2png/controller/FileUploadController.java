package com.example.ppt2png.controller;

import org.apache.poi.xslf.usermodel.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        try {
            // Save the file locally
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convFile);

            // Convert PPT to images
            convertPptToImages(convFile);

            return ResponseEntity.ok("File uploaded and converted successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    private void convertPptToImages(File pptFile) {
        try (FileInputStream is = new FileInputStream(pptFile)) {
            XMLSlideShow ppt = new XMLSlideShow(is);
            Dimension pgsize = ppt.getPageSize();

            System.out.println("這是要列印的訊息1");

            int slideNum = 1;
            int i=0;
            for (XSLFSlide slide : ppt.getSlides()) {

                handlerPPTXEncoding(ppt, i);

                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();

                System.out.println("這是要列印的訊息2");

                // Clear the drawing area
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

                System.out.println("這是要列印的訊息3");

                // Render
                slide.draw(graphics);

                System.out.println("這是要列印的訊息4");

                // Save the output
                String outputFilename = System.getProperty("java.io.tmpdir") + "/slide-" + slideNum + ".png";
                ImageIO.write(img, "png", new File(outputFilename));
                slideNum++;

                System.out.println("這是要列印的訊息5");

                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handlerPPTXEncoding(XMLSlideShow ppt, int index) {
        for (XSLFShape shape : ppt.getSlides().get(index).getShapes()) {
            if (shape instanceof XSLFTextShape) {
                XSLFTextShape tsh = (XSLFTextShape) shape;
                for (XSLFTextParagraph p : tsh) {
                    for (XSLFTextRun r : p) {
                        String fontFamily = r.getFontFamily();
                        fontFamily = "宋体";
                        r.setFontFamily(fontFamily);
                    }
                }
            }
        }
        }
}
