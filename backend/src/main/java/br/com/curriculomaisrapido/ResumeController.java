package br.com.curriculomaisrapido;
import java.util.Map;
import java.util.Set;
import org.apache.tika.Tika;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/** Stateless, opt-in extraction. No file or profile persistence. */
@RestController
@RequestMapping("/api")
public class ResumeController {
 private static final Set<String> TYPES=Set.of("application/pdf","application/vnd.openxmlformats-officedocument.wordprocessingml.document","text/plain");
 @GetMapping("/health") public Map<String,String> health(){return Map.of("status","ok","service","curriculo-mais-rapido");}
 @PostMapping("/resume/extract") public Map<String,Object> extract(@RequestParam("file") MultipartFile file){
  if(file.isEmpty()||file.getSize()>5*1024*1024)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Use um arquivo de até 5 MB.");
  try(var stream=file.getInputStream()){
   Tika tika=new Tika();String type=tika.detect(file.getBytes());
   // An OOXML file may initially be detected as ZIP; inspect its metadata via filename and parser.
   if(!TYPES.contains(type))throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE,"Use PDF, DOCX ou TXT.");
   String text=tika.parseToString(stream,new org.apache.tika.metadata.Metadata(),200_000);
   if(text.trim().length()<15)throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Sem texto selecionável. Cole o conteúdo manualmente.");
   return Map.of("text",text,"reviewRequired",true);
  }catch(ResponseStatusException e){throw e;}catch(Exception e){throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Não foi possível ler este currículo.");}
 }
}
