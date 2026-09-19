package com.coffemail.views;

import com.coffemail.models.User;
import com.coffemail.services.DuplicateUsernameException;
import com.coffemail.services.RegistrationValidator;
import com.coffemail.services.UserService;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Formulario de registro de usuarios.
 *
 * @author Estuardo Sabán
 */
public class RegisterView extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    private final transient UserService userService = new UserService();

    public RegisterView() {
        initComponents();
        getContentPane().setBackground(Color.DARK_GRAY);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dcBirthDate = new cambodia.raven.DateChooser();
        txtUser = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        txtPasswordConfirm = new javax.swing.JPasswordField();
        jLabel10 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtPathPhoto = new javax.swing.JTextField();
        btnUpload = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        txtName = new javax.swing.JTextField();
        txtBirthDate = new javax.swing.JTextField();
        btnBirthDate = new javax.swing.JButton();
        txtPhone = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();

        dcBirthDate.setButton(btnBirthDate);
        dcBirthDate.setTextRefernce(txtBirthDate);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CoffEmail - Registro");

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Usuario");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("REGISTRARSE");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nombre");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Apellido");

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Password");

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Confirmar Password");

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Correo Electrónico");

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Fecha de Nacimiento");

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Fotografía");

        btnUpload.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnUpload.setText("Seleccionar");
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });

        btnRegister.setBackground(new java.awt.Color(204, 153, 0));
        btnRegister.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btnRegister.setForeground(java.awt.Color.gray);
        btnRegister.setText("Registrar");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        btnBirthDate.setText("...");

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Teléfono");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                    .addComponent(txtName)
                    .addComponent(txtUser)
                    .addComponent(txtPassword)
                    .addComponent(jLabel10)
                    .addComponent(txtPasswordConfirm))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel6)
                        .addComponent(txtEmail)
                        .addComponent(jLabel8)
                        .addComponent(txtPathPhoto)
                        .addComponent(btnUpload, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(txtBirthDate, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnBirthDate, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel7)
                        .addComponent(txtPhone))
                    .addComponent(jLabel9))
                .addGap(86, 110, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(318, 318, 318)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(329, 329, 329)
                        .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel2)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBirthDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBirthDate))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPathPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUpload)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPasswordConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        char[] password = txtPassword.getPassword();
        char[] passwordConfirm = txtPasswordConfirm.getPassword();

        List<String> errors = RegistrationValidator.validate(
                txtUser.getText(), txtName.getText(), txtLastName.getText(), txtEmail.getText(),
                txtPhone.getText(), txtBirthDate.getText(), txtPathPhoto.getText(),
                password, passwordConfirm);
        Arrays.fill(passwordConfirm, '\0');

        if (!errors.isEmpty()) {
            Arrays.fill(password, '\0');
            JOptionPane.showMessageDialog(this,
                    "Corrija lo siguiente:\n\n• " + String.join("\n• ", errors),
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = new User();
        user.setUsername(txtUser.getText().trim());
        user.setName(txtName.getText().trim());
        user.setLastName(txtLastName.getText().trim());
        user.setEmail(txtEmail.getText().trim());
        user.setPhone(txtPhone.getText().trim());
        user.setBirthDate(RegistrationValidator.parseBirthDate(txtBirthDate.getText()).orElseThrow());
        Path photo = new File(txtPathPhoto.getText().trim()).toPath();

        register(user, password, photo);
    }//GEN-LAST:event_btnRegisterActionPerformed

    /**
     * Ejecuta el registro fuera del hilo de eventos.
     *
     * <p>Derivar la contraseña con PBKDF2 es intencionalmente lento (cientos de
     * miles de iteraciones) y además hay que leer y copiar la imagen: hacerlo en
     * el hilo de Swing congelaría la ventana.
     */
    private void register(User user, char[] password, Path photo) {
        btnRegister.setEnabled(false);
        new SwingWorker<User, Void>() {
            @Override
            protected User doInBackground() throws Exception {
                return userService.register(user, password, photo);
            }

            @Override
            protected void done() {
                btnRegister.setEnabled(true);
                try {
                    get();
                    JOptionPane.showMessageDialog(RegisterView.this,
                            "Usuario creado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    clearInputs();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException ex) {
                    showFailure(ex.getCause());
                }
            }
        }.execute();
    }

    private void showFailure(Throwable cause) {
        if (cause instanceof DuplicateUsernameException || cause instanceof IllegalArgumentException) {
            // Errores que el usuario puede corregir: se muestran tal cual.
            JOptionPane.showMessageDialog(this, cause.getMessage(),
                    "No se pudo registrar", JOptionPane.WARNING_MESSAGE);
        } else if (cause instanceof IOException) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el usuario: " + cause.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado al crear el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadActionPerformed
        JFileChooser dialog = new JFileChooser();
        dialog.setAcceptAllFileFilterUsed(false);
        dialog.setFileFilter(new FileNameExtensionFilter(
                "Imágenes (png, jpg, jpeg, gif, bmp)", "png", "jpg", "jpeg", "gif", "bmp"));

        if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtPathPhoto.setText(dialog.getSelectedFile().getPath());
        }
    }//GEN-LAST:event_btnUploadActionPerformed

    private void clearInputs() {
        txtUser.setText("");
        txtName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtPasswordConfirm.setText("");
        txtPhone.setText("");
        txtBirthDate.setText("");
        txtPathPhoto.setText("");
    }

    /**
     * @param args argumentos de línea de comandos (no se usan)
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegisterView.class.getName())
                    .log(java.util.logging.Level.WARNING, "No se pudo aplicar el look and feel Nimbus", ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new RegisterView().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBirthDate;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnUpload;
    private cambodia.raven.DateChooser dcBirthDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtBirthDate;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JPasswordField txtPasswordConfirm;
    private javax.swing.JTextField txtPathPhoto;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables

}
