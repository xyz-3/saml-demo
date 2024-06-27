package org.data.mujinaidpplatform.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import org.data.mujinaidpplatform.dto.ValidateCodeDto;
import org.data.mujinaidpplatform.dto.Validation;
import org.data.mujinaidpplatform.repository.CredentialRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GAController {
    private final GoogleAuthenticator gAuth;

    private final CredentialRepository credentialRepository;

    @GetMapping("/generate/{username}")
    public String generate(@PathVariable String username, RedirectAttributes redirectAttributes) throws WriterException, IOException {
        final GoogleAuthenticatorKey key = gAuth.createCredentials(username);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("saml-demo", username, key);

        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        String base64 = new String(Base64.getEncoder().encode(outputStream.toByteArray()));
        outputStream.close();

        redirectAttributes.addFlashAttribute("qrcodebase64", base64);
        return "redirect:/qrcode.html";
    }

    @GetMapping({"qrcode", "/qrcode.html"})
    public String qrcode(){
        return "qrcode";
    }

    @PostMapping("/validate/key")
    public Validation validateKey(@RequestBody ValidateCodeDto body) {
        Validation res = new Validation(gAuth.authorizeUser(body.getUsername(), body.getCode()));
        return res;
    }

    @GetMapping("/scratches/{username}")
    public List<Integer> getScratches(@PathVariable String username) {
        return getScratchCodes(username);
    }

    private List<Integer> getScratchCodes(@PathVariable String username) {
        return credentialRepository.getUser(username).getScratchCodes();
    }

    @PostMapping("/scratches/")
    public Validation validateScratch(@RequestBody ValidateCodeDto body) {
        List<Integer> scratchCodes = getScratchCodes(body.getUsername());
        Validation validation = new Validation(scratchCodes.contains(body.getCode()));
        scratchCodes.remove(body.getCode());
        return validation;
    }
}
