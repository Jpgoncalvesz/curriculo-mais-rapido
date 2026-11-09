package br.com.curriculomaisrapido;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ResumeApiTest {
 @Autowired MockMvc mvc;
 @Test void healthReturnsOk() throws Exception {mvc.perform(get("/api/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ok"));}
 @Test void extractsActualPdf() throws Exception {
  byte[] bytes;try(var doc=new PDDocument();var out=new ByteArrayOutputStream()){
   var page=new PDPage();doc.addPage(page);
   try(var content=new PDPageContentStream(doc,page)){content.beginText();content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA),12);content.newLineAtOffset(40,700);content.showText("Ana Silva - ana@example.com - Analista");content.endText();}
   doc.save(out);bytes=out.toByteArray();
  }
  mvc.perform(multipart("/api/resume/extract").file(new MockMultipartFile("file","cv.pdf","application/pdf",bytes))).andExpect(status().isOk()).andExpect(jsonPath("$.text").value(org.hamcrest.Matchers.containsString("Ana Silva"))).andExpect(jsonPath("$.reviewRequired").value(true));
 }
 @Test void extractsActualDocx() throws Exception {
  byte[] bytes;try(var doc=new XWPFDocument();var out=new ByteArrayOutputStream()){doc.createParagraph().createRun().setText("Ana Silva - ana@example.com - Analista administrativa");doc.write(out);bytes=out.toByteArray();}
  mvc.perform(multipart("/api/resume/extract").file(new MockMultipartFile("file","cv.docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document",bytes))).andExpect(status().isOk()).andExpect(jsonPath("$.text").value(org.hamcrest.Matchers.containsString("Ana Silva")));
 }
 @Test void rejectsMissingUpload() throws Exception {mvc.perform(multipart("/api/resume/extract")).andExpect(status().isBadRequest());}
 @Test void rejectsEmptyUpload() throws Exception {mvc.perform(multipart("/api/resume/extract").file(new MockMultipartFile("file","empty.txt","text/plain",new byte[0]))).andExpect(status().isBadRequest());}
 @Test void rejectsBinaryDisguisedAsPdf() throws Exception {mvc.perform(multipart("/api/resume/extract").file(new MockMultipartFile("file","cv.pdf","application/pdf",new byte[]{0,1,2,3,4,5,0,1,0,0,0,7,8,0,0,0}))).andExpect(status().isUnsupportedMediaType());}
 @Test void rejectsTextWithoutUsefulContent() throws Exception {mvc.perform(multipart("/api/resume/extract").file(new MockMultipartFile("file","cv.txt","text/plain","Ana".getBytes(StandardCharsets.UTF_8)))).andExpect(status().isUnprocessableEntity());}
 @Test void permitsConfiguredDevelopmentOrigin() throws Exception {mvc.perform(options("/api/resume/extract").header("Origin","http://127.0.0.1:5173").header("Access-Control-Request-Method","POST")).andExpect(status().isOk()).andExpect(header().string("Access-Control-Allow-Origin","http://127.0.0.1:5173"));}
 @Test void rejectsUnknownOrigin() throws Exception {mvc.perform(options("/api/resume/extract").header("Origin","https://unknown.example").header("Access-Control-Request-Method","POST")).andExpect(status().isForbidden());}
}
