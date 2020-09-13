package com.coffemail.controllers;

import com.coffemail.models.User;
import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Estuardo Sabán
 */
public class UserController {

    private static MessageDigest md;

    private static byte[] GetSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    public String EncryptPassword(String userPassword, byte[] salt) {
        String generatedPassword = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(salt);
            byte[] passBytes = md.digest(userPassword.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < passBytes.length; i++) {
                sb.append(Integer.toString((passBytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return generatedPassword;
    }

    public String GetExtension(String pathPhoto) {
        int lastIndex = pathPhoto.lastIndexOf(".");
        if (lastIndex == -1) {
            return ""; // empty extension
        }
        return pathPhoto.substring(lastIndex);
    }

    public String CopyPhoto(String username, String pathPhoto) {
        Path src = Paths.get(pathPhoto);
        Path destiny = Paths.get("C:/MEIA/images/");
        try {
            Files.createDirectories(destiny);
            Files.copy(src, destiny.resolve(src.getFileName()).resolveSibling(username
                    + GetExtension(pathPhoto)), StandardCopyOption.REPLACE_EXISTING);
            return destiny + "\\" + username + GetExtension(pathPhoto);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return "";
    }

    public boolean WriteUser(User user) throws NoSuchAlgorithmException, NoSuchProviderException {
        String path = "C:/MEIA/usuario.txt";
        File file = new File(path);
        byte[] salt = GetSalt();

        if (file.exists() && !file.isDirectory()) {
            try {
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.newLine();
                bw.write(user.getUser() + "|" + user.getName() + "|" + user.getLastName() + "|"
                        + EncryptPassword(user.getPassword(), salt) + "|" + user.getRole() + "|" + user.getBirthDate() + "|"
                        + user.getEmail() + "|" + user.getPhone() + "|" + CopyPhoto(user.getUser(), user.getPathPhoto()) + "|"
                        + user.getStatus()
                );
                bw.close();
                fw.close();
                return true;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
        } else {
            try {
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);

                bw.write("usuario|nombre|apellido|password|rol|fechaNacimiento|correo|telefono|pathFotografia|estatus");
                bw.newLine();
                bw.write(user.getUser() + "|" + user.getName() + "|" + user.getLastName() + "|"
                        + EncryptPassword(user.getPassword(), salt) + "|" + user.getRole() + "|" + user.getBirthDate() + "|"
                        + user.getEmail() + "|" + user.getPhone() + "|" + CopyPhoto(user.getUser(), user.getPathPhoto()) + "|"
                        + user.getStatus()
                );
                bw.close();
                fw.close();
                return true;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
        }
    }
}
