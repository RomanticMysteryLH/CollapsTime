package henu.javaweb.collapstime.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;


public interface MailService {
    public void sendSimpleMail(String to,String title,String content);
    public void sendAttachmentsMail(String to, String title, String cotent, List<File> fileList);
}
