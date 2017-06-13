package cn.superman.web.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.mail.Session;

import org.springframework.stereotype.Service;

import cn.superman.constant.ConstantParameter;
import cn.superman.util.Log4JUtil;
import cn.superman.web.constant.MailConstant;
import cn.superman.web.util.mail.Mail;
import cn.superman.web.util.mail.SendMailUtil;

@Service
public class EmailService {

    private ExecutorService emailExecutorService = Executors.newCachedThreadPool();
    private Semaphore emailThreadSemaphore = new Semaphore(30);

    public void sendEmail(EmailRunnable runnable) {
        try {
            emailThreadSemaphore.acquire();
            emailExecutorService.execute(runnable);
        } catch (Exception e) {
            Log4JUtil.logError(e);
        } finally {
            emailThreadSemaphore.release();
        }
    }

    public static class EmailRunnable implements Runnable {
        private Mail mail;
        private Session session;

        public EmailRunnable(String emailSubject, String emailContent, String emailReceiver) {

            session = SendMailUtil.createDefaultSession(MailConstant.host.getValue(), MailConstant.userName.getValue(), MailConstant.password.getValue());
            mail = new Mail(MailConstant.userName.getValue(), emailReceiver);
            mail.setSubject(emailSubject);
            mail.setContent(emailContent);
        }

        @Override
        public void run() {
            try {
                SendMailUtil.sendMail(mail, session, ConstantParameter.DEFAULT_CHARSET_CODE);
            } catch (Exception e) {
                Log4JUtil.logError(e);
            }
        }
    }
}
