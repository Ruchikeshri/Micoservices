package com.example.myproj;

import org.springframework.stereotype.Service;

@Service
public class EmailSender {
    public static void sendEmail(String email)
    {
        System.out.println("sending message from blog: "+ email);
    }
}
