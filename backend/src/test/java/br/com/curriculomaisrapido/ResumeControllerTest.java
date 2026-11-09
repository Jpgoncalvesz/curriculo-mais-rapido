package br.com.curriculomaisrapido;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
class ResumeControllerTest {
 private final ResumeController controller=new ResumeController();
 @Test void extractsTextAndRequiresReview(){var f=new MockMultipartFile("file","cv.txt","text/plain","Ana Silva\nana@example.com\nExperiência profissional: Analista".getBytes(java.nio.charset.StandardCharsets.UTF_8));var result=controller.extract(f);assertTrue(result.get("text").toString().contains("Ana Silva"));assertEquals(true,result.get("reviewRequired"));}
 @Test void rejectsEmpty(){assertThrows(ResponseStatusException.class,()->controller.extract(new MockMultipartFile("file",new byte[0])));}
 @Test void rejectsOversized(){assertThrows(ResponseStatusException.class,()->controller.extract(new MockMultipartFile("file",new byte[5*1024*1024+1])));}
 @Test void rejectsImage(){byte[] png={(byte)137,80,78,71,13,10,26,10,0,0,0,13,73,72,68,82};assertThrows(ResponseStatusException.class,()->controller.extract(new MockMultipartFile("file","a.png","image/png",png)));}
}
