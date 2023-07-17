package br.com.mundim.CarRent.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String to;
    private String subject;
    private String htmlMailContent;
    private LocalDateTime timeStamp;

    public Mail(String to, String subject, String htmlMailContent){
        this.to = to;
        this.subject = subject;
        this.htmlMailContent = htmlMailContent;
        this.timeStamp = LocalDateTime.now();
    }

}
