package main.answers;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StatusLoad {
   List<LoadLine> log;

   public StatusLoad(){
       log = new ArrayList<>();
   }

   public void addLog(LoadLine logLine){
       log.add(logLine);
   }
}
