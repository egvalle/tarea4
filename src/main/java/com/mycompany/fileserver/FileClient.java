/* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fileserver;
/** @author G3OVA */

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

public class FileClient extends javax.swing.JFrame {

    public FileClient() {
        initComponents();
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            jLabelIP.setText("IP Local: " + ip);
        } catch (UnknownHostException ex) {
            Logger.getLogger(FileClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initComponents() {
        jButtonOpen = new javax.swing.JButton();
        jButtonSend = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableFiles = new javax.swing.JTable();
        jLabelIP = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButtonOpen.setText("Agregar");
        jButtonOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenActionPerformed(evt);
            }
        });

        jButtonSend.setText("Enviar");
        jButtonSend.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendActionPerformed(evt);
            }
        });

        jTableFiles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Origen", "Destino"}
        ) {
            Class[] types = new Class[] {java.lang.String.class, java.lang.String.class};
            boolean[] canEdit = new boolean[] {false, false};

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableFiles);

        jLabelIP.setText("IP Local: 192.168.1.1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonOpen)
                .addGap(18, 18, 18)
                .addComponent(jButtonSend)
                .addGap(18, 18, 18)
                .addComponent(jLabelIP)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOpen)
                    .addComponent(jButtonSend)
                    .addComponent(jLabelIP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }

    private void jButtonOpenActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        DefaultTableModel defaultTableModel = (DefaultTableModel) jTableFiles.getModel();
        int result = jFileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser.getSelectedFile();
            defaultTableModel.addRow(new Object[]{selectedFile.getAbsolutePath(), ""});
        }
    }

    private void jButtonSendActionPerformed(java.awt.event.ActionEvent evt) {
        DefaultTableModel defaultTableModel = (DefaultTableModel) jTableFiles.getModel();
        int selectedRow = jTableFiles.getSelectedRow();
        if (selectedRow != -1) {
            String sourcePath = (String) defaultTableModel.getValueAt(selectedRow, 0);
            String serverAddress = JOptionPane.showInputDialog("Ingrese la dirección del servidor:");

            try (Socket socket = new Socket(serverAddress, 1234);
                 FileInputStream fis = new FileInputStream(sourcePath);
                 DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

                File fileToSend = new File(sourcePath);
                dos.writeLong(fileToSend.length());
                byte[] buffer = new byte[4096];

                int read = 0;
                while ((read = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, read);
                }

                defaultTableModel.setValueAt(serverAddress, selectedRow, 1);
                JOptionPane.showMessageDialog(this, "Archivo enviado con éxito.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al enviar el archivo.");
            }
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileClient().setVisible(true);
            }
        });
    }

    private javax.swing.JButton jButtonOpen;
    private javax.swing.JButton jButtonSend;
    private javax.swing.JLabel jLabelIP;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableFiles;
}
