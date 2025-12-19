package org.zhuhsh.travelbooking.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;

@RestController
public class CaptchaController {

    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    @GetMapping("/api/auth/captcha")
    public String getCaptcha(HttpSession session) throws Exception {
        String code = generateCode(5);
        session.setAttribute("CAPTCHA", code);

        BufferedImage image = draw(code);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);

        return "data:image/png;base64," +
                Base64.getEncoder().encodeToString(out.toByteArray());
    }

    private String generateCode(int len) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(CHARS.charAt(r.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    private BufferedImage draw(String text) {
        BufferedImage img = new BufferedImage(160, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, 160, 50);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.setColor(Color.BLACK);
        g.drawString(text, 20, 38);
        g.dispose();

        return img;
    }
}
